from fastapi import FastAPI, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import uvicorn
from PIL import Image
import io
from img.ingredients import FoodClassifier

app = FastAPI()

model_dir = "./img/content"
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

# # --- Endpoint to accept multiple images ---
# @app.post("/upload-images")
# async def upload_images(files: list[UploadFile] = File(...)):
#     results = []

#     files = ['test1.jpg', 'test2.jpg', 'test3.jpg', 'test4.jpg']
#     for file in files:
#         img_path = file
#         prediction = clf.predict_image(img_path)
#         results.append(prediction)
#     return {"ingredients": results}

from pathlib import Path
import uuid

UPLOAD_DIR = Path("uploads")
UPLOAD_DIR.mkdir(exist_ok=True)

@app.post("/upload-images")
async def upload_images(files: list[UploadFile] = File(...)):
    """
    Accept multiple images from the 'files' form field, save temp, run clf, return predictions.
    Response shape: {"ingredients": [prediction, ...]}
    Each 'prediction' should include a 'label' key if your FoodClassifier provides it.
    """
    results = []
    saved_paths = []

    try:
        # Save uploads to temp files (since clf.predict_image expects a path)
        for uf in files:
            contents = await uf.read()
            suffix = Path(uf.filename).suffix or ".jpg"
            fname = f"{uuid.uuid4().hex}{suffix}"
            fpath = UPLOAD_DIR / fname
            with open(fpath, "wb") as f:
                f.write(contents)
            saved_paths.append(fpath)

        # Run predictions
        for p in saved_paths:
            pred = clf.predict_image(str(p))
            results.append(pred)

        return {"ingredients": results}

    finally:
        # Optional: cleanup temp files
        for p in saved_paths:
            try:
                p.unlink()
            except Exception:
                pass

# ---- add near your other imports ----
from pydantic import BaseModel
from typing import List, Optional
from phi3_utils import recipes_of, recipe_of_name  # your local helpers

# ---- CORS so the HTML page can call the API from the browser ----
from fastapi.middleware.cors import CORSMiddleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # or ["http://localhost:5173", ...] if you prefer strict
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ---- payload shapes ----
class IngredientsReq(BaseModel):
    ingredients: List[str]
    count: int = 5

class RecipeReq(BaseModel):
    name: str
    available_ingredients: Optional[List[str]] = None

# ---- endpoints ----
@app.post("/recipes-from-ingredients")
def recipes_from_ingredients(req: IngredientsReq):
    titles = recipes_of(req.ingredients, req.count)
    return {"recipes": titles}

@app.post("/recipe-details")
def recipe_details(req: RecipeReq):
    text = recipe_of_name(req.name, req.available_ingredients)
    return {"recipe": text}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
