package services;/*telegram @tomihjhj_bot*/

import Manager.ChanLeManager;
import models.Item;/*telegram @tomihjhj_bot*/
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;

public class ChanLeServices {

    private static ChanLeServices instance;/*telegram @tomihjhj_bot*/

    public static ChanLeServices gI() {
        if (instance == null) {
            instance = new ChanLeServices();/*telegram @tomihjhj_bot*/
        }
        return instance;/*telegram @tomihjhj_bot*/
    }
    public void addPlayerOdd(Player player) {
        if (ChanLeManager.currentPariry != null) {
            ChanLeManager.gI().addPlayerOdd(player);/*telegram @tomihjhj_bot*/
        }
    }
    public void addPlayerEven(Player player) {
        if (ChanLeManager.currentPariry != null) {
            ChanLeManager.gI().addPlayerEven(player);/*telegram @tomihjhj_bot*/
        }
    }
    public int tvWiner;/*telegram @tomihjhj_bot*/
    public void rewardRuby(Player player) {
        if (player != null) {
            Item tvthang = ItemService.gI().createNewItem((short)457);/*telegram @tomihjhj_bot*/
            tvthang.quantity = (int) Math.abs(player.rubyWin * 1.5);/*telegram @tomihjhj_bot*/
            tvWiner = tvthang.quantity;/*telegram @tomihjhj_bot*/
            
            if (tvthang.template.id == 457) {
                tvthang.itemOptions.add(new Item.ItemOption(30, 1));/*telegram @tomihjhj_bot*/
                tvthang.itemOptions.add(new Item.ItemOption(93, 10));/*telegram @tomihjhj_bot*/
            }
             InventoryService.gI().addItemBag(player, tvthang);/*telegram @tomihjhj_bot*/
             Service.gI().sendThongBao(player, "Bạn đã nhận được " + tvWiner + " thỏi vàng");/*telegram @tomihjhj_bot*/
             InventoryService.gI().sendItemBag(player);/*telegram @tomihjhj_bot*/
            ChanLeManager.gI().setAfterPlayerReward(player);/*telegram @tomihjhj_bot*/
           
            player.rubyWin = 0;/*telegram @tomihjhj_bot*/
        }
    }

    public boolean checkHavePariry() {
        return ChanLeManager.currentPariry != null;/*telegram @tomihjhj_bot*/
    }

    public String getHistoryPlayer(Player player) {
        return ChanLeManager.gI().getHistoryPlayer(player);/*telegram @tomihjhj_bot*/
    }

    public String getHistory() {
        return ChanLeManager.gI().getHistoryGame();/*telegram @tomihjhj_bot*/
    }
    public boolean hasPlacedBet(Player player) {
    if (ChanLeManager.currentPariry != null) {
        // Kiểm tra xem người chơi có trong danh sách cược chẵn hoặc lẻ
        return ChanLeManager.currentPariry.getPlayersEven().contains(player) || 
               ChanLeManager.currentPariry.getPlayersOdd().contains(player);/*telegram @tomihjhj_bot*/
    }
    return false;/*telegram @tomihjhj_bot*/ // Không có phiên cược đang chạy
}

}
