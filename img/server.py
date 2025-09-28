from fastapi import FastAPI, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import uvicorn
from PIL import Image
import io
from ingredients import FoodClassifier

app = FastAPI()

model_dir = "./content"
clf = FoodClassifier(model_dir, providers=["CoreMLExecutionProvider", "CPUExecutionProvider"])

# Enable CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# --- Endpoint to accept names (your original one) ---
class NamesRequest(BaseModel):
    names: list[str]

@app.post("/process-names")
async def process_names(request: NamesRequest):
    names = request.names
    result = {
        "count": len(names),
        "lengths": {name: len(name) for name in names},
        "uppercased": [name.upper() for name in names]
    }
    return result

# --- Endpoint to accept multiple images ---
@app.post("/upload-images")
async def upload_images(files: list[UploadFile] = File(...)):
    results = []

    for file in files:
        contents = await file.read()
        img_path = "./test1.jpg"   # change this to your actual image file path
        prediction = clf.predict_image(img_path)

        try:
            image = Image.open(io.BytesIO(contents))
            width, height = image.size
            mode = image.mode
            results.append({
                "filename": file.filename,
                "content_type": file.content_type,
                "width": width,
                "height": height,
                "mode": mode
            })
        except Exception:
            results.append({
                "filename": file.filename,
                "error": "Not a valid image"
            })

    return {"images": results}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
