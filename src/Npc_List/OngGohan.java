package Npc_List;

/**
 * @author NgocThach
 */
import DBConnect.NNTDB;
import consts.ConstNpc;
import consts.ConstTask;
import consts.ConstTaskBadges;
import models.Item;

import java.util.ArrayList;
import java.util.List;

import JDBCMysql.PlayerDAO;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import FunC.Input;
import NgocThachServer.NgocRongOnline;
import services.ShopService;
import services.BadgesTaskService;
import utils.Util;

public class OngGohan extends Npc {

    public OngGohan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    int costNapVang = 1;

    int[][] napVang = {{20000, 20}, {50000, 55}, {100000, 125}, {500000, 1250}, {1000000, 2500}, {2000000, 5000}, {5000000, 15000}};

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "- Xin Chào " + player.name + " đến với Ngọc Rồng " + NgocRongOnline.NAME
                        + "\n\b|7|Con đang có :" + player.getSession().cash + " VND\n|0|"
                        + "\n\b|7|Con đang có :" + player.getSession().hongngoc + " hồng ngọc\n|0|"
                        + "\nDùng COIN, VND hãy qua NPC BARDOCK, QUY LÃO ở đảo Kame nhé!!!"
                        + "\n Con có thể nhận quà nạp mốc, đua top sự kiện tại Quy Lão Kame\n"
                        + "Mở thành viên tại Bardock làng , nhận đệ tử miễn phí\n|1|"
                        + "Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất qua admin Mew Mew,"
                        + "\nmọi rủi ro tự chịu nếu không chấp hành.",
                        "GiftCode\n[HOT]",
                        "Nạp tiền\n[HOT]",
                        "Xóa đệ\n[10K]",
                        "Nhận 10m\nNgọc Xanh\n[FREE]",
                        "Nhận đệ tử\n[FREE]",
                        "Hỗ trợ\nNhiệm vụ\n[FREE]",
                        "Hộp thư\n[HOT]",
                        "Nhận Hồng Ngọc\n[HOT]",
                        "Xem gói\nVip\n[HOT]",
                        "Từ chối");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // mã quà tặng
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 1: // nạp tiền
                        String npcSay = "Số dư của con là: " + Util.mumberToLouis(player.getSession().cash) + " VND dùng để nạp qua đơn vị khác\n"
                                + "Ta đang giữ giúp con " + Util.mumberToLouis(player.getSession().goldBar) + " thỏi vàng";
                        createOtherMenu(player, ConstNpc.NAP_TIEN, npcSay,
                                "Nạp VNĐ",
                                "Nạp vàng",
                                "Nhận\nThỏi vàng",
                                "Đóng");
                        break;
                    case 2:// xóa đệ
                        this.createOtherMenu(player, 12456,
                                "|0|Bạn muốn xóa đệ với giá 10K VND ?",
                                "Đồng ý", "Không");
                        break;

                    case 3: // nhận ngọc
                        if (player.inventory.gem >= 5000000) {
                            Service.gI().sendThongBao(player, "Tiêu hết đi đã r nhận cu");
                            return;
                        }
                        int soLuongNgoc = 10_000_000;
                        player.inventory.gem += soLuongNgoc;
                        Service.gI().sendMoney(player);
                        Service.gI().sendThongBao(player, "Bạn vừa nhận " + Util.mumberToLouis(soLuongNgoc) + " ngọc xanh");
                        break;
                    case 4: // nhận đệ
                        if (player.pet == null) {
                            PetService.gI().createNormalPet(player);
                            Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                        } else {
                            this.npcChat(player, "Bú ít thôi con, giấu số đá còn lại ở đâu r ");
                        }

                        break;
                    case 5:
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_9_0 && TaskService.gI().getIdTask(player) < ConstTask.TASK_11_0) {
                            player.playerTask.taskMain.id = 10;
                            player.playerTask.taskMain.index = 0;
                            TaskService.gI().sendNextTaskMain(player);
                            Service.gI().sendThongBao(player, "Bạn đã được hỗ trợ nhiệm vụ thành công");
                        } else if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_18_0 && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                            player.playerTask.taskMain.id = 19;
                            player.playerTask.taskMain.index = 0;
                            TaskService.gI().sendNextTaskMain(player);
                            Service.gI().sendThongBao(player, "Bạn đã được hỗ trợ nhiệm vụ thành công");
                        } else {
                            Service.gI().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ tàu pảy pảy và nhiệm vụ DHVT, Trung úy trắng");
                        }
                        break;
                    case 6:
                        this.createOtherMenu(player, ConstNpc.MAIL_BOX,
                                "|0|Tình yêu như một dây đàn\n"
                                + "Tình vừa được thì đàn đứt dây\n"
                                + "Đứt dây này anh thay dây khác\n"
                                + "Mất em rồi anh biết thay ai?",
                                "Hòm Thư\n(" + (player.inventory.itemsMailBox.size()
                                - InventoryService.gI().getCountEmptyListItem(player.inventory.itemsMailBox))
                                + " món)",
                                "Xóa Hết\nHòm Thư", "Đóng");
                        break;
                    case 7:
                        this.createOtherMenu(player, ConstNpc.ALL_IN_ONE, "Con Đang Còn " + player.getSession().hongngoc + " Hồng ngọc"
                                + " Chưa Nhận \n"
                                + "Con Có Muốn Nhận Không ?", "Nhận", "Từ Chối");
                        break;

                    case 8:
                        this.createOtherMenu(player, 6, "|7|Kích Hoạt Vip Tháng\n"
                                + "|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + "\n|5|Trạng thái tài khoản : " + (player.getSession().actived == false ? "Chưa kích hoạt" : "Đã kích hoạt")
                                + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "VIP4" : player.vip == 5 ? "VIP5" : player.vip == 6 ? "SVIP 6" : "Chưa Kích Hoạt")
                                + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""), "Hướng Dẫn\n[Chi Tiết]", "Kích Hoạt\nVIP", "Điểm Danh", "Đóng");

                        break;
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.ALL_IN_ONE) {
                switch (select) {
                    case 0:
                        if (player.getSession().hongngoc > 0 && player.inventory.ruby < Integer.MAX_VALUE) {
                            Input.gI().INPUTHONGNGOC(player);
                        } else {
                            this.npcChat(player, "Con làm gì có hồng ngọc mà rút");
                        }
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MAIL_BOX) {
                switch (select) {
                    case 0:
                        ShopService.gI().opendShop(player, "ITEMS_MAIL_BOX", true);
                        break;
                    case 1:
                        NpcService.gI().createMenuConMeo(player,
                                ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX, this.avartar,
                                "|3|Bạn chắc muốn xóa hết vật phẩm trong hòm thư?\n"
                                + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                "Đồng ý", "Hủy bỏ");
                        break;
                    case 2:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_TIEN) {
                switch (select) {
                    case 0:
                        NpcService.gI().createBigMessage(player, avartar, "Nhớ đăng nhập xong sau đó bấm NẠP!!!", (byte) 1, "NẠP", "https://ngocthack.bio.link");
                        break;
                    case 1:
                        List<String> menu = new ArrayList<>();
                        for (int i = 0; i < napVang.length; i++) {
                            menu.add(i, Util.mumberToLouis(napVang[i][0]) + "\n" + Util.mumberToLouis(napVang[i][1] * costNapVang) + " Thỏi\nvàng");
                        }
                        String[] menus = menu.toArray(new String[0]);
                        createOtherMenu(player, ConstNpc.NAP_VANG, "Ta sẽ giữ giúp con\n"
                                + "Nếu con cần dùng tới hãy quay lại đây gặp ta!", menus);
                        break;
                    case 2:
                        if (player.getSession().goldBar > 0) {
                            List<Item> listItem = new ArrayList<>();
                            Item thoiVang = ItemService.gI().createNewItem((short) 457, player.getSession().goldBar);
                            listItem.add(thoiVang);
                            if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                                Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                            }
                            for (Item it : listItem) {
                                InventoryService.gI().addItemBag(player, it);
                                InventoryService.gI().sendItemBag(player);
                            }
                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + player.getSession().goldBar + " thỏi vàng");
                            PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                            PlayerDAO.subGoldBar(player, player.getSession().goldBar);
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_VANG) {
                if (player.getSession().cash >= napVang[select][0]) {
                    List<Item> listItem = new ArrayList<>();
                    if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                        Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                    }
                    for (Item it : listItem) {
                        InventoryService.gI().addItemBag(player, it);
                        InventoryService.gI().sendItemBag(player);
                    }
                    PlayerDAO.subcash(player, napVang[select][0]);
                    BadgesTaskService.updateCountBagesTask(player, ConstTaskBadges.DAI_GIA_MOI_NHU, napVang[select][0]);
                    PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                    Service.gI().sendThongBao(player, "Bạn có thêm " + Util.mumberToLouis(napVang[select][1] * costNapVang) + " thỏi vàng");
                } else {
                    Service.gI().sendThongBao(player, "Không đủ số dư");
                }
            } else if (player.iDMark.getIndexMenu() == 12456) {
                switch (select) {
                    case 0:
                        if (player.getSession().cash < 10000) {
                            Service.gI().sendThongBao(player, "Xóa đệ cần 10K VND");
                            return;
                        }
                        if (player.pet != null) {
                            PetService.gI().deletePet(player);
                        }
                        break;
                    case 1:
                        break;

                }
            } //8
            else if (player.iDMark.getIndexMenu() == 6) {
                switch (select) {
                    case 0:
                        String messages
                                = "|7| Hướng dẫn cụ thể về Vip\b"
                                + "|5|Server hỗ trợ 6 gói vip cho các player\b"
                                + "|5|Mỗi gói có hạn trong 1 tháng\b"
                                + "|5|Hãy đến đây để điểm danh nhận hàng ngày!\b"
                                + "|7|Lưu ý!!!\b"
                                + "|7|Gói vip chỉ có thể nâng cấp\b"
                                + "|7|KHÔNG CỘNG DỒN\n"
                                + "|7| Giá các cấp VIP:\b"
                                + "|5| VIP 1: 100K\b"
                                + "|5| VIP 2: 200K\b"
                                + "|5| VIP 3: 300K\b"
                                + "|5| VIP 4: 400K\b"
                                + "|5| VIP 5: 500K\b"
                                + "|5| VIP 6: 600K\n"
                                + "|7| Phần Thưởng Mỗi Ngày:\b"
                                + "|5| VIP 1: 20000hn - 10tv\b"
                                + "|5| VIP 2: 40000hn - 20tv\b"
                                + "|5| VIP 3: 65000hn - 30tv\b"
                                + "|5| VIP 4: 800000hn - 50tv\b"
                                + "|5| VIP 5: 130000hn - 100tv\b"
                                + "|5| VIP 6: 200000hn - 200tv\b"
                                + "|5|Chúc May Mắn!!!";
                        NpcService.gI().createTutorial(player, this.avartar, messages);
                        break;

                    case 1:
                        this.createOtherMenu(player, 12610, "|7|MUA THẺ VIP THÁNG\n"
                                + "|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "VIP4" : player.vip == 5 ? "VIP5" : player.vip == 6 ? "SVIP 6" : "Chưa Kích Hoạt")
                                + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt\nVIP1\n[100.000]", "Kích Hoạt\nVIP2\n{200.000Đ]", "Kích Hoạt\nVIP3\n[300.000Đ]", "Kích Hoạt\nVIP4\n[400.000Đ]", "Kích Hoạt\nVIP5\n[500.000Đ]", "Kích Hoạt\nSVIP 6\n[600.000Đ]", "Đóng");
                        break;
                    case 2:
                        if (player.vip >= 1) {
                            if (player.diemdanh < 1) {
                                int tv = 0;
                                int hn = 0;
                                switch (player.vip) {

                                    case 1:
                                        tv = 10;
                                        hn = 20000;
                                        break;
                                    case 2:
                                        tv = 20;
                                        hn = 40000;
                                        break;
                                    case 3:
                                        tv = 30;
                                        hn = 65000;
                                        break;
                                    case 4:
                                        tv = 50;
                                        hn = 80000;
                                        break;
                                    case 5:
                                        tv = 100;
                                        hn = 130000;
                                        break;
                                    case 6:
                                        tv = 200;
                                        hn = 200000;
                                        break;
                                }
                                player.inventory.ruby += hn;
                                Item thoivang = ItemService.gI().createNewItem((short) 457, tv);
                                InventoryService.gI().addItemBag(player, thoivang);
                                InventoryService.gI().sendItemBag(player);
                                Service.gI().sendMoney(player);
                                player.diemdanh++;
                                Service.gI().sendThongBao(player, "|7|Điểm danh thành công!\nNhận được " + tv + " Thỏi vàng và " + Util.formatNumber(hn) + " Hồng ngọc");
                            } else {
                                this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                            }
                        } else {
                            this.npcChat(player, "Mở vip đi em");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 12610) {
                switch (select) {
                    case 0:
                        this.createOtherMenu(player, 22610, "|7|VIP1\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 1:
                        this.createOtherMenu(player, 32610, "|7|VIP2\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 2:
                        this.createOtherMenu(player, 42610, "|7|VIP3\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 3:
                        this.createOtherMenu(player, 52610, "|7|VIP4\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 4:
                        this.createOtherMenu(player, 62610, "|7|VIP5\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 5:
                        this.createOtherMenu(player, 72610, "|7|SVIP 6\n"
                                + "\n|2|Số tiền hiện tại : " + Util.formatNumber(player.getSession().cash) + " VNĐ"
                                + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : VIP4 " : player.vip == 5 ? "\n|7|Trạng thái VIP : VIP5" : player.vip == 6 ? "\n|7|Trạng thái VIP : SVIP 6" : "")
                                + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 22610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 1) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 1 ? "VIP1" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 100000) {
                            player.vip = 1;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player, 100000);
                            Service.gI().sendMoney(player);
                            player.diemdanh -=1;
                            this.npcChat(player, "|6|Đã mở thành công\n|7|VIP1");
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 32610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 2) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 2 ? "VIP2" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 200000) {
                            player.vip = 2;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player,200000);
                            Service.gI().sendMoney(player);
                            player.diemdanh -=1;
                            this.npcChat(player, "|6|Đã mở thành công\n|7|VIP2 ");
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 42610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 3) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 3 ? "VIP3" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 300000) {
                            player.vip = 3;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player, 300000);
                            Service.gI().sendMoney(player);
                            this.npcChat(player, "|6|Đã mở thành công\n|7|VIP3 ");
                            player.diemdanh -=1;
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 52610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 4) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "VIP4" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 400000) {
                            player.vip = 4;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player, 400000);
                            Service.gI().sendMoney(player);
                            player.diemdanh -=1;
                            this.npcChat(player, "|6|Đã mở thành công\n|7|SVIP ");
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 62610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 5) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip >= 6 ? "SVIP" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 500000) {
                            player.vip = 5;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player, 500000);
                            Service.gI().sendMoney(player);
                            player.diemdanh -=1;
                            this.npcChat(player, "|6|Đã mở thành công\n|7|SVIP ");
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 72610) {
                switch (select) {
                    case 0:
                        if (player.vip >= 6) {
                            this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 6 ? "SVIP" : "VIP" + player.vip) + " rồi");
                            return;
                        }
                        if (player.getSession().cash >= 600000) {
                            player.vip = 6;
                            player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                            PlayerDAO.subcash(player, 600000);
                            Service.gI().sendMoney(player);
                            player.diemdanh -=1;
                            this.npcChat(player, "|6|Đã mở thành công\n|7|SVIP ");
                        } else {
                            this.npcChat(player, "Bạn không đủ tiền");
                        }
                        break;
                }
            }
        }
    }
}
