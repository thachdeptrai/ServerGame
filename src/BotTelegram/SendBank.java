/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package BotTelegram;

import static BotTelegram.MyTelegramBot.chatidFromAdmin;
import DBConnect.NNTDB;
import NgocThachServer.NgocRongOnline;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Transaction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Logger;

/**
 *
 * @author kagam
 */
public class SendBank {

    private MyTelegramBot telegramBot = new MyTelegramBot();
    List<Transaction> transactions = getTransactionInfo(); // Lấy thông tin giao dịch từ cơ sở dữ liệu
    private static SendBank instance;

    public static SendBank gI() {
        if (instance == null) {
            instance = new SendBank();
        }
        return instance;
    }

    // Phương thức tạo các nút bấm cho menu
    private InlineKeyboardMarkup createMenuButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Tạo các hàng nút bấm
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(createMenuButtonRow("Tắt/Bật Bot", "toggle_bot"));
        rows.add(createMenuButtonRow("Tắt Server", "shutdown_server"));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public void onCallbackQuery(CallbackQuery callbackQuery) {
        int messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();

        if (callbackData.startsWith("confirm_")) {
            // Xử lý xác nhận giao dịch
            int transactionId = Integer.parseInt(callbackData.substring("confirm_".length()));
            deleteMessage(chatidFromAdmin, messageId);
            handleTransactionConfirmation(transactionId, chatidFromAdmin);
        } else if (callbackData.startsWith("cancel_")) {
            // Xử lý hủy giao dịch
            int transactionId = Integer.parseInt(callbackData.substring("cancel_".length()));
            handleTransactionCanceation(transactionId, chatidFromAdmin);
        }
    }

    // Phương thức tạo một hàng nút bấm
    private List<InlineKeyboardButton> createMenuButtonRow(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

        return row;
    }

    protected void sendMenuToUser(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId); // Gửi đến chatId riêng
        message.setText("Chọn một tùy chọn:"); // Nội dung tin nhắn

        // Tạo các nút menu
        message.setReplyMarkup(createMenuButtons());

        try {
            telegramBot.execute(message); // Gửi tin nhắn
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getTransactionInfo() {
        List<Transaction> transactions = new ArrayList<>();
        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();
        // Lọc các giao dịch được tạo trong vòng 20 giây
        long SecondsAgo = currentTimeMillis - 20000; // 20 giây trước

        String query = "SELECT * FROM transactions WHERE status_deposit = 0 AND created_at > ?";

        try (Connection connection = NNTDB.getConnectionServer(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, new Timestamp(SecondsAgo));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String playerName = resultSet.getString("player_name");
                String accountName = resultSet.getString("account_name");
                String bankname = resultSet.getString("bank_name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                int amount = resultSet.getInt("amount");
                String description = resultSet.getString("description");
                String accountNumber = resultSet.getString("account_number");
                String qrlink = resultSet.getString("qr_link");

                transactions.add(new Transaction(id, amount, qrlink, bankname, accountName, accountNumber, description, createdAt, playerName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void sendTransactionInfo(String chatId) {
        transactions = getTransactionInfo();

        StringBuilder messageText = new StringBuilder();
        for (Transaction transaction : transactions) {

            messageText.append("ID : ").append(transaction.getId())
                    .append("Tên người chơi: ").append(transaction.getPlayerName())
                    .append("\nNgân Hàng: ").append(transaction.getBankName())
                    .append("\nNgày tạo: ").append(transaction.getCreatedAt())
                    .append("\nSố tiền: ").append(transaction.getAmount())
                    .append("\nMô tả: ").append(transaction.getDescription())
                    .append("\nSố tài khoản: ").append(transaction.getAccountNumber())
                    .append("\n----------------------------\n");

            // Tạo nút bấm cho mỗi giao dịch
            InlineKeyboardMarkup inlineKeyboardMarkup = createTransactionButtons(transaction.getId());

            // Tạo tin nhắn và gửi kèm nút bấm
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(messageText.toString());
            message.setReplyMarkup(inlineKeyboardMarkup); // Gửi nút bấm

            try {
                if (message == null) {
                return;
            }
                telegramBot.execute(message);  // Gửi tin nhắn
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            // Đặt lại nội dung tin nhắn để không bị trùng khi gửi cho các giao dịch tiếp theo
            messageText.setLength(0);  // Reset nội dung tin nhắn
        }
    }

    private InlineKeyboardMarkup createTransactionButtons(int transactionId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Tạo các hàng nút bấm, bao gồm ID giao dịch trong callbackData
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(createMenuButtonRow("Xác nhận giao dịch", "confirm_" + transactionId));
        rows.add(createMenuButtonRow("Hủy giao dịch", "cancel_" + transactionId));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static boolean doneNap(int id) {
        String updateQuery = "UPDATE transactions SET status_deposit = 1 WHERE id = ?";
        try (Connection con = NNTDB.getConnectionServer(); PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.error(" Lỗi ở hàm doneNap");
            return false;
        }
    }

    // Xử lý xác nhận giao dịch
    private void handleTransactionConfirmation(int transactionId, String chatId) {
        // Gọi hàm doneNap để cập nhật trạng thái giao dịch
        boolean success = doneNap(transactionId);
        // Gửi thông báo cho người dùng về kết quả
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (success) {
            message.setText("Giao dịch " + transactionId + " đã được xác nhận thành công.");
        } else {
            message.setText("Xác nhận giao dịch " + transactionId + " không thành công. Vui lòng thử lại.");
        }

        try {
            if (message == null) {
                return;
            }
            telegramBot.execute(message); // Gửi tin nhắn thông báo cho người dùng
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        try {
            // Gửi yêu cầu xóa tin nhắn
            
            telegramBot.execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException e) {
            System.err.println("Failed to delete message: " + e.getMessage());
        }
    }

    private void handleTransactionCanceation(int transactionId, String chatId) {
        // Gửi thông báo cho người dùng về kết quả
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("đã hủy giao dịch " + transactionId + "");
        NgocRongOnline.deleteOldUnprocessedTransactions();
        try {
            if (message == null) {
                return;
            }
            telegramBot.execute(message); // Gửi tin nhắn thông báo cho người dùng
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
