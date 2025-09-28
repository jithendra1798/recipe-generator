import requests

url = "http://localhost:8000/upload-images"

# Open image files in binary mode
files = [
    ("files", ("test1.jpg", open("test.jpg", "rb"), "image/jpeg")),
    ("files", ("test2.jpg", open("test.jpg", "rb"), "image/jpg")),
]

response = requests.post(url, files=files)

print(response.status_code)
print(response.json())