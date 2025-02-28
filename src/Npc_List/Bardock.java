package Npc_List;

/**
 *
 * @author NgocThach
 */
import consts.ConstNpc;
import consts.Nthach;
import models.Item;
import JDBCMysql.PlayerDAO;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import FunC.Input;
import services.ShopService;

public class Bardock extends Npc {

    public Bardock(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                switch (mapId) {
                    case 0,7,14 ->
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "\b|1|Đây là nơi ngươi có thể đổi bất cứ thứ gì"
                                + "\nMiễn là ngươi có tiền"
                                + "\b\n|3| Nạp VND giá trị ( cứ 20k được <20.000 VND> và <20.000 VND> trong game)"
                                + "\b|5|MBBANK: " + Nthach.SDT + " \n"
                                + "|1|Nội dung chuyển khoản: " + Nthach.MANAP + "" + player.getSession().userId + "\n"
                                + "\b|3|Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất qua admin ngocthach,\n"
                                + "mọi rủi ro tự chịu nếu không chấp hành."
                                + "\n\b|7|Bạn đang có :" + player.getSession().cash + " VND\n|4|"
                                + "Giftcode mở thành viên: codethanhvien1 - codethanhvien2 - codethanhvien3",
                                "Cửa hàng",
                                "Menu VND",
                                "Nạp Tiền",
                                "Đổi đệ",
                                " Mở thành viên",
                                "Đổi skill đệ");
                    default ->
                        super.openBaseMenu(player);
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        ShopService.gI().opendShop(player, "BARDOCK_SHOP", false);
                        break;
                    case 1:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 777,
                                    "\b|1|Có tiền rồi đổi thôi!\n"
                                    + "|6|\bCó thể nhận mốc nạp khi nạp ở quy lão Kame nha"
                                    + " \n\b|7|Bạn đang có :" + player.getSession().cash + " VND",
                                    "Đổi thỏi vàng", "Đổi ngọc xanh", "Đổi hồng ngọc");
                        }
                        break;
                    case 2:
                        NpcService.gI().createBigMessage(player, avartar, "Nhớ đăng nhập xong sau đó bấm NẠP!!!", (byte) 1, "NẠP", "https://ngocthack.bio.link");
                        break;
                    case 3:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 13000,
                                    "\b|1|Có tiền rồi đổi thôi!\n|3|"
                                    + "Đổi đệ thì tháo cái đồ đệ ra, mất tự chịu nha!\n|7|"
                                    + "Thông tin từng loại đệ:\n|2|"
                                    + "Fide nhí:+ 20 - 30% chỉ số khi hợp thể\n|2|"
                                    + "Cell nhí:+ 25 - 45% chỉ số khi hợp thể\n|2|"
                                    + "Bưu nhí:+ 30 - 55% chỉ số khi hợp thể\n|2|"
                                    + "Adr bãi biển:+ 45 - 60% chỉ số khi hợp thể\n|2|"
                                    + "Bưu gầy:+ 70% chỉ số khi hợp thể\n|2|"
                                    + "Berrus nhí:+ 45 - 80% chỉ số khi hợp thể\n|1|"
                                    + "Bạn đang có :" + player.getSession().cash + " VND\n|6|"
                                    + "\nChỉ số hợp thể đệ hiện tại :\n|7|"
                                    + "-HP:" + player.pointfusion.getHpFusion() + " ,KI:" + player.pointfusion.getMpFusion() + " ,DAME:" + player.pointfusion.getDameFusion() + "",
                                    "Mua đệ\nFide nhí\n<" + Nthach.de31 + ">", "Mua đệ\nCell nhí\n<" + Nthach.de32 + ">", "Mua đệ\nBưu nhí\n<" + Nthach.de33 + ">", "Mua đệ\nAdr bãi biển\n<" + Nthach.de34 + ">", "Mua đệ\nBưu gầy\n<" + Nthach.de35 + ">", "Mua đệ\nBerrus nhí\n<" + Nthach.de36 + ">");
                        }
                        break;

                    case 4:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 782,
                                    "\b|2|Mở thành viên giá 20000 VND \n \b|7|Bạn đã nạp :"
                                    + "" + player.getSession().cash + " đồng\n|4|"
                                    + " + \"Giftcode mở thành viên: codethanhvien1 - codethanhvien2 - codethanhvien3\"",
                                    "Mở", "Đóng");
                        }
                        break;
                    case 5:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 888,
                                    "|0|Lưu ý: Đổi Skill đệ bằng tiền nạp sẽ mất VND\n|7|"
                                    + "Bạn có: " + player.getSession().cash + " VND",
                                    //Menu CHọn
                                    "Đổi skill 2-3\n <" + Nthach.skill23 + ">", "Đổi skill 2-4\n <" + Nthach.skill24 + ">");

                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 13000) {
                switch (select) {
                    case 0:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }
                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de31) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de31 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de31)) {
                            PetService.gI().createPetFideNhi(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Fide Nhí");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }

                        break;
                    case 1:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }
                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de32) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de32 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de32)) {
                            PetService.gI().createPetCellNhi(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Cell Nhí");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }
                        break;
                    case 2:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }

                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de33) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de33 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de33)) {
                            PetService.gI().createPetBuuNhi(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Bưu Nhí");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }

                        break;
                    case 3:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }
                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de34) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de34 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de34)) {
                            PetService.gI().createPetAdrBeach(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Adr Bãi biển");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }

                        break;
                    case 4:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }
                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de35) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de35 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de35)) {
                            PetService.gI().createPetMabuGay(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Bưu Gầy");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }
                        break;
                    case 5:
                        if (player.pet == null) {
                            Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                            return;
                        }
                        for (Item item : player.pet.inventory.itemsBody) {
                            if (item.isNotNullItem()) {
                                Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                                return;
                            }
                        }
                        if (player.getSession() != null && player.getSession().cash < Nthach.de36) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.de36 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.de36)) {
                            PetService.gI().createPetBerrusNhi(player, player.pet != null, player.gender);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Berrus");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 888) {
                switch (select) {
                    case 0: //thay chiêu 2-3 đệ tử
                        if (player.getSession() != null && player.getSession().cash < Nthach.skill23) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.skill23 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.skill23)) {
                            if (player.pet != null) {
                                if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                    player.pet.openSkill2();
                                    if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                        player.pet.openSkill3();
                                    }
                                    Service.gI().sendThongBao(player, "Đổi skill 2-3 đệ thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                }
                            } else {
                                Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");

                            }
                        }
                        break;
                    case 1: //thay chiêu 2-4 đệ tử
                        if (player.getSession() != null && player.getSession().cash < Nthach.skill24) {
                            Service.gI().sendThongBao(player, "Bạn không đủ " + Nthach.skill24 + " VND");
                            return;
                        }

                        if (PlayerDAO.subcash(player, Nthach.skill24)) {
                            if (player.pet != null) {
                                if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                    player.pet.openSkill2();
                                    if (player.pet.playerSkill.skills.get(3).skillId != -1) {
                                        player.pet.openSkill4();
                                    }
                                    Service.gI().sendThongBao(player, "Đổi skill 2-4 đệ thành công");

                                } else {
                                    Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                }
                            } else {
                                Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");

                            }
                        }
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 777) {
                switch (select) {
                    case 0:
                        Input.gI().createFormDoiThoiVang(player);
                        break;
                    case 1:
                        Input.gI().createFormDoiNgocXanh(player);
                        break;
                    case 2:
                        Input.gI().createFormDoiNgocHong(player);
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 782) {
                switch (select) {
                    case 0:
                        if (player.getSession() != null && player.getSession().actived) {
                            Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                            return;
                        }
                        if (player.getSession() != null && player.getSession().danap < 20000) {
                            NpcService.gI().createBigMessage(player, avartar, "Bạn chưa nạp 20K, bạn có muốn nạp để mở thành viên không ?!!!", (byte) 1, "NẠP", "https://ngocthack.bio.link");
                            return;
                        }
                        if (PlayerDAO.updateActive(player, 1)) {
                            Service.gI().sendThongBao(player, "Bạn đã mở thành viên thành công");
                        } else {
                            Service.gI().sendThongBao(player, "Đã có lỗi xẩy ra khi kích hoạt tài khoản, vui long liên hệ admin nếu bị trừ tiền mà không kích hoạt được, chụp lại thông báo này");
                        }

                        break;
                    case 1:

                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 0) {
                switch (mapId) {
                }
            }

        }
    }
}
