package Npc_List;/*telegram @tomihjhj_bot*/

import MiniGameZ.AllinOne;
import consts.ConstNpc;/*telegram @tomihjhj_bot*/
import models.Item;/*telegram @tomihjhj_bot*/
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.Service;
import utils.Util;/*telegram @tomihjhj_bot*/

/**
 *
 * @author kagam
 */
public class AllinOneNpc extends Npc {

    public void reloadPlayerCounts() {
        totalPlayersNormar = AllinOne.gI().getPlayerNormarCount();/*telegram @tomihjhj_bot*/
        totalPlayersVIP = AllinOne.gI().getPlayerVIPCount();/*telegram @tomihjhj_bot*/
    }

    private static final int GOLD_NORMAR_AMOUNT = 10;/*telegram @tomihjhj_bot*/ // 10 
    private static final int GOLD_VIP_AMOUNT = 100;/*telegram @tomihjhj_bot*/  // 100 

    int totalPlayersNormar = 0;/*telegram @tomihjhj_bot*/
    int totalPlayersVIP = 0;/*telegram @tomihjhj_bot*/
    public AllinOneNpc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);/*telegram @tomihjhj_bot*/
    }

    @Override
    public void openBaseMenu(Player player) {
        createOtherMenu(player, ConstNpc.BASE_MENU, "Nhìn Cái Chó Gì", "Chơi", "Đóng");/*telegram @tomihjhj_bot*/
    }

    @Override
    public void confirmMenu(Player player, int select) {
        String time = ((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";/*telegram @tomihjhj_bot*/
        if (((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
            AllinOne.gI().lastTimeEnd = System.currentTimeMillis() + 60000;/*telegram @tomihjhj_bot*/
            reloadPlayerCounts();/*telegram @tomihjhj_bot*/
        }
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0:
                    createOtherMenu(player, ConstNpc.ALL_IN_ONE, "Trò chơi AllinOne đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy "
                            + "may mắn thì có thể tham gia thử", "Chơi\nngay", "Thể lệ");/*telegram @tomihjhj_bot*/
                    break;/*telegram @tomihjhj_bot*/
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.ALL_IN_ONE) {
            xửLýAllinOne(player, select, time);/*telegram @tomihjhj_bot*/
        } else if (player.iDMark.getIndexMenu() == ConstNpc.ALL_IN_ONE_VANG) {
            xửLýAllinOneVang(player, select, time);/*telegram @tomihjhj_bot*/
        }
    }

    private void xửLýAllinOne(Player player, int select, String time) {
        switch (select) {
            case 1:
                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Mỗi lượt chơi có 6 giải thưởng\n"
                        + "Được chọn tối đa 10 lần mỗi giải\n"
                        + "Thời gian 1 lượt chọn là 5 phút\n"
                        + "Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn\n"
                        + "của từng giải và trao thưởng.\n"
                        + "Lưu ý: Nếu thoát game sẽ mất toàn bộ số cược và tỉ lệ nhận sẽ = 0.", "OK");/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
            case 0:
                createOtherMenu(player, ConstNpc.ALL_IN_ONE_VANG, "Tổng giải thường: " + Util.numberToMoney(AllinOne.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\n"
                        + "Tổng số người chơi tham gia: Thường: " + totalPlayersNormar + ", VIP: " + totalPlayersVIP + "\n"
                        + "Lưu ý: Nếu thoát game sẽ mất toàn bộ số cược và tỉ lệ nhận sẽ = 0\n"
                        + "Tổng giải VIP: " + Util.numberToMoney(AllinOne.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n"
                        + "Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 Thỏi\nvàng", "VIP\n100 Thỏi\nvàng", "Đóng");/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
        }
    }

    private void xửLýAllinOneVang(Player player, int select, String time) {

        switch (select) {
            case 0:
                createOtherMenu(player, ConstNpc.ALL_IN_ONE_VANG, "Tổng giải thường: " + Util.numberToMoney(AllinOne.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(AllinOne.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%"
                        + "\nTổng số người chơi tham gia: Thường: " + totalPlayersNormar + ", VIP: " + totalPlayersVIP + "\n"
                        + "Lưu ý: Nếu thoát game sẽ mất toàn bộ số cược và tỉ lệ nhận sẽ = 0\n"
                        + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 Thỏi\nvàng", "VIP\n100 Thỏi\nvàng", "Đóng");/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
            case 1:
                xửLýThuong10ThoiVang(player);/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
            case 2:
                xửLýThuong100ThoiVang(player);/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
        }
    }

    private void xửLýThuong10ThoiVang(Player player) {
        try {
            String time = ((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";/*telegram @tomihjhj_bot*/
            if (((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                AllinOne.gI().lastTimeEnd = System.currentTimeMillis() + 300000;/*telegram @tomihjhj_bot*/
            }
            Item thoivang = InventoryService.gI().findItemBag(player, 457);/*telegram @tomihjhj_bot*/

            // Kiểm tra số lượng người chơi đã tham gia
            if (AllinOne.gI().getPlayerNormarCount() >= 10) {
                Service.gI().sendThongBao(player, "Số lượng người chơi đã đạt tối đa!");/*telegram @tomihjhj_bot*/
                return;/*telegram @tomihjhj_bot*/
            }
            if (thoivang != null && thoivang.quantity >= GOLD_NORMAR_AMOUNT) {
                InventoryService.gI().subQuantityItemsBag(player, thoivang, GOLD_NORMAR_AMOUNT);/*telegram @tomihjhj_bot*/
                InventoryService.gI().sendItemBag(player);/*telegram @tomihjhj_bot*/
                Service.gI().sendMoney(player);/*telegram @tomihjhj_bot*/
                player.goldNormar += GOLD_NORMAR_AMOUNT;/*telegram @tomihjhj_bot*/
                AllinOne.gI().goldNormar += GOLD_NORMAR_AMOUNT;/*telegram @tomihjhj_bot*/
                AllinOne.gI().addPlayerNormar(player);/*telegram @tomihjhj_bot*/
                reloadPlayerCounts();/*telegram @tomihjhj_bot*/
                createOtherMenu(player, ConstNpc.ALL_IN_ONE_VANG, "Tổng giải thường: " + Util.numberToMoney(AllinOne.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(AllinOne.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%"
                        + "\nTổng số người chơi tham gia: Thường: " + totalPlayersNormar + ", VIP: " + totalPlayersVIP + "\n"
                        + "Lưu ý: Nếu thoát game sẽ mất toàn bộ số cược và tỉ lệ nhận sẽ = 0\n"
                        + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 Thỏi\nvàng", "VIP\n100 Thỏi\nvàng", "Đóng");/*telegram @tomihjhj_bot*/
            } else {
                Service.gI().sendThongBao(player, "Bạn không đủ vàng");/*telegram @tomihjhj_bot*/
            }
        } catch (Exception ex) {
            System.out.println("Lỗi AllinOne");/*telegram @tomihjhj_bot*/
        }
    }

    private void xửLýThuong100ThoiVang(Player player) {
        try {
            String time = ((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";/*telegram @tomihjhj_bot*/
            if (((AllinOne.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                AllinOne.gI().lastTimeEnd = System.currentTimeMillis() + 300000;/*telegram @tomihjhj_bot*/
            }
            // Kiểm tra số lượng người chơi đã tham gia
            if (AllinOne.gI().getPlayerVIPCount() >= 10) {
                Service.gI().sendThongBao(player, "Số lượng người chơi VIP đã đạt tối đa!");/*telegram @tomihjhj_bot*/
                return;/*telegram @tomihjhj_bot*/
            }
            Item thoivang = InventoryService.gI().findItemBag(player, 457);/*telegram @tomihjhj_bot*/
            if (thoivang != null && thoivang.quantity >= GOLD_VIP_AMOUNT) {
                InventoryService.gI().subQuantityItemsBag(player, thoivang, GOLD_VIP_AMOUNT);/*telegram @tomihjhj_bot*/
                InventoryService.gI().sendItemBag(player);/*telegram @tomihjhj_bot*/
                Service.gI().sendMoney(player);/*telegram @tomihjhj_bot*/
                player.goldVIP += GOLD_VIP_AMOUNT;/*telegram @tomihjhj_bot*/
                AllinOne.gI().goldVip += GOLD_VIP_AMOUNT;/*telegram @tomihjhj_bot*/
                AllinOne.gI().addPlayerVIP(player);/*telegram @tomihjhj_bot*/
                reloadPlayerCounts();/*telegram @tomihjhj_bot*/
                //             PlayerDAO.updatePlayer(player);/*telegram @tomihjhj_bot*/
                createOtherMenu(player, ConstNpc.ALL_IN_ONE_VANG, "Tổng giải thường: " + Util.numberToMoney(AllinOne.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(AllinOne.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%"
                        + "\nTổng số người chơi tham gia: Thường: " + totalPlayersNormar + ", VIP: " + totalPlayersVIP + "\n"
                        + "Lưu ý: Nếu thoát game sẽ mất toàn bộ số cược và tỉ lệ nhận sẽ = 0\n"
                        + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 Thỏi\nvàng", "VIP\n100 Thỏi\nvàng", "Đóng");/*telegram @tomihjhj_bot*/

            } else {
                Service.gI().sendThongBao(player, "Bạn không đủ vàng");/*telegram @tomihjhj_bot*/
            }
        } catch (Exception ex) {
            System.out.println("Lỗi ALL_IN_ONE_VANG VIP");/*telegram @tomihjhj_bot*/
        }
    }
}
