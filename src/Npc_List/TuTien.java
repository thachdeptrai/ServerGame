/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Npc_List;

import consts.ConstNpc;
import java.util.ArrayList;
import models.Clan;
import models.Shop;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.OpenPowerService;
import services.Service;
import services.ShopService;
import services.TaskService;
import utils.Util;

/**
 *
 * @author kagam
 */
public class TuTien extends Npc {

    public TuTien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                ArrayList<String> menu = new ArrayList<>();
                menu.add("Nhiệm vụ \ntu tiên giới");
                menu.add("Mở giới hạn\ntu tiên");
                menu.add("Mở giới hạn\ntu tiên\nđệ tử");
                menu.add("Shop\nTiên nhân");
                String[] menus = menu.toArray(String[]::new);
                createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Chào con, ta rất vui khi gặp được con\nCon muốn làm gì nào ?", menus);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0:
                    NpcService.gI().createTutorial(player, tempId, avartar, player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                    break;
                case 1:
                    if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                        if (OpenPowerService.gI().openPowerTuTien(player)) {
                            player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                            Service.gI().sendMoney(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player,
                                "Bạn không đủ vàng để mở, còn thiếu "
                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                        break;
                    }
                case 2:
                    if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                        if (OpenPowerService.gI().openPowerTuTien(player.pet)) {
                            player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                            Service.gI().sendMoney(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player,
                                "Bạn không đủ vàng để mở, còn thiếu "
                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                    }
                    break;
                case 3:
                    ShopService.gI().opendShop(player,"TIENNHAN", true);
                    break;
            }
        }
    }
}
