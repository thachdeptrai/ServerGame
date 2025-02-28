package Npc_List;

/**
 * @author NgocThach
 */
import consts.ConstNpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import npc.Npc;
import player.Player;
import FunC.Input;
import models.Item;
import services.InventoryService;
import services.ShopService;

public class Santa extends Npc {

    public Santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }
//@Override
//    public void openBaseMenu(Player player) {
//        if (canOpenNpc(player)) {
//
//          
//            List<String> menu = new ArrayList<>(Arrays.asList(
//                    "Cửa hàng",
//                    "Mở rộng\nHành trang\nRương đồ",
//                    "Nhập mã\nquà tặng",
//                    "Cửa hàng\nHạn sử dụng",
//                    "Tiệm\nHớt tóc",
//                    "Danh\nhiệu"
//            ));
//
//           
//
//            String[] menus = menu.toArray(new String[0]);
//
//            createOtherMenu(player, ConstNpc.BASE_MENU,
//                    "Xin chào, ta có một số vật phẩm đặc biệt cậu có muốn xem không?", menus);
//        }
//
//    }
//
//    @Override
//    public void confirmMenu(Player player, int select) {
//        if (canOpenNpc(player)) {
//            Item pGG = InventoryService.gI().findItem(player.inventory.itemsBag, 459);
//            int soLuong = 0;
//            if (pGG != null) {
//                soLuong = pGG.quantity;
//            }
//
//            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
//                if (player.idMark.isBaseMenu()) {
//                    switch (select) {
//                        case 0:
//                            ShopService.gI().opendShop(player, "SANTA", false);
//                            break;
//                        case 1:
//                            if (soLuong >= 1) {
//                                ShopService.gI().opendShop(player, "SANTA_GIAM_GIA", false);
//                            } else {
//                                ShopService.gI().opendShop(player, "SANTA_MO_RONG_HANH_TRANG", false);
//                            }
//                            break;
//                        case 2:
//                            if (soLuong >= 1) {
//                                ShopService.gI().opendShop(player, "SANTA_MO_RONG_HANH_TRANG", false);
//                            } else {
//                                Input.gI().createFormGiftCode(player);
//                            }
//                            break;
//                        case 3:
//                            if (soLuong >= 1) {
//                                Input.gI().createFormGiftCode(player);
//                            } else {
//                                ShopService.gI().opendShop(player, "SANTA_HAN_SU_DUNG", false);
//                            }
//                            break;
//                        case 4:
//                            if (soLuong >= 1) {
//                                ShopService.gI().opendShop(player, "SANTA_HAN_SU_DUNG", false);
//                            } else {
//                                ShopService.gI().opendShop(player, "SANTA_HEAD", false);

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            Item pGG = InventoryService.gI().findItem(player.inventory.itemsBag, 459);
            int soLuong = 0;
            if (pGG != null) {
                soLuong = pGG.quantity;
            }
            List<String> menu = new ArrayList<>(Arrays.asList(
                    "Cửa hàng",
                    "Mở rộng\nHành trang\nRương đồ",
                    "Nhập mã\nquà tặng",
                    "Cửa hàng\nHạn sử dụng",
                    "Tiệm\nHớt tóc",
                    "Danh\nhiệu",
                    "Phụ kiện"
            ));

            if (!player.inventory.itemsDaBan.isEmpty()) {
                menu.add(4, "Mua lại\nvật phẩm\nđã bán [" + player.inventory.itemsDaBan.size() + "/20]");
            }
            if (soLuong >= 1) {
                menu.add(1, "Giảm giá\n80%");
            }
            String[] menus = menu.toArray(new String[0]);

            createOtherMenu(player, ConstNpc.BASE_MENU,
                    "Xin chào, ta có một số vật phẩm đặc biệt cậu có muốn xem không?", menus);
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            Item pGG = InventoryService.gI().findItem(player.inventory.itemsBag, 459);
            int soLuong = 0;
            if (pGG != null) {
                soLuong = pGG.quantity;
            }
            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0 -> // Cửa hàng
                            ShopService.gI().opendShop(player, "SANTA", false);
                        case 1 -> { // Mở rộng hành trang
                            if (soLuong >= 1) {
                                ShopService.gI().opendShop(player, "SANTA_GIAM_GIA", false);
                            } else {
                                ShopService.gI().opendShop(player, "SANTA_MO_RONG_HANH_TRANG", false);
                            }
                        }
                        case 2 -> {
                            if (soLuong >= 1) {
                                ShopService.gI().opendShop(player, "SANTA_MO_RONG_HANH_TRANG", false);
                            } else {// Nhập mã quà tặng
                                Input.gI().createFormGiftCode(player);
                            }
                        }
                        case 3 -> { // Cửa hàng hạn sử dụng
                            if (soLuong > 0) {
                                Input.gI().createFormGiftCode(player);
                            } else {
                                ShopService.gI().opendShop(player, "SANTA_HAN_SU_DUNG", false);
                            }
                        }
                        case 4 -> // Mua lại vật phẩm đã bán
                        {
                            if (soLuong > 0) {
                                ShopService.gI().opendShop(player, "SANTA_HAN_SU_DUNG", false);
                            } else {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "ITEMS_DABAN", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_HEAD", false);
                                }
                            }
                        }
                        case 5 -> // Tiệm hớt tóc
                        {
                            if (soLuong > 0) {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "ITEMS_DABAN", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_HEAD", false);
                                }
                            } else {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "SANTA_HEAD", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_DANH_HIEU", false);
                                }
                            }
                        }
                        case 6 -> // Danh hiệu
                        {
                            if (soLuong > 0) {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "SANTA_HEAD", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_DANH_HIEU", false);
                                }
                            } else {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "SANTA_DANH_HIEU", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_PHUKIEN", false);
                                }
                            }
                        }
                        case 7 -> { // shop phụ kiện
                            if (soLuong > 0) {
                                if (!player.inventory.itemsDaBan.isEmpty()) {
                                    ShopService.gI().opendShop(player, "SANTA_DANH_HIEU", false);
                                } else {
                                    ShopService.gI().opendShop(player, "SANTA_PHUKIEN", false);
                                }
                            } else {
                                ShopService.gI().opendShop(player, "SANTA_PHUKIEN", false);
                            }
                        }
                        case 8 ->{
                            if(soLuong>0){
                               ShopService.gI().opendShop(player, "SANTA_PHUKIEN", false);
                            }else{
                                
                            }
                        }
                    }
                }
            }
        }

    }
}
