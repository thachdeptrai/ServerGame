import os

folder_path = r"E:\Download\DragonBoy-main-sale\data\map\rename"  # Thay bằng đường dẫn thư mục của bạn
start_number = 184  # Số bắt đầu

# Lấy danh sách tất cả các file trong thư mục
files_to_rename = os.listdir(folder_path)

# Đổi tên từng file
for index, filename in enumerate(files_to_rename):
    old_path = os.path.join(folder_path, filename)
    if os.path.isfile(old_path):  # Chỉ đổi tên file, bỏ qua thư mục
        new_name = str(start_number + index)  # Tạo tên mới bắt đầu từ 184
        file_extension = os.path.splitext(filename)[1]  # Lấy phần mở rộng file
        new_path = os.path.join(folder_path, f"{new_name}{file_extension}")
        os.rename(old_path, new_path)

print("Đổi tên file hoàn tất!")
