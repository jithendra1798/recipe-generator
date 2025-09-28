from huggingface_hub import snapshot_download
snapshot_download(
    repo_id="microsoft/Phi-3-mini-4k-instruct-onnx",
    repo_type="model",
    local_dir=r"C:\Users\qc_de\Desktop\recipe_llm\hack-princeton",
    allow_patterns=["directml/directml-int4-awq-block-128/*"]
)
