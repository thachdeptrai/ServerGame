/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package BotTelegram;

import static BotTelegram.MyTelegramBot.chatid;
import DBConnect.NNTDB;
import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Base64;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author kagam
 */
public class SendKeyWeb {

    public MyTelegramBot myTelegramBot = new MyTelegramBot();

    private static SendKeyWeb instance;

    public static SendKeyWeb gI() {
        if (instance == null) {
            instance = new SendKeyWeb();
        }
        return instance;
    }

    public void generateAndSendKey(String chatId) {
        try {
            // Tạo key ngẫu nhiên 16 byte và mã hóa Base64
            SecureRandom random = new SecureRandom();
            byte[] keyBytes = new byte[16]; // 16 byte
            random.nextBytes(keyBytes);
            String encodedKey = Base64.getEncoder().encodeToString(keyBytes);

            // Lưu vào database
            saveKeyToDatabase(encodedKey);

            // Gửi key cho người dùng
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Key của bạn: " + encodedKey);
            if (message == null) {
                return;
            }
            myTelegramBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveKeyToDatabase(String key) {
        String sql = "INSERT INTO admin_keys (`key`) VALUES (?)";
        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addKey(String chatId, String key) {
        String sql = "INSERT INTO admin_keys (`key`) VALUES (?)";

        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.executeUpdate();

            myTelegramBot.sendLogMessage(chatId, "Đã thêm key: " + key);
        } catch (Exception e) {
            System.out.println("key trùng");
            myTelegramBot.sendLogMessage(chatId, "Lỗi khi thêm key.");
        }
    }

    public void showKeys(String chatId) {
        String sql = "SELECT `key` FROM admin_keys";
        StringBuilder keysList = new StringBuilder("Danh sách key:\n");
        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                keysList.append("- ").append(rs.getString("key")).append("\n");
            }
            myTelegramBot.sendLogMessage(chatId, keysList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            myTelegramBot.sendLogMessage(chatId, "Lỗi khi lấy danh sách key.");
        }
    }

    public static List<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        String query = "SELECT * FROM admin_keys"; 

        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                keys.add(rs.getString("key"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keys;
    }

    public void showKeysForRemoval(String chatId) {
        String sql = "SELECT `key` FROM admin_keys";
            List<String> keys = getAllKeys();
        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
 
                for (String key : keys) {
                    if (key == null || key.trim().isEmpty()) continue;
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(key.length() > 30 ? key.substring(0, 27) + "..." : key); // Giới hạn độ dài hiển thị
                    button.setCallbackData("deleteKey:" + key); // Sử dụng mã hash thay vì key đầy đủ
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    row.add(button);
                    rows.add(row);
            }

            if (rows.isEmpty()) {
                myTelegramBot.sendLogMessage(chatId, "Không có key nào để xóa.");
                return;
            }

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(rows);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Chọn key cần xóa:");
            message.setReplyMarkup(markup);
            if (message == null) {
                return;
            }
            myTelegramBot.execute(message);

        } catch (Exception e) {
            e.printStackTrace();
            myTelegramBot.sendLogMessage(chatId, "Lỗi khi lấy danh sách key.");
        }
    }

    public void removeKey(String chatId, String key) {
        String sql = "DELETE FROM admin_keys WHERE `key` = ?";

        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                myTelegramBot.sendLogMessage(chatId, "Đã xóa key: " + key);
            } else {
                myTelegramBot.sendLogMessage(chatId, "Key không tồn tại hoặc đã bị xóa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            myTelegramBot.sendLogMessage(chatId, "Lỗi khi xóa key.");
        }
    }

    // Lấy tên máy tính
    private String getHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            return "Không xác định";
        }
    }

// Lấy địa chỉ IP của máy tính
    private String getLocalIPAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            return "Không xác định";
        }
    }

    // Lấy vị trí của máy tính thông qua API ipinfo.io
    private String getLocation() {
        try {
            // Gửi yêu cầu đến ipinfo.io
            String ipInfoUrl = "http://ipinfo.io/json";
            URL url = new URL(ipInfoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Đọc phản hồi từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Chuyển đổi phản hồi thành JSONObject
            JSONObject myResponse = new JSONObject(response.toString());

            // Lấy thông tin vị trí từ JSON
            String city = myResponse.optString("city", "Không xác định");
            String region = myResponse.optString("region", "Không xác định");
            String country = myResponse.optString("country", "Không xác định");

            return city + ", " + region + ", " + country;

        } catch (Exception e) {
            e.printStackTrace();
            return "Không thể lấy vị trí";
        }
    }

    public void sendStartupMessage() {
        // Lấy thông tin hệ thống
        StringBuilder systemInfo = new StringBuilder();

        // Tên máy tính
        String hostname = getHostName();
        systemInfo.append("Tên máy: ").append(hostname).append("\n");

        // Phiên bản hệ điều hành
        String os = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        systemInfo.append("Hệ điều hành: ").append(os).append(" ").append(osVersion).append("\n");

        // Phiên bản Java
        String javaVersion = System.getProperty("java.version");
        systemInfo.append("Phiên bản Java: ").append(javaVersion).append("\n");

        // Thông tin bộ xử lý
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        systemInfo.append("CPU: ").append(osBean.getAvailableProcessors()).append(" processors\n");

        // Bộ nhớ hệ thống
        systemInfo.append("Total Memory: ").append(osBean.getTotalPhysicalMemorySize() / (1024 * 1024 * 1024)).append(" GB\n");
        systemInfo.append("Free Memory: ").append(osBean.getFreePhysicalMemorySize() / (1024 * 1024 * 1024)).append(" GB\n");

        String ipAddress = getLocalIPAddress();
        systemInfo.append("Địa chỉ IP: ").append(ipAddress).append("\n");

        String location = getLocation();
        systemInfo.append("Vị trí: ").append(location).append("\n");

        String chatId = chatid;  // Địa chỉ chat ID nơi bạn muốn gửi tin nhắn
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Đã khởi động server !\n"
                + "Thông tin máy tính: \n"
                + systemInfo.toString() + "\n"
                + "Chào mừng bạn đến với bot quản lý server! \nBạn có thể sử dụng các lệnh như:\n"
                + "/status - Kiểm tra trạng thái server\n"
                + "/load_top - Tải top người chơi\n"
                + "/load_giftcode - Tải giftcode\n"
                + "/backup_sql - Sao lưu SQL\n"
                + "/load_map - Tải map\n"
                + "/backup_src - Sao lưu mã nguồn\n"
                + "/Restart - Thực hiện Khởi động lại server\n"
                + "/tatsv - tắt sv trong 60s và k khởi động lại\n"
                + "/getkey - randomkey trên web\n"
                + "/addkey - add trên web\n"
                + "/showkey - xem trên web\n"
                + "/removekey - xóa key trên web\n"
                + "/thongtin - thông tin\n");
        try {
            if (message == null) {
                return;
            }
            myTelegramBot.execute(message);  // Gửi tin nhắn
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
