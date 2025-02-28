/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Npc_List;

import consts.ConstTask;
import java.io.Serial;
import models.Item;
import npc.Npc;
import player.Player;
import services.CombineService;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.TaskService;

/**
 *
 * @author kagam
 */
public class ThuongNhan extends Npc {

    public ThuongNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player) && map.mapId == 186) {
                this.createOtherMenu(player, 111, "Ngươi Muốn Mua Rượu à?\n"
                        + "Đưa Tiền Đây Ngươi Sẽ Nhận Đươc Thứ Mình Muốn !", "Trao đổi", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (this.map.mapId == 186) {
            int confirm = player.iDMark.getIndexMenu();
            switch (confirm) {
                case 111:
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 1, "Ngươi có chắc muốn đổi 100 linh thạch cấp 1 lấy rượu chứ ?", "Đổi", "dell");
                            break;
                        case 1:
                            break;

                        default:
                            break;
                    }
                    break;
                case 1:
                    Item item = InventoryService.gI().findItemBag(player, (short) 1754);
                    if (item != null && item.quantity >= 100) {
                        InventoryService.gI().subQuantityItemsBag(player, item, 100);
                        Item nr = ItemService.gI().createNewItem((short) 1757);
                        InventoryService.gI().addItemBag(player, nr);
                        InventoryService.gI().sendItemBag(player);
                        Service.gI().sendThongBao(player, "Bạn Nhận Được bình rượu");
                        TaskService.gI().doneTask(player,ConstTask.TASK_33_3);
                    } else {
                        this.npcChat(player, "Nghèo mà cũng đòi mua rượu à ????");
                    }
                default:
                    break;
            }
        }
    }

}
