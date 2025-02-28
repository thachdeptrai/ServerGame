import os

folder_path = r"E:\Download\DragonBoy-main-sale\data\map\item_bg_map_data"  # Thay bằng đường dẫn thư mục của bạn
expected_range = range(0, 184)  # Dãy từ 0 đến 183

# Lấy danh sách file hiện có (chỉ tên, không mở rộng)
existing_files = []
for filename in os.listdir(folder_path):
    if os.path.isfile(os.path.join(folder_path, filename)):
        try:
            # Chuyển tên file (bỏ phần mở rộng) thành số
            file_number = int(os.path.splitext(filename)[0])
            existing_files.append(file_number)
        except ValueError:
            continue  # Bỏ qua file không phải là số

# Tìm các file còn thiếu
missing_files = [num for num in expected_range if num not in existing_files]

# Hiển thị kết quả
if missing_files:
    print(f"Còn thiếu {len(missing_files)} file:")
    print(", ".join(map(str, missing_files)))
else:
    print("Không thiếu file nào!")
