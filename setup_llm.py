from huggingface_hub import snapshot_download
snapshot_download(
    repo_id="microsoft/Phi-3-mini-4k-instruct-onnx",
    repo_type="model",
    local_dir=r".",
    allow_patterns=["directml/directml-int4-awq-block-128/*"]
)
