/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package List_Boss;

import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossStatus;
import static boss.BossType.TUTIEN;
import boss.BossesData;
import consts.ConstPlayer;
import player.Player;
import skill.Skill;
import utils.Util;

/**
 *
 * @author kagam
 */
public class THIEN_DE extends Boss {

    public long st;

    public THIEN_DE() throws Exception {
        super( BossID.THIENDE,false,true, BossesData.THIENDE);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
        super.reward(plKill);
        if (this.currentLevel == 1) {
            return;
        }
    }

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.leaveMapNew(); st = System.currentTimeMillis();
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();
        }
    }

    @Override
    public void doneChatS() {
        this.changeStatus(BossStatus.AFK);
    }
}
