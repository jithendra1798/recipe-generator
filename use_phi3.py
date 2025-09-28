from phi3_utils import recipes_of, recipe_of_name

_MODEL_DIR = r".\directml\directml-int4-awq-block-128"

def _debug_model_dir(model_dir=_MODEL_DIR):
    import os, glob
    p = os.path.abspath(model_dir)
    print("Model dir:", p)
    for f in sorted(glob.glob(os.path.join(p, "*"))):
        try:
            print(os.path.basename(f), os.path.getsize(f), "bytes")
        except OSError as e:
            print(os.path.basename(f), "ERR:", e)
# _debug_model_dir(model_dir=_MODEL_DIR)

import re, ast
from typing import List, Union


import time

start = time.time()

ings = ["rice","tomato","onion","spinach","chicken","yogurt","garlic","lemon","cumin"]
titles = recipes_of(ings, 12)   # returns a Python list of 5 titles
print(titles, type(titles), len(titles))

print(recipe_of_name("Lemon Garlic Chicken Rice Bowl"))


end = time.time()
print(f"Runtime: {end - start:.2f} seconds")

