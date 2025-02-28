/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package EventZ;

import MapZ.Zone;
import boss.Boss;
import boss.BossID;
import static boss.BossStatus.DIE;
import boss.BossesData;
import consts.ConstRatio;
import java.util.List;
import player.Player;
import services.SkillService;
import utils.SkillUtil;
import utils.Util;

public class SoiHecQuynEvent extends Boss {

    public SoiHecQuynEvent() throws Exception {
        super(BossID.SOI_HEC_QUYN_EVENT, BossesData.HEC_QUYN_EVENT);

    }

    private boolean checkNhatXuong = false;
    private long lastTimeNhatXuong = 0;
    private long lastTimRestPawn;

    @Override
    public void reward(Player pl) {

    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        double dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (damage > 10000) {
                damage = 10000;
            }
            if (checkNhatXuong) {
                return 10;
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                // notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

    public void NhatXuong() {
        checkNhatXuong = true;
        lastTimeNhatXuong = System.currentTimeMillis();
    }

    public boolean checkNhatXuong() {
        return checkNhatXuong;
    }

    @Override
    public void attack() {
        if (isDie()) {
            super.leaveMap();

        }
        if (lastTimeNhatXuong > 0) {
            if (Util.canDoWithTime(lastTimeNhatXuong, 5000)) {
                lastTimeNhatXuong = 0;
                checkNhatXuong = false;
                super.leaveMap();
                setJustRevivaled();
                changeStatus(DIE);
                // super.respawn();
                // setJustRestToFuture();
            }
        }
        if (Util.canDoWithTime(lastTimRestPawn, 9000000)) {
            // Logger.warning("Soi hecquynh ount map");
            lastTimRestPawn = System.currentTimeMillis();
            super.leaveMap();
            setJustRevivaled();
            changeStatus(DIE);
            // super.respawn();

        }

        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (this.playerSkill != null && this.playerSkill.skills != null && !this.playerSkill.skills.isEmpty()) {
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));;
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        SkillService.gI().useSkill(this, pl, null, -1, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);

                    }
                }
            }
        } catch (Exception ex) {
            // Logger.warning(SoiHecQuynEvent.class, ex);
        }
    }

    @Override
    public void checkPlayerDie(Player pl
    ) {

    }

    @Override
    public void leaveMap() {
        // ChangeMapService.gI().spaceShipArrive(this, (byte) 2,
        // ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();

    }

    @Override
    public void joinMap() {
        super.joinMap();
        this.lastTimRestPawn = System.currentTimeMillis();
        this.lastTimeNhatXuong = 0;
        this.checkNhatXuong = false;
    }
}
