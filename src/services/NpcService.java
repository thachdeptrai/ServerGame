package services;

/*
 *
 *
 * @author NgocThach
 */
import consts.ConstNpc;
import npc.Npc;
import npc.NpcFactory;
import player.Player;
import NgocThachServer.Manager;
import network.Message;
import utils.Logger;

public class NpcService {

    private static NpcService i;

    public static NpcService gI() {
        if (i == null) {
            i = new NpcService();
        }
        return i;
    }

    public void createMenuRongThieng(Player player, int indexMenu, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.RONG_THIENG, -1, npcSay, menuSelect);
    }

    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.CON_MEO, avatar, npcSay, menuSelect);
    }

    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        createMenuConMeo(player, indexMenu, avatar, npcSay, menuSelect);
    }

    private void createMenu(Player player, int indexMenu, byte npcTempId, int avatar, String npcSay, String... menuSelect) {
        if (player == null || !player.isPl() || player.iDMark == null) {
            return;
        }
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(npcTempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(NpcService.class, e);
        }
    }

    public void createTutorial(Player player, int avatar, String npcSay) {
        Message msg;
        try {
            msg = new Message(38);
            msg.writer().writeShort(ConstNpc.CON_MEO);
            msg.writer().writeUTF(npcSay);
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createTutorial(Player player, int tempId, int avatar, String npcSay) {
        Message msg;
        try {
            msg = new Message(38);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public int getAvatar(int npcId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == npcId) {
                return npc.avartar;
            }
        }
        return 1139;
    }

//    public void createBigMessage(Player player, int avatar, String npcSay, byte type, String select, String confirn) {
//        Message msg;
//        try {
//            msg = new Message(-70);
//            msg.writer().writeShort(avatar);
//            msg.writer().writeUTF(npcSay);
//            msg.writer().writeByte(type);
//            if (type == 1) {
//                msg.writer().writeUTF(confirn);// select
//                msg.writer().writeUTF(select);// string Select
//            }
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception ex) {
//        }
//    }
    public void createBigMessage(Player player, int avatar, String npcSay, byte type, String select, String confirm) {
    Message msg;  // Đối tượng Message để chứa thông điệp

    
        try {
        msg = new Message(-70);  // Tạo một thông điệp với mã hiệu -70
        msg.writer().writeShort(avatar);  // Ghi avatar của NPC vào thông điệp
        msg.writer().writeUTF(npcSay);  // Ghi nội dung mà NPC sẽ nói
        msg.writer().writeByte(type);  // Ghi loại thông điệp

        if (type == 1) {  // Nếu là loại thông điệp yêu cầu lựa chọn
            msg.writer().writeUTF(confirm);  // Ghi nội dung yêu cầu xác nhận
            msg.writer().writeUTF(select);  // Ghi danh sách các nút (phân cách bằng dấu phẩy)
        }

        player.sendMessage(msg);  // Gửi thông điệp đến người chơi
        msg.cleanup();  // Giải phóng tài nguyên
    }
    catch (Exception ex

    
        ) {
        ex.printStackTrace();  // In lỗi ra console nếu có
    }
}

}
