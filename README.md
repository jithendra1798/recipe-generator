# hack-princeton
Qualcomm Hackathon at the Princeton University 

# Setup Instructions for Recipe Generator

PowerShell: 
1. `.\setup_env.ps1`
2. `uvicorn app:app --host 0.0.0.0 --port 8000`
3. [Run server](http://localhost:8000) - Open in new tab


### 1) Install and enable LFS (one-time per machine)
1. `winget install GitHub.GitLFS`
2. `git lfs install`

### 2) Clone as usual; LFS files download automatically at checkout
1. `git clone https://github.com/<user>/<repo>.git`
2. `cd <repo>`

### 3) Ensure LFS is enabled in this repo
1. `git lfs install`

### 4) Pull normal commits
1. `git pull origin main`  

# 5) Pull the associated LFS objects (if they didn’t auto-download)
git lfs pull
