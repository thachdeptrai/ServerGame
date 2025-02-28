package Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DBConnect.NNTDB;
import JDBCMysql.PlayerDAO;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import models.Transaction;
import lombok.Getter;
import models.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import player.Player;
import services.ItemService;
import services.Service;

/**
 * Quản lý các giao dịch của người chơi.
 */
public class TransactionManager {

    @Getter
    private List<Transaction> transactionList = new ArrayList<>();
    private static final TransactionManager INSTANCE = new TransactionManager();

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    // Tải danh sách giao dịch của tất cả người chơi
    public void load() {
        transactionList.clear();
        try (Connection con = NNTDB.getConnectionServer(); PreparedStatement ps = con.prepareStatement("SELECT t.*, p.head, p.items_body\n"
                + "FROM transactions t\n"
                + "JOIN player p ON t.player_name = p.name\n"
                + "ORDER BY t.created_at DESC\n"
                + "LIMIT 100;"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = processTransactionResultSet(rs);

                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xử lý kết quả trả về từ truy vấn SQL
    private Transaction processTransactionResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("t.id"));
        transaction.head = rs.getShort("p.head");
        transaction.name = (rs.getString("t.player_name"));
        transaction.setAmount(rs.getInt("t.amount"));
        transaction.setBankName(rs.getString("t.bank_name"));
        transaction.setAccountName(rs.getString("t.account_name"));
        transaction.setAccountNumber(rs.getString("t.account_number"));
        transaction.setDescription(rs.getString("t.description"));
        transaction.setQrLink(rs.getString("t.qr_link"));
        transaction.setStatusReceive(rs.getBoolean("t.status_receive"));
        transaction.setStatusDeposit(rs.getBoolean("t.status_deposit"));
        transaction.setCreatedAt(rs.getTimestamp("t.created_at"));
        extractItemsBody(rs.getString("p.items_body"), transaction);
        return transaction;
    }

    private void extractItemsBody(String itemsBody, Player player) {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = (JSONArray) jv.parse(itemsBody);

        for (Object itemDataObject : dataArray) {
            Item item = createItemFromDataObject(itemDataObject.toString());
            player.inventory.itemsBody.add(item);
        }

        dataArray.clear();
    }

    private Item createItemFromDataObject(String itemData) {
        JSONValue jv = new JSONValue();
        JSONArray dataObject = (JSONArray) jv.parse(itemData);
        short tempId = Short.parseShort(String.valueOf(dataObject.get(0)));
        Item item;
        if (tempId != -1) {
            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get(1))));
            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get(2)).replaceAll("\"", ""));

            for (Object option : options) {
                JSONArray opt = (JSONArray) jv.parse(String.valueOf(option));
                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                        Integer.parseInt(String.valueOf(opt.get(1)))));
            }
            item.createTime = Long.parseLong(String.valueOf(dataObject.get(3)));
            if (ItemService.gI().isOutOfDateTime(item)) {
                item = ItemService.gI().createItemNull();
            }
        } else {
            item = ItemService.gI().createItemNull();
        }

        return item;
    }

    public void deleteOldTransactions() {
        // Kết nối đến cơ sở dữ liệu và thực hiện xóa
        try (Connection con = NNTDB.getConnectionServer()) {
            // Câu truy vấn SQL để xóa các giao dịch cũ hơn 7 ngày
            String sql = "DELETE FROM transactions WHERE created_at <= NOW() - INTERVAL 3 DAY";

            // Thực thi câu lệnh xóa
            try (PreparedStatement psDelete = con.prepareStatement(sql)) {
                // Thực hiện câu lệnh và lấy số lượng bản ghi bị xóa
                int rowsAffected = psDelete.executeUpdate();
                System.out.println("Đã xóa " + rowsAffected + " giao dịch cũ hơn 3 ngày.");
            }
        } catch (SQLException e) {
            // In ra lỗi nếu có
            System.err.println("Lỗi khi xóa giao dịch: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
