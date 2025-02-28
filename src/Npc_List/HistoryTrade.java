package Npc_List;

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import JDBCMysql.HistoryTransactionDAO;
import java.util.List;

/**
 *
 * @author kagam
 */
public class HistoryTrade extends Npc {

    private static final int ITEMS_PER_PAGE = 5; // Số lượng lịch sử giao dịch mỗi trang
    private int currentPage = 0; // Trang hiện tại

    public HistoryTrade(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (this.mapId == 5) {
            // Mở menu cơ bản cho người chơi khi tương tác với NPC
            createOtherMenu(player, ConstNpc.BASE_MENU, "Xem lịch sử giao dịch", "Xem", "Đóng");
        } else {
            this.npcChat(player, "Không hợp lệ.");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (this.mapId != 5) {
            this.npcChat(player, "Không hợp lệ.");
            return;
        }

        List<String> histories = HistoryTransactionDAO.getHistoryTransactions();
        switch (player.iDMark.getIndexMenu()) {
            case ConstNpc.BASE_MENU:
                switch (select) {
                    case 0: // Người chơi chọn "Xem"
                        if (histories.isEmpty()) {
                            this.npcChat(player, "Không có lịch sử giao dịch nào để hiển thị.");
                        } else {
                            currentPage = 0; // Hiển thị trang đầu tiên
                            showPage(player, histories, currentPage);
                        }
                        break;

                    case 1: // Người chơi chọn "Đóng"
                        break;

                    default:
                        this.npcChat(player, "Lựa chọn không hợp lệ.");
                        break;
                }
                break;

            case ConstNpc.HISTORYTRADE:
                switch (select) {
                    case 0: // Người chơi chọn "Trở về"
                        if (currentPage > 0) {
                            currentPage--; // Quay về trang trước
                            showPage(player, histories, currentPage);
                        } else {
                            this.npcChat(player, "Đây là trang đầu tiên rồi.");
                        }
                        break;

                    case 1: // Người chơi chọn "Trang sau"
                        if ((currentPage + 1) * ITEMS_PER_PAGE < histories.size()) {
                            currentPage++; // Chuyển đến trang tiếp theo
                            showPage(player, histories, currentPage);
                        } else {
                            this.npcChat(player, "Đây là trang cuối cùng rồi.");
                        }
                        break;

                    case 2: // Người chơi chọn "Đóng"
                        break;

                    default:
                        this.npcChat(player, "Lựa chọn không hợp lệ.");
                        break;
                }
                break;

            default:
                this.npcChat(player, "Lựa chọn không hợp lệ.");
                break;
        }
    }

    // Hàm hiển thị nội dung theo trang
    private void showPage(Player player, List<String> histories, int page) {
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, histories.size());

        StringBuilder content = new StringBuilder("Lịch sử giao dịch (Trang ")
                .append(page + 1)
                .append("):\n");
        for (int i = start; i < end; i++) {
            String history = histories.get(i).replaceAll("\\(.*?\\)", "").trim(); // Loại bỏ phần trong ngoặc đơn
            content.append(history).append("\n\n");
        }

        // Hiển thị menu với các tùy chọn
        createOtherMenu(player, ConstNpc.HISTORYTRADE, content.toString(), "Trở về", "Trang sau", "Đóng");
    }
}
