import os
import time
import random
import onnxruntime as ort
import numpy as np
from PIL import Image
from transformers import CLIPProcessor


class FoodClassifier:
    def __init__(self, model_dir, providers=None):
        """
        model_dir: path where 'model.onnx' and 'model.data' are stored
        providers: ONNX runtime providers list (CUDAExecutionProvider, CPUExecutionProvider, etc.)
        """
        onnx_path = onnx_path = os.path.join(model_dir, "model.onnx")

        self.labels = [ # only working on these label now
            "apple", "banana", "orange", "tomato", "potato", "carrot",
            "grape", "onion", "mango", "pomegranate", "pineapple",
            "cabbage", "spaghetti", "fried_rice"
        ]

        self.calories = {
            "apple": 52, "banana": 96, "orange": 43, "tomato": 18, "potato": 77,
            "carrot": 41, "grape": 69, "onion": 40, "mango": 60, "pomegranate": 83,
            "pineapple": 50, "cabbage": 25, "spaghetti": 120, "fried_rice": 200
        }

        # Hugging Face processor (for CLIP-style preprocessing + tokenization)
        self.processor = CLIPProcessor.from_pretrained("openai/clip-vit-base-patch16")

        # Load ONNX runtime session
        self.session = ort.InferenceSession(
            onnx_path,
            providers=providers or ['CUDAExecutionProvider', 'CPUExecutionProvider']
        )

        # Debug info
        model_size_mb = os.path.getsize(onnx_path) / (1024 * 1024)
        print(f"📦 Model size: {model_size_mb:.2f} MB")
        print("Inputs:", [i.name for i in self.session.get_inputs()])
        print("Outputs:", [o.name for o in self.session.get_outputs()])

    def predict_image(self, img_path):
        """Predict best label + kcal for a single image."""
        img = Image.open(img_path).convert("RGB")

        # Preprocess image
        inputs = self.processor(images=img, return_tensors="np")
        image_tensor = inputs["pixel_values"].astype("float32")

        # Collect scores
        scores = []
        start_time = time.time()
        for label in self.labels:
            text_inputs = self.processor.tokenizer(
                [label], padding="max_length", truncation=True, max_length=77,
                return_tensors="np"
            )
            text_ids = text_inputs["input_ids"].astype("int32")

            ort_inputs = {"image": image_tensor, "text": text_ids}
            ort_outputs = self.session.run(["logits_per_image"], ort_inputs)
            scores.append(ort_outputs[0][0][0])

        latency = (time.time() - start_time) * 1000  # ms

        # Softmax normalization
        scores = np.array(scores)
        probs = np.exp(scores) / np.exp(scores).sum()

        # Best prediction
        best_idx = np.argmax(probs)
        best_label = self.labels[best_idx]
        best_prob = float(probs[best_idx])
        kcal = self.calories.get(best_label, "N/A")

        return {
            "image": os.path.basename(img_path),
            "label": best_label,
            "probability": best_prob,
            "kcal": kcal,
            "latency_ms": latency
        }

    def predict_folder(self, folder_path, n_samples=6):
        """Pick n_samples images from folder and predict class + kcal."""
        all_imgs = [f for f in os.listdir(folder_path)
                    if f.lower().endswith((".png", ".jpg", ".jpeg"))]

        chosen_imgs = random.sample(all_imgs, min(n_samples, len(all_imgs)))
        results = []

        for img_name in chosen_imgs:
            img_path = os.path.join(folder_path, img_name)
            pred = self.predict_image(img_path)
            results.append(pred)

        return results
