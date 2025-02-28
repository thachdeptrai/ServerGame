/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package EventZ;

import FunC.ChangeMapService;
import MapZ.ItemMap;
import MapZ.Zone;
import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossType;
import boss.BossesData;
import consts.ConstPlayer;
import java.util.List;
import player.Player;
import services.MapService;
import services.PlayerService;
import services.Service;
import utils.Functions;
import utils.Logger;
import utils.Util;

/**
 *
 * @author kagam
 */
public class Xinbato extends Boss {

    public static boolean PK = true;

    public Xinbato() throws Exception {
        super(BossID.XINBATO_EVENT, BossesData.XINBATO_EVENT);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        damage = 1;
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl == null || pl.isDie() || pl.isPet) {
                return;
            }
            if (Util.isTrue(40, 100)) {
                goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 50)),
                        pl.location.y, false);
            }
            if (PK) {
                PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
                PK = false;
            }
            autoChat();
            Player playerGive = playerGiveWaterBottle();
            if (playerGive != null) {
                this.chat("Cảm ơn " + playerGive.name + " đã cho ta bình nước");
                Functions.sleep(3000);
                die();
                reward(playerGive);
            }
        } catch (Exception ex) {
            Logger.logException(Boss.class, ex);
        }
    }

    @Override
    public void reward(Player pl) {
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        ItemMap itemMap = new ItemMap(this.zone, 459, 1, x, y, pl.id);
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void leaveMap() {
        List<Player> playersMap = this.zone.getPlayers();
        for (Player player : playersMap) {
            if (player.giveWaterBottle) {
                player.giveWaterBottle = false;
            }
        }
         super.leaveMap();
    }

    public void joinMap(Zone zone) {
        this.zone = zone;
        this.zone.loadBoss(this);
    }

    public long lastTimeChat;
    public String[] text = {"Dân làng tôi sắp chết khát rồi", "Cho tôi xin 99 bình nước, làm ơn"};

    public void autoChat() {
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            for (String textChat : text) {
                this.chat(textChat);
            }
            lastTimeChat = System.currentTimeMillis();
        }
    }

    public Player playerGiveWaterBottle() {
        List<Player> playersMap = this.zone.getPlayers();
        for (Player player : playersMap) {
            if (player.giveWaterBottle) {
                return player;
            }
        }
        return null;
    }
}
