import requests

url = "http://localhost:8000/upload-images"

# Open image files in binary mode
files = [
    ("files", ("img/test1.jpg", open("img/test1.jpg", "rb"), "image/jpg")),
    ("files", ("img/test2.jpg", open("img/test2.jpg", "rb"), "image/jpg")),
]

response = requests.post(url, files=files)

# print(response.status_code)
# print(response.json())
print(4*'\n\n')
print(18*6*'*')
print('\n\n')
ingredients = [i['label'] for i in response.json()['ingredients']]
print('Ingredients from images:', ingredients)

print('\n\n')
print(18*6*'*')
print('\n\n')

from phi3_utils import recipes_of, recipe_of_name

_MODEL_DIR = r"..\directml\directml-int4-awq-block-128"

n = 5
recipes = recipes_of(ingredients, 5)   # returns a Python list of 5 titles
for i in range(n):
    print(f"{i+1}. {recipes[i]}")


print('\n\n')
print(18*6*'*')
print('\n\n')


print('\n\n')
print(18*6*'*')
print('\n\n')
print(f'Recipe for: {recipes[0]}')
print(recipe_of_name(recipes[0]))
print('\n\n')
print(18*6*'*')
print('\n\n')
