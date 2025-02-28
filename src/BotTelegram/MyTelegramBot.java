package BotTelegram;

import DBConnect.NNTDB;
import NgocThachServer.Client;
import NgocThachServer.Maintenance;
import NgocThachServer.Manager;
import java.io.*;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.*;
import java.net.*;
import org.json.JSONObject;
import java.util.*;
import NgocThachServer.NgocRongOnline;
import NgocThachServer.ServerManagerUI;
import java.util.concurrent.TimeUnit;
import models.Transaction;
import network.SessionManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import utils.Logger;

public class MyTelegramBot extends TelegramLongPollingBot {

    private static MyTelegramBot instance;

    public static MyTelegramBot gI() {
        if (instance == null) {
            instance = new MyTelegramBot();
        }
        return instance;
    }
    private final ServerManagerUI serverManagerUI = ServerManagerUI.gI();

    // Đặt tên người dùng mà bot sẽ phản hồi
    public static final String ALLOWED_USERNAME = "tomihjhj";
    private static boolean isBotActive = true;  // Biến điều khiển trạng thái bot
    public static final String chatid = "-1002285312797";
    public static final String chatidFromAdmin = "1325717522";
    public static final String chatidFromAdmin2 = "1325717522";
    public static int threadmap = 0;

    @Override
    public String getBotUsername() {
        return "Nro Meo";
    }

    @Override
    public String getBotToken() {
        String encodedToken = "Nzk3MTI4MTY4MjpBQUhlcFZIOTVVV3AyTDF4Sk5iaDlYNTZtcktxOU9Icnplbw==";
        return Base64TokenUtil.decodeToken(encodedToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                SendBank.gI().sendMenuToUser(chatidFromAdmin);
            }
            if (messageText.equals("/next")) {
                String adminChatId = chatidFromAdmin2;
                SendBank.gI().sendMenuToUser(adminChatId);
            }
        }
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData(); // Lấy dữ liệu của nút bấm
            String chatId = chatid;
//            System.out.println("Received callback query: " + callbackQuery.getData());  // Log callback data
            SendBank.gI().onCallbackQuery(callbackQuery);
            if (callbackData.startsWith("deleteKey:")) {
                String keyToRemove = callbackData.replace("deleteKey:", "");
                SendKeyWeb.gI().removeKey(chatidFromAdmin, keyToRemove); // Xóa key khỏi database
            }
            // Xử lý khi người dùng chọn "Tắt/Bật Bot"
            if (data.equals("toggle_bot")) {
                isBotActive = !isBotActive; // Đổi trạng thái của bot
                String status = isBotActive ? "Bật" : "Tắt";

                // Gửi thông báo cho người dùng về trạng thái mới của bot
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Bot hiện tại đã " + status);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            // Xử lý các lệnh khác tại đây...
        }
        if (!isBotActive) {
            return; // Dừng xử lý nếu bot bị tắt
        }
        if (isBotActive && update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            Message message1 = update.getMessage();
            String username = message1.getFrom().getUserName();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            String[] parts = messageText.split("\\s+", 2); // Tách lệnh và tham số
//            String command = parts[0]; // Lệnh (ví dụ: "/addkey")
            String argument = parts.length > 1 ? parts[1] : ""; // Tham số đi kèm (nếu có)
            argument = argument.replaceAll("@\\S+", "").trim();
            if (messageText.startsWith("/")) {
                if (ALLOWED_USERNAME.equals(username)) {
                    String command = messageText.contains("@") ? messageText.split("@")[0] : parts[0];
//                    switch (messageText.toLowerCase()) {
                    switch (command) {
                        case "/start":
                            // Gửi thông điệp chào mừng khi người dùng gọi lệnh /start
                            message.setText("Chào mừng bạn đến với bot quản lý server! \nBạn có thể sử dụng các lệnh như:\n"
                                    + "/status - Kiểm tra trạng thái server\n"
                                    + "/load_top - Tải top người chơi\n"
                                    + "/load_giftcode - Tải giftcode\n"
                                    + "/backup_sql - Sao lưu SQL\n"
                                    + "/load_map - Tải map\n"
                                    + "/backup_src - Sao lưu mã nguồn\n"
                                    + "/Restart - Thực hiện Khởi động lại server\n"
                                    + "/tatsv - tắt sv trong 60s và k khởi động lại\n"
                                    + "/getkey - randomkey trên sql\n"
                                    + "/addkey - add trên sql\n"
                                    + "/showkey - xem trên sql\n"
                                    + "/removekey - xóa key trên sql\n"
                                    + "/thongtin - thông tin");
                            break;
                        case "/status":
                            message.setText("Server đang chạy tại port: " + NgocRongOnline.gI().PORT
                                    + "\n Thread: " + Thread.activeCount() + ""
                                    + "\nsesson: " + SessionManager.gI().getNumSession() + ""
                                    + "\nThreadMap: " + threadmap
                                    + "\nThread - ThreadMap: " + (Thread.activeCount() - threadmap)
                            );

                            break;
                        case "/load_top":
                            serverManagerUI.loadButton1.doClick();
                            message.setText("Đã load top.");
                            break;
                        case "/load_giftcode":
                            serverManagerUI.loadButton2.doClick();
                            message.setText("Đã load giftcode.");
                            break;
                        case "/backup_sql":
                            NgocRongOnline.backupDatabase();
                            message.setText("Đã thực hiện backup SQL.");
                            break;
                        case "/load_map":
                            serverManagerUI.loadButton4.doClick();
                            message.setText("Đã load map.");
                            break;
                        case "/backup_src":
                            NgocRongOnline.backupSrcFolder();
                            message.setText("Đã thực hiện backup Src.");
                            break;
                        case "/restart":
                            int secon = 15;
                            Maintenance.gI().start(secon);
                            message.setText("Đang Thực Hiện Restart sau: " + secon + " giây");
                            System.out.println("Đang Thực Hiện Restart sau: " + secon + " giây");
                            break;
                        case "/tatsv":
                            int thoigian = 60;
                            Maintenance.gI().baotri(thoigian);
                            String b = ("Đang Thực Hiện Bảo trì sau: " + thoigian + " giây");
                            message.setText(b);
                            System.out.println(b);
                            break;
                        case "/getkey":
                            SendKeyWeb.gI().generateAndSendKey(chatidFromAdmin);
                            message.setText("ok");
                            break;
                        case "/addkey":
                            if (!argument.isEmpty()) {
                                SendKeyWeb.gI().addKey(chatidFromAdmin, argument);
                            } else {
                                sendLogMessage(chatidFromAdmin, "Vui lòng nhập key: /addkey <key>");
                            }
                            message.setText("ok");
                            break;
                        case "/showkey":
                            SendKeyWeb.gI().showKeys(chatidFromAdmin);
                            message.setText("ok");
                            break;
                        case "/removekey":
                            SendKeyWeb.gI().showKeysForRemoval(chatidFromAdmin);
                            message.setText("ok");
                            break;
                        case "/thongtin":
                            message.setText("Server: " + NgocRongOnline.NAME + "\nĐang Chạy Tại Port: " + NgocRongOnline.PORT
                                    + "\nTiềm Năng hiện Tại: " + Manager.RATE_EXP_SERVER + "\n Thread Hiện Tại: " + Thread.activeCount()
                                    + "\nSố lượng người chơi hiện tại của Server: " + Client.gI().getPlayers().size()
                                    + "\n"
                                    + "\nNgoc Thach Dev ");
                            break;
                        default:
                            message.setText("Lệnh không hợp lệ. Hãy thử: /status, /load_top, /load_giftcode, /backup_sql, /load_map,"
                                    + "/thongtin,/tatsv,/restart");
                            break;
                    }
                    try {
                        if (message == null) {
                            message.setText("ok");
                            return;
                        }
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    message.setText("Bạn Không có quyền hạn gửi lệnh bot");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    // Không phản hồi nếu không phải username cho phép
                    System.out.println("Người dùng không hợp lệ: " + username);
                }
            }
        }
    }

    public void sendLogMessage(String chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        boolean success = false;
        int retries = 5;  // Số lần thử lại tối đa khi gặp lỗi

        while (!success && retries > 0) {
            try {
                // Gửi tin nhắn tới Telegram
                execute(message);
                success = true; // Nếu gửi thành công, thoát khỏi vòng lặp
//            System.out.println("Message sent to chatId: " + chatId); // In ra khi tin nhắn được gửi thành công
            } catch (TelegramApiException e) {
                if (e.getMessage().contains("429")) {
                    // Lỗi quá nhiều yêu cầu, chờ và thử lại
                    System.out.println("Rate limit reached. Retrying in 10 seconds...");
                    try {
                        TimeUnit.SECONDS.sleep(10); // Đợi 10 giây trước khi thử lại
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Xử lý lỗi khác
                    System.err.println("Failed to send message to chatId: " + chatId);
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace(); // In ra thông tin lỗi chi tiết
                    break; // Thoát khỏi vòng lặp nếu gặp lỗi khác ngoài 429
                }
            }
            retries--;
        }
    }

}
