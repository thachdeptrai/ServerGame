package List_Boss;

/*
 *
 *
 * @author NgocThach
 */
import boss.Boss;
import boss.BossID;
import boss.BossesData;
import consts.ConstTaskBadges;
import models.Item;
import MapZ.ItemMap;
import player.Player;
import services.EffectSkillService;
import services.Service;
import utils.Util;

import java.util.Random;
import services.TaskService;
import services.BadgesTaskService;

public class Cooler extends Boss {

    private long st;

    public Cooler() throws Exception {
        super(BossID.COOLER, BossesData.COOLER, BossesData.COOLER_2);
    }

    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);

        BadgesTaskService.updateCountBagesTask(plKill, ConstTaskBadges.TRUM_SAN_BOSS, 1);

        int[] itemDos = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281};
        int[] itemtime = new int[]{381, 382, 383, 384, 385};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomitem = new Random().nextInt(itemtime.length);
        ItemMap it = new ItemMap(this.zone, 702, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        it.options.add(new Item.ItemOption(93, 30));
        Service.gI().dropItemMap(this.zone, it);
        if (Util.isTrue(20, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, 281, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(20, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemtime[randomitem], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (piercing) {
                damage /= 100;
            }
            if (Util.isTrue(200, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (damage > 10_000_000) {
                damage = Util.nextInt(9_000_000, 10_000_000);
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
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

}
