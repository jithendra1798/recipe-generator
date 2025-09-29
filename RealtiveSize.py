import cv2
import torch
import torchvision.transforms as T
from PIL import Image
import numpy as np

# ----------------------------
# 1. Load MiDaS Model
# ----------------------------
midas = torch.hub.load("intel-isl/MiDaS", "DPT_Large")
midas.eval()

midas_transforms = torch.hub.load("intel-isl/MiDaS", "transforms")
transform = midas_transforms.dpt_transform

# ----------------------------
# 2. Load Image
# ----------------------------
img_path = "food.jpg"
img = Image.open(img_path)
img_cv = cv2.imread(img_path)
input_batch = transform(img).unsqueeze(0)

# ----------------------------
# 3. Predict Depth Map
# ----------------------------
with torch.no_grad():
    prediction = midas(input_batch)

depth = torch.nn.functional.interpolate(
    prediction.unsqueeze(1),
    size=img.size[::-1],
    mode="bicubic",
    align_corners=False,
).squeeze().cpu().numpy()

# Normalize for visualization
depth_norm = cv2.normalize(depth, None, 0, 255, cv2.NORM_MINMAX)
depth_colormap = cv2.applyColorMap(depth_norm.astype(np.uint8), cv2.COLORMAP_MAGMA)
cv2.imwrite("depth_map.jpg", depth_colormap)
print("Depth map saved as depth_map.jpg")

# ----------------------------
# 4. Let User Select Multiple Objects
# ----------------------------
rois = cv2.selectROIs("Select Objects", img_cv)
cv2.destroyAllWindows()

# ----------------------------
# 5. Compute Relative Sizes
# ----------------------------
sizes = []
for i, roi in enumerate(rois):
    x, y, w, h = roi
    roi_depth = depth[y:y+h, x:x+w]
    roi_area = w * h
    avg_depth = np.mean(roi_depth)

    # Relative size proxy = area adjusted by depth
    relative_size = roi_area / avg_depth
    sizes.append((i+1, relative_size, roi))  # Store ROI for visualization

# ----------------------------
# 6. Draw Bounding Boxes & Labels
# ----------------------------
output_img = img_cv.copy()
for obj_id, rel_size, roi in sizes:
    x, y, w, h = roi
    cv2.rectangle(output_img, (x, y), (x+w, y+h), (0, 255, 0), 2)
    label = f"Obj {obj_id}: {rel_size:.2f}"
    cv2.putText(output_img, label, (x, y-10),
                cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)

# Optional: Compare objects ratios and show in console
print("\nRelative Size Results:")
for obj_id, rel_size, _ in sizes:
    print(f"Object {obj_id}: {rel_size:.2f}")

print("\nComparisons:")
for i in range(len(sizes)):
    for j in range(i+1, len(sizes)):
        ratio = sizes[i][1] / sizes[j][1]
        print(f"Object {sizes[i][0]} is {ratio:.2f}x the size of Object {sizes[j][0]}")

# Save annotated image
cv2.imwrite("food_relative_sizes.jpg", output_img)
print("Annotated image saved as food_relative_sizes.jpg")
