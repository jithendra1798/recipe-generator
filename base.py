from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
import shutil
import os
from datetime import datetime

app = FastAPI()

# Folder to save uploaded photos
UPLOAD_FOLDER = "uploaded_photos"
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

@app.post("/upload-photo/")
async def upload_photo(file: UploadFile = File(...)):
    try:
        # Generate a unique filename
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        file_extension = os.path.splitext(file.filename)[1]
        saved_filename = f"{timestamp}_{file.filename}"
        file_path = os.path.join(UPLOAD_FOLDER, saved_filename)

        # Save the file locally
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

        return JSONResponse(
            status_code=200,
            content={"message": "Photo uploaded successfully", "filename": saved_filename}
        )
    except Exception as e:
        return JSONResponse(
            status_code=500,
            content={"message": f"Failed to upload photo: {str(e)}"}
        )

# Run using: uvicorn filename:app --reload
