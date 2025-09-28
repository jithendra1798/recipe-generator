# Create a virtual environment in folder "venv"
python -m venv venv

# Activate the virtual environment
.\venv\Scripts\Activate.ps1

# Upgrade pip
python -m pip install --upgrade pip

# Install dependencies from requirements.txt
pip install -r requirements.txt

# Enable LLM transfer
setx HF_HUB_ENABLE_HF_TRANSFER 1

# Setup llm
python setup_ll.py

Write-Host "✅ Virtual environment setup complete. Use '.\venv\Scripts\Activate.ps1' to activate it later."
