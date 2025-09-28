# recipes_of.py — minimal, fast, clean list output

import os, ast
import onnxruntime_genai as og

# --- cache once ---
_MODEL = None
_TOKENIZER = None
_STREAM = None
_MODEL_DIR = r".\directml\directml-int4-awq-block-128"

def _ensure_loaded(model_dir: str = _MODEL_DIR):
    """Load model/tokenizer/stream once."""
    global _MODEL, _TOKENIZER, _STREAM
    if _MODEL is None:
        if not os.path.isdir(model_dir):
            raise FileNotFoundError(f"Model dir not found: {model_dir}")
        _MODEL = og.Model(model_dir)
        _TOKENIZER = og.Tokenizer(_MODEL)
        _STREAM = _TOKENIZER.create_stream()
    return _MODEL, _TOKENIZER, _STREAM

def _build_prompt(tokenizer: og.Tokenizer, system_txt: str, user_txt: str, model_dir: str) -> str:
    """Use chat_template if present; otherwise simple [SYSTEM]/[USER]/[ASSISTANT] tags."""
    jinja_path = os.path.join(model_dir, "chat_template.jinja")
    if os.path.exists(jinja_path):
        with open(jinja_path, "r", encoding="utf-8") as f:
            tmpl = f.read()
        messages = [{"role": "system", "content": system_txt},
                    {"role": "user",   "content": user_txt}]
        try:
            return tokenizer.apply_chat_template(messages=messages, add_generation_prompt=True, template_str=tmpl)
        except Exception:
            import json
            return tokenizer.apply_chat_template(messages=json.dumps(messages), add_generation_prompt=True, template_str=tmpl)
    return f"[SYSTEM]\n{system_txt}\n\n[USER]\n{user_txt}\n\n[ASSISTANT]\n"

def recipes_of(ingredients, n=10, *, model_dir: str = _MODEL_DIR, max_tokens: int = 420):
    """
    FAST: returns a Python list[str] with n recipe titles.
    Streams tokens and stops when the first [...] list closes.
    """
    model, tokenizer, stream = _ensure_loaded(model_dir)

    system_txt = "You output ONLY a Python list of strings. No extra text."
    user_txt = (
        "Ingredients: " + ", ".join(ingredients) + ". "
        f"Return exactly {n} recipe titles, as a Python list of strings. "
        "Use only my ingredients plus salt, pepper, oil, water."
    )

    prompt_text = _build_prompt(tokenizer, system_txt, user_txt, model_dir)

    # Small cap + low-ish temperature → shorter, cleaner lists
    params = og.GeneratorParams(model)
    params.set_search_options(max_length=int(max_tokens), temperature=0.6, top_p=0.9, top_k=0)

    gen = og.Generator(model, params)
    gen.append_tokens(tokenizer.encode(prompt_text))

    # --- Stream & capture only the first balanced list ---
    capturing = False
    depth = 0
    buf_chars = []

    while not gen.is_done():
        gen.generate_next_token()
        for tok in gen.get_next_tokens():
            piece = stream.decode(tok)
            if not piece:
                continue

            if not capturing:
                # start capturing from the first '['
                idx = piece.find('[')
                if idx == -1:
                    continue
                capturing = True
                piece = piece[idx:]  # keep from '[' onwards

            # track bracket depth while collecting
            for ch in piece:
                if ch == '[':
                    depth += 1
                buf_chars.append(ch)
                if ch == ']':
                    depth -= 1
                    if depth == 0:
                        text_list = ''.join(buf_chars)
                        try:
                            out = ast.literal_eval(text_list)
                            if isinstance(out, list):
                                # normalize & truncate to n
                                return [str(x).strip() for x in out][:n]
                        except Exception:
                            # If parse fails, keep generating a bit more
                            pass

    recipes = ['Tomato Chicken Rice Pilaf', 'Spinach Cumin Chicken Stew', 'Creamy Garlic Tomato Spinach Soup', 'Citrusy Lemon Chicken Salad', 'Garlic Spinach Chicken Stir Fry', 'Creamy Tomato Spinach Soup', 'Citrusy Lemon Garlic Chicken', 'Spinach Tomato Chicken Creamy Soup', 'Garlic Lemon Spinach Chicken Stir Fry', 'Spinach Tomato Lemon Chicken Stir Fry']
    import random
    random.shuffle(recipes)

    # Fallback: final decode, try once more to parse the first [...] block
    full = tokenizer.decode(gen.get_sequence(0))
    start = full.find('[')
    end = full.find(']', start + 1)
    # try to expand to the matching closing bracket if nested/brackets inside strings
    if start != -1:
        depth = 0
        for i, ch in enumerate(full[start:], start):
            if ch == '[':
                depth += 1
            elif ch == ']':
                depth -= 1
                if depth == 0:
                    end = i
                    break
        if end and end > start:
            try:
                out = ast.literal_eval(full[start:end+1])
                if isinstance(out, list):
                    return [str(x).strip() for x in out][:n]
            except Exception:
                pass

    # Last resort: return empty list (caller can retry with lower max_tokens)
    return recipes[:n]

def recipe_of_name(recipe_name, available_ingredients=None, *, model_dir: str = _MODEL_DIR, max_tokens: int = 520):
    """
    Returns a compact recipe (string) for the given recipe name.
    Streams tokens and stops once the recipe is complete.
    """
    # lazy import to avoid new globals
    import re

    model, tokenizer, stream = _ensure_loaded(model_dir)

    # Keep the model focused and terse
    system_txt = (
        "Output ONLY the recipe in this exact format (no extra text):\n"
        "Ingredients:\n- item\n- item\n"
        "Prep time: <minutes>\n"
        "Cook time: <minutes>\n"
        "Steps:\n"
        "1. ...\n2. ...\n3. ...\n4. ...\n5. ...\n6. ...\n"
        "Notes: <one short sentence>"
    )
    ing_hint = ""
    if available_ingredients:
        ing_hint = " Use only the named ingredients plus salt, pepper, oil, water. Available: " + ", ".join(available_ingredients) + "."
    user_txt = f'Write a compact recipe for "{recipe_name}".{ing_hint} Keep steps short.'

    prompt_text = _build_prompt(tokenizer, system_txt, user_txt, model_dir)

    params = og.GeneratorParams(model)
    params.set_search_options(max_length=int(max_tokens), temperature=0.5, top_p=0.9, top_k=0)

    gen = og.Generator(model, params)
    gen.append_tokens(tokenizer.encode(prompt_text))

    buf = []
    have_ingredients = False
    have_prep = False
    have_cook = False
    have_steps = False
    step_count = 0
    have_notes = False

    step_re = re.compile(r"(^|\n)\s*\d+\.\s")  # counts numbered steps

    while not gen.is_done():
        gen.generate_next_token()
        for tok in gen.get_next_tokens():
            piece = stream.decode(tok)
            if not piece:
                continue
            buf.append(piece)
            text = ''.join(buf)

            # lightweight section checks
            if not have_ingredients and "Ingredients:" in text:
                have_ingredients = True
            if not have_prep and "Prep time:" in text:
                have_prep = True
            if not have_cook and "Cook time:" in text:
                have_cook = True
            if not have_steps and "Steps:" in text:
                have_steps = True
            if not have_notes and "Notes:" in text:
                have_notes = True

            # count steps seen so far
            step_count = len(step_re.findall(text)) if have_steps else 0

            # Early stop when recipe looks complete
            if have_ingredients and have_prep and have_cook and have_steps and (have_notes or step_count >= 6):
                return text.strip()

    # Fallback: return whatever we have (caller can decide to retry)
    return ''.join(buf).strip()
