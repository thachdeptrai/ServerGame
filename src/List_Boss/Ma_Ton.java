package List_Boss;

import boss.AppearType;
import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossStatus;
import static boss.BossType.*;
import boss.BossesData;
import consts.ConstPlayer;
import player.Player;
import skill.Skill;
import utils.Util;

/**
 *
 * @author kagam
 */
public class Ma_Ton extends Boss {

    public long st;

    public Ma_Ton() throws Exception {
        super( BossID.MATON, false,true,BossesData.MATON);

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
    public void doneChatE() {
        if (this.parentBoss == null || this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.DUONGTANG && !boss.isDie()) {
                boss.changeStatus(BossStatus.ACTIVE);
                break;
            }
        }
    }
}
