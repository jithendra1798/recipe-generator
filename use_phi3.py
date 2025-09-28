from phi3_utils import recipes_of


import re, ast
from typing import List, Union


import time

start = time.time()

ings = ["rice","tomato","onion","spinach","chicken","yogurt","garlic","lemon","cumin"]
titles = recipes_of(ings, 12)   # returns a Python list of 5 titles
print(titles, type(titles), len(titles))


end = time.time()
print(f"Runtime: {end - start:.2f} seconds")

