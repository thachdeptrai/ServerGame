package Npc_List;

import DBConnect.NNTDB;
import Manager.EventManager;
import NgocThachServer.Manager;
import NgocThachServer.NgocRongOnline;
import npc.Npc;
import player.Player;

/**
 *
 * @author kagam
 */
import java.sql.*;
import models.Item;
import services.InventoryService;
import services.NpcService;
import services.Service;
import utils.Logger;

public class CayThongNoel extends Npc {

    private static final int ITEM_CHUONG = 1459; // ID của Chuông
    private static final int ITEM_QUA_CHAU = 1460; // ID của Quả Châu
    private static final int ITEM_NGOI_SAO = 1461; // ID của Ngôi Sao
    private static final int ITEM_DAY_KIM_TUYEN = 1462; // ID của Dây Kim Tuyến
    private static final int ITEM_MOC_TREO = 1463; // ID của Móc Treo

    private static final int REQ_CHUONG = 30;
    private static final int REQ_QUA_CHAU = 30;
    private static final int REQ_NGOI_SAO = 30;
    private static final int REQ_DAY_KIM_TUYEN = 2;
    private static final int REQ_MOC_TREO = 1;

    private static final String LOAD_QUERY = "SELECT total_decorators, last_exp_boost_time, exp_rate FROM christmas_tree WHERE id = 1";
    private static int currentExpRate = 1; // Hệ số EXP mặc định
    private static final String UPDATE_QUERY = "UPDATE christmas_tree SET total_decorators = ?, last_exp_boost_time = ?, exp_rate = ? WHERE id = 1";
    private static int totalDecorators = 0;
    private static long lastExpBoostTime = 0;

    public CayThongNoel(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
        loadTotalDecorators(); // Tải dữ liệu từ SQL khi NPC được khởi tạo
    }

    @Override
    public void openBaseMenu(Player player) {
        if (EventManager.CHRISTMAS == true) {
            switch (this.map.mapId) {
                case 14, 0, 7 -> {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - lastExpBoostTime;
                    String expStatus = "Hiện tại không có tăng EXP nào.";

                    // Kiểm tra trạng thái EXP hiện tại
                    if (currentExpRate == 2 && elapsedTime < 12 * 60 * 60 * 1000) {
                        long remainingTime = 12 * 60 * 60 * 1000 - elapsedTime;
                        expStatus = "x2 EXP còn lại: " + formatTime(remainingTime);
                    } else if (currentExpRate == 3 && elapsedTime < 24 * 60 * 60 * 1000) {
                        long remainingTime = 24 * 60 * 60 * 1000 - elapsedTime;
                        expStatus = "x3 EXP còn lại: " + formatTime(remainingTime);
                    } else if (currentExpRate == 4 && elapsedTime < 72 * 60 * 60 * 1000) {
                        long remainingTime = 72 * 60 * 60 * 1000 - elapsedTime;
                        expStatus = "x4 EXP còn lại: " + formatTime(remainingTime);
                    }
                    String message = "Hiện tại có " + totalDecorators + " Lượt trang trí.\n"
                            + "Đạt 1000 người: x2 EXP trong 12 giờ.\n"
                            + "Đạt 2000 người: x3 EXP trong 24 giờ.\n"
                            + "Đạt 10000 người: x4 EXP trong 72 giờ.\n"
                            + expStatus;
                    this.createOtherMenu(player, 10, message, "Trang trí", "Thoát");
                }
                default ->
                    this.createOtherMenu(player, 0, "tôi có thể giúp gì cho bạn", "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (EventManager.CHRISTMAS == true) {
            // Lấy số lượng từng vật phẩm hiện có
            Item chuongItem = InventoryService.gI().findItemBag(player, ITEM_CHUONG);
            Item quaChauItem = InventoryService.gI().findItemBag(player, ITEM_QUA_CHAU);
            Item ngoiSaoItem = InventoryService.gI().findItemBag(player, ITEM_NGOI_SAO);
            Item dayKimTuyenItem = InventoryService.gI().findItemBag(player, ITEM_DAY_KIM_TUYEN);
            Item mocTreoItem = InventoryService.gI().findItemBag(player, ITEM_MOC_TREO);

            // Số lượng vật phẩm có trong hành trang
            int chuongCount = chuongItem != null ? chuongItem.quantity : 0;
            int quaChauCount = quaChauItem != null ? quaChauItem.quantity : 0;
            int ngoiSaoCount = ngoiSaoItem != null ? ngoiSaoItem.quantity : 0;
            int dayKimTuyenCount = dayKimTuyenItem != null ? dayKimTuyenItem.quantity : 0;
            int mocTreoCount = mocTreoItem != null ? mocTreoItem.quantity : 0;

            if (player.iDMark.getIndexMenu() == 10) {
                if (select == 0) { // Người chơi chọn "Trang trí"
                    StringBuilder missingItems = new StringBuilder("Bạn không đủ vật phẩm trang trí:\n");

                    // Hiển thị số lượng vật phẩm trong hành trang so với yêu cầu
                    missingItems.append("|3|- Chuông: ").append(chuongCount).append("/").append(REQ_CHUONG).append("\n");
                    missingItems.append("|3|- Quả Châu: ").append(quaChauCount).append("/").append(REQ_QUA_CHAU).append("\n");
                    missingItems.append("|3|- Ngôi Sao: ").append(ngoiSaoCount).append("/").append(REQ_NGOI_SAO).append("\n");
                    missingItems.append("|3|- Dây Kim Tuyến: ").append(dayKimTuyenCount).append("/").append(REQ_DAY_KIM_TUYEN).append("\n");
                    missingItems.append("|3|- Móc Treo: ").append(mocTreoCount).append("/").append(REQ_MOC_TREO).append("\n");

                    // Kiểm tra nếu người chơi có đủ vật phẩm
                    if (!hasEnoughDecorItems(player)) {
                        this.createOtherMenu(player, 100, missingItems.toString(), "Thoát");
                        return;
                    }
                    // Nếu đủ vật phẩm, hiển thị menu trang trí
                    this.createOtherMenu(player, 11, missingItems.toString(), "Trang Trí", "Thoát");

                }
            }

            if (player.iDMark.getIndexMenu() == 11) {
                // Trừ vật phẩm sau khi kiểm tra
                deductDecorItems(player);
                // Tăng số lượng trang trí
                totalDecorators++;
                saveTotalDecorators(); // Lưu lên SQL

                String message = "Cảm ơn bạn đã trang trí cây thông!\n"
                        + "Tổng số lượt trang trí: " + totalDecorators;
                if (totalDecorators == 1000) {
                    startExpBoost((byte) 2, 12 * 60 * 60 * 1000); // X2 EXP trong 12 giờ
                    message += "\nĐã kích hoạt x2 EXP trong 12 giờ!";
                } else if (totalDecorators == 2000) {
                    startExpBoost((byte) 3, 24 * 60 * 60 * 1000); // X3 EXP trong 24 giờ
                    message += "\nĐã kích hoạt x3 EXP trong 24 giờ!";
                } else if (totalDecorators == 10000) {
                    startExpBoost((byte) 4, 72 * 60 * 60 * 1000); // X4 EXP trong 72 giờ
                    message += "\nĐã kích hoạt x4 EXP trong 72 giờ!";
                }
                createOtherMenu(player, 99, message, "Thoát");
            }
        }
    }

    private void startExpBoost(byte multiplier, long duration) {
        // Lưu thời gian bắt đầu
        lastExpBoostTime = System.currentTimeMillis();
        saveTotalDecorators(); // Cập nhật SQL với thời gian mới
        currentExpRate = multiplier;
        // Thiết lập hệ số EXP
        Manager.RATE_EXP_SERVER = (byte)currentExpRate;

        // Đặt hẹn giờ để reset lại EXP sau khi hết thời gian boost
        new Thread(() -> {
            try {
                Thread.sleep(duration); // Chờ thời gian tăng EXP (duration)
                Manager.RATE_EXP_SERVER = 1; // Reset EXP về mặc định
                currentExpRate = 1;
                saveTotalDecorators();
                Logger.error("EXP boost đã kết thúc, hệ số EXP trở lại 1x.\n");
            } catch (InterruptedException e) {
                Logger.logException(CayThongNoel.class, e);
            }
        }).start();

        Logger.error("Đã kích hoạt x" + multiplier + " EXP trong " + (duration / (60 * 60 * 1000)) + " giờ!\n");
    }

    private void saveTotalDecorators() {
        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {
            stmt.setInt(1, totalDecorators);
            stmt.setLong(2, lastExpBoostTime);
            stmt.setInt(3, currentExpRate); // Lưu hệ số EXP
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.logException(CayThongNoel.class, e);
        }
    }

    private void loadTotalDecorators() {
        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement stmt = conn.prepareStatement(LOAD_QUERY); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalDecorators = rs.getInt("total_decorators");
                lastExpBoostTime = rs.getLong("last_exp_boost_time");
                currentExpRate = rs.getInt("exp_rate"); // Lấy hệ số EXP từ SQL
            }
        } catch (SQLException e) {
            Logger.logException(CayThongNoel.class, e);
        }
    }

    private boolean hasEnoughDecorItems(Player player) {
        Item chuongItem = InventoryService.gI().findItemBag(player, ITEM_CHUONG);
        Item quaChauItem = InventoryService.gI().findItemBag(player, ITEM_QUA_CHAU);
        Item ngoiSaoItem = InventoryService.gI().findItemBag(player, ITEM_NGOI_SAO);
        Item dayKimTuyenItem = InventoryService.gI().findItemBag(player, ITEM_DAY_KIM_TUYEN);
        Item mocTreoItem = InventoryService.gI().findItemBag(player, ITEM_MOC_TREO);
        // Kiểm tra số lượng từng vật phẩm (nếu không tồn tại thì coi như số lượng = 0)
        int chuongCount = chuongItem != null ? chuongItem.quantity : 0;
        int quaChauCount = quaChauItem != null ? quaChauItem.quantity : 0;
        int ngoiSaoCount = ngoiSaoItem != null ? ngoiSaoItem.quantity : 0;
        int dayKimTuyenCount = dayKimTuyenItem != null ? dayKimTuyenItem.quantity : 0;
        int mocTreoCount = mocTreoItem != null ? mocTreoItem.quantity : 0;
        return chuongCount >= REQ_CHUONG
                && quaChauCount >= REQ_QUA_CHAU
                && ngoiSaoCount >= REQ_NGOI_SAO
                && dayKimTuyenCount >= REQ_DAY_KIM_TUYEN
                && mocTreoCount >= REQ_MOC_TREO;
    }

    private void deductDecorItems(Player player) {
        Item chuongItem = InventoryService.gI().findItemBag(player, ITEM_CHUONG);
        Item quaChauItem = InventoryService.gI().findItemBag(player, ITEM_QUA_CHAU);
        Item ngoiSaoItem = InventoryService.gI().findItemBag(player, ITEM_NGOI_SAO);
        Item dayKimTuyenItem = InventoryService.gI().findItemBag(player, ITEM_DAY_KIM_TUYEN);
        Item mocTreoItem = InventoryService.gI().findItemBag(player, ITEM_MOC_TREO);

        InventoryService.gI().subQuantityItemsBag(player, chuongItem, REQ_CHUONG);
        InventoryService.gI().subQuantityItemsBag(player, quaChauItem, REQ_QUA_CHAU);
        InventoryService.gI().subQuantityItemsBag(player, ngoiSaoItem, REQ_NGOI_SAO);
        InventoryService.gI().subQuantityItemsBag(player, dayKimTuyenItem, REQ_DAY_KIM_TUYEN);
        InventoryService.gI().subQuantityItemsBag(player, mocTreoItem, REQ_MOC_TREO);
        Service.gI().sendMoney(player);
        InventoryService.gI().sendItemBag(player);
    }


    private String formatTime(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60));
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
