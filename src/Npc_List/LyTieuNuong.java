package Npc_List;

/**
 * @author NgocThach
 */
import MiniGameZ.DecisionMaker;
import MiniGameZ.DecisionMakerGem;
import MiniGameZ.DecisionMakerGold;
import MiniGameZ.DecisionMakerRuby;
import MiniGameZ.LuckyNumber;
import MiniGameZ.LuckyNumberService;
import MiniGameZ.RockPaperScissors;
import npc.Npc;
import player.Player;
import services.TaskService;
import FunC.Input;
import consts.ConstTask;
import services.Service;

public class LyTieuNuong extends Npc {

    public class ConstMiniGame {

        public static final byte MENU_CHINH = 0;
        public static final byte MENU_KEO_BUA_BAO = 1;
        public static final byte MENU_CON_SO_MAY_MAN_VANG = 2;
        public static final byte MENU_CON_SO_MAY_MAN_NGOC = 3;
        public static final byte MENU_CHON_AI_DAY = 4;

        public static final byte MENU_PLAY_KEO_BUA_BAO = 5;

        public static final byte MENU_LUCKY_NUMBER = 6;
        public static final byte MENU_PLAY_LUCKY_NUMBER_GOLD = 7;
        public static final byte MENU_PLAY_LUCKY_NUMBER_GEM = 8;

        public static final byte MENU_PLAY_DECISION_MAKER_GOLD = 9;
        public static final byte MENU_PLAY_DECISION_MAKER_RUBY = 10;
        public static final byte MENU_PLAY_DECISION_MAKER_GEM = 11;
        public static final byte MENU_WAIT_NEW_GAME = 12;
    }

    public LyTieuNuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
//        services.Service.gI().sendThongBaoOK(player, "Chức năng tạm đóng");
        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            createOtherMenu(player, ConstMiniGame.MENU_CHINH, "Mini game.", "Kéo\nBúa\nBao", "Con số\nmay mắn\nvàng", "Con số\nmay mắn\nngọc xanh", "Chọn ai đây", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstMiniGame.MENU_CHINH:
                    switch (select) {
                        case 0: // Kéo búa bao
                            createOtherMenu(player, ConstMiniGame.MENU_KEO_BUA_BAO, "Hãy chọn mức cược.", "1 Tr vàng", "5 Tr vàng", "10 Tr vàng");
                            break;
                        case 1: // Con số may mắn vàng
                            LuckyNumber.showMenu(this, player, false);
                            player.iDMark.setGemCSMM(false);
                            break;
                        case 2: // Con số may mắn ngọc
                            LuckyNumber.showMenu(this, player, true);
                            player.iDMark.setGemCSMM(true);
                            break;
                        case 3: // Chọn ai đây
                            DecisionMaker.gI().showMenu(this, player);
                            break;
                        default:
                            break;
                    }
                    break;
                case ConstMiniGame.MENU_KEO_BUA_BAO:
                    if (player.getSession().actived) {
                        RockPaperScissors.confirmMenu(this, player, select);
                    }else{
                        Service.getInstance().sendThongBaoFromAdmin(player, "Mở Thành viên Đi em");
                    }
                    break;
                case ConstMiniGame.MENU_PLAY_KEO_BUA_BAO:
                    if (player.getSession().actived) {
                    if (player.iDMark.getTimePlayKeoBuaBao() - System.currentTimeMillis() > 0) {
                        RockPaperScissors.confirmPlay(this, player, select);
                    } else {
                        createOtherMenu(player, ConstMiniGame.MENU_KEO_BUA_BAO, "Hãy chọn mức cược.", "1 Tr vàng", "5 Tr vàng", "10 Tr vàng");
                    }
                    }else{
                        Service.getInstance().sendThongBaoFromAdmin(player, "Mở Thành viên Đi em");
                    }
                    break;
                case ConstMiniGame.MENU_CON_SO_MAY_MAN_VANG:

                    break;
                case ConstMiniGame.MENU_CON_SO_MAY_MAN_NGOC:

                    break;
                case ConstMiniGame.MENU_CHON_AI_DAY:
                    switch (select) {
                        case 0 ->
                            DecisionMaker.gI().showTutorial(this, player);
                        case 1 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGold.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 2 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerRuby.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 3 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGem.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                    }
                    break;
                case ConstMiniGame.MENU_LUCKY_NUMBER:
                    if (select == 0) {
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                            LuckyNumber.showMenu(this, player, player.iDMark.isGemCSMM());
                        } else {
                            Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                        }
                    }
                    break;
                case ConstMiniGame.MENU_PLAY_LUCKY_NUMBER_GOLD, ConstMiniGame.MENU_PLAY_LUCKY_NUMBER_GEM:
                    switch (select) {
                        case 0:
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                LuckyNumber.showMenu(this, player, player.iDMark.isGemCSMM());
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                            break;
                        case 1:
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                Input.gI().createFormSelectOneNumberLuckyNumber(player, player.iDMark.isGemCSMM());
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                            break;
                        case 2:
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                LuckyNumberService.addOneNumber(player, true);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }

                            break;
                        case 3:
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                LuckyNumberService.addOneNumber(player, false);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                            break;
                        case 4:
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                LuckyNumber.showMenuTutorials(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case ConstMiniGame.MENU_PLAY_DECISION_MAKER_GOLD:
                    switch (select) {
                        case 0 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGold.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 1 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGold.selectPlay(this, player, true);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 2 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGold.selectPlay(this, player, false);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                    }
                    break;
                case ConstMiniGame.MENU_PLAY_DECISION_MAKER_GEM:
                    switch (select) {
                        case 0 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGem.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 1 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGem.selectPlay(this, player, true);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 2 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerGem.selectPlay(this, player, false);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                    }
                    break;
                case ConstMiniGame.MENU_PLAY_DECISION_MAKER_RUBY:
                    switch (select) {
                        case 0 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerRuby.showMenuSelect(this, player);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 1 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerRuby.selectPlay(this, player, true);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                        case 2 -> {
                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                DecisionMakerRuby.selectPlay(this, player, false);
                            } else {
                                Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                            }
                        }
                    }
                    break;
                case LyTieuNuong.ConstMiniGame.MENU_WAIT_NEW_GAME:
                    if (select == 0) {
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                            DecisionMaker.gI().showTutorial(this, player);
                        } else {
                            Service.gI().sendThongBaoOK(player, "Bạn phải xonh nhiệm vụ 24 mới được chơi");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
