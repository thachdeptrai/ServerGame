package List_Boss;

/*
 * @Author: NgocThach
 * @Description: NgocThach
 */
import FunC.ChangeMapService;
import MapZ.Zone;
import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossStatus;
import java.util.logging.Level;
import java.util.logging.Logger;
import consts.ConstPlayer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import skill.Skill;
import utils.SkillUtil;
import utils.*;
import boss.BossType;
import consts.ConstRatio;
import player.Player;
import services.EffectSkillService;
import services.Service;
import services.SkillService;
public class Broly extends Boss {

    public double savedHp;

    public Broly() throws Exception {

        super(BossType.BROLY, BossID.BROLY, new BossData(
                "Broly", //name
                ConstPlayer.XAYDA, //gender
                new short[]{291, 292, 293, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                100, //dame
                new double[]{1000}, //hp
                new int[]{5, 13, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38}, //map join
                new int[][]{
                    {Skill.TAI_TAO_NANG_LUONG, 1, 1000}, {Skill.TAI_TAO_NANG_LUONG, 2, 1000}, {Skill.TAI_TAO_NANG_LUONG, 3, 1000}, {Skill.TAI_TAO_NANG_LUONG, 4, 1000}, {Skill.TAI_TAO_NANG_LUONG, 5, 1000}, {Skill.TAI_TAO_NANG_LUONG, 6, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 1000},
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 1000}, {Skill.DRAGON, 3, 1000}, {Skill.DRAGON, 4, 1000}, {Skill.DRAGON, 5, 1000}, {Skill.DRAGON, 6, 1000}, {Skill.DRAGON, 7, 1000},
                    {Skill.DEMON, 1, 1000}, {Skill.DEMON, 2, 1000}, {Skill.DEMON, 3, 1000}, {Skill.DEMON, 4, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 7, 1000},
                    {Skill.GALICK, 1, 1000}, {Skill.GALICK, 2, 1000}, {Skill.GALICK, 3, 1000}, {Skill.GALICK, 4, 1000}, {Skill.GALICK, 5, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 7, 1000},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1000}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 4, 1000}, {Skill.KAMEJOKO, 5, 1000}, {Skill.KAMEJOKO, 6, 1000}, {Skill.KAMEJOKO, 7, 1000},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1000}, {Skill.MASENKO, 3, 1000}, {Skill.MASENKO, 4, 1000}, {Skill.MASENKO, 5, 1000}, {Skill.MASENKO, 6, 1000}, {Skill.MASENKO, 7, 1000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1000}, {Skill.ANTOMIC, 3, 1000}, {Skill.ANTOMIC, 4, 1000}, {Skill.ANTOMIC, 5, 1000}, {Skill.ANTOMIC, 6, 1000}, {Skill.ANTOMIC, 7, 1000},}, //skill
                new String[]{}, //text chat 1
                new String[]{},
                new String[]{}, //text chat 3
                600//type appear
        ));
    }

    @Override
    public void active() {
        super.active();
        this.angryPlayers = new HashMap<>();
        this.angry();
    }

    @Override
    public void joinMap() {
        this.name = "Broly " + Util.nextInt(1, 100);
        this.nPoint.hpMax = Util.nextInt(100, 500000);
        this.nPoint.hp = this.nPoint.hpMax;
        this.nPoint.dame = this.nPoint.hpMax / 100;
        this.nPoint.crit = Util.nextInt(50);
        this.joinMap2();
         st = System.currentTimeMillis();
    }

    @Override
    public void checkPlayerDie(Player pl) {
        if (pl.isDie()) {
            this.chat("Chừa nha " + pl.name + " động vào ta chỉ có chết.");
            this.playersAttack.remove(pl);
            isAngry = false;
        }
    }

    public void joinMap2() {
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            try {

                int zoneid = Util.nextInt(2, this.zone.map.zones.size() - 1);

                while (zoneid < this.zone.map.zones.size() && this.zone.map.zones.get(zoneid).getBosses().size() > 0) {
                    zoneid++;
                }

                if (zoneid < this.zone.map.zones.size()) {
                    this.zone = this.zone.map.zones.get(zoneid);
                } else {
                    if (this.id == BossID.BROLY) {
                        this.changeStatus(BossStatus.DIE);
                        return;
                    }
                    this.zone = this.zone.map.zones.get(Util.nextInt(2, this.zone.map.zones.size() - 1));
                }

                if (this.zone.zoneId < 2) {
                    this.leaveMap();
                }
//               System.out.println("BOSS " + this.name + " : " + this.zone.map.mapName + " HP: "+ Util.formatRed(this.nPoint.hpMax) + " khu vực " + this.zone.zoneId + "(" + this.zone.map.mapId + ")");
                ChangeMapService.gI().changeMap(this, this.zone, -1, -1);
                this.changeStatus(BossStatus.CHAT_S);
            } catch (Exception e) {
                this.changeStatus(BossStatus.REST);
            }
        } else {
            this.changeStatus(BossStatus.RESPAWN);
        }
    }

    private long st;

    @Override
     public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {

        if (!this.isDie()) {
            addPlayerAttack(plAtt);
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (Util.isTrue(1, 30)) {
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));
                this.tangChiSo();
                SkillService.gI().useSkill(this, null, null, -1, null);
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && plAtt.playerSkill.skillSelect.template.id != Skill.TU_SAT && damage > this.nPoint.hpMax / 100) {
                damage = this.nPoint.hpMax / 100;

            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return  damage;
        } else {
            return 0;
        }
    }

    private long lastTimeAttack;

    @Override
    public void attack() {
        Player pl;
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                pl = getPlayerAttack();

                if (pl == null || pl.isDie()) {
                    return;
                }
                if (!playersAttack.contains(pl)) {
                    if (Util.isTrue(1, 30)) {
                        this.goToPlayer(pl, false);
                    }
                } else {
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(7, this.playerSkill.skills.size() - 1));
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                            } else {
                                this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                            }
                        }
                        if (Util.isTrue(1, 100)) {
                            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));
                            SkillService.gI().phanSatThuong(pl, playerTarger, id);
                            this.tangChiSo();

                        }

                        SkillService.gI().useSkill(this, pl, null, -1, null);
                        checkPlayerDie(pl);
                    } else {
                        if (Util.isTrue(1, 30)) {
                            this.goToPlayer(pl, false);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void effectCharger() {
            EffectSkillService.gI().sendEffectCharge(this);
    }
    private Map<Player, Integer> angryPlayers = new HashMap<>();

    private final List<Player> playersAttack = new ArrayList<>();
    private static final int DIS_ANGRY = 100;

    @Override
    protected void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    private void addPlayerAttack(Player plAtt) {
        boolean haveInList = false;
        for (Player pl : playersAttack) {
            if (pl.equals(plAtt)) {
                haveInList = true;
                break;
            }
        }
        if (!haveInList || !plAtt.isDie() && !isAngry) {
            playersAttack.add(plAtt);
            angryPlayers.put(plAtt, 0);
            isAngry = true;
            Service.gI().chat(this, "Mi làm ta nổi giận rồi "
                    + plAtt.name.replaceAll("$", "").replaceAll("#", ""));
        }
    }

    private boolean isInListPlayersAttack(Player player) {
        for (Player pl : playersAttack) {
            if (player.equals(pl)) {
                return true;
            }
        }
        return false;
    }
    private boolean isAngry = false;
    private Map<Player, Integer> angryCountMap = new HashMap<>();

    @Override
    public void angry() {
        if (this.playersAttack.size() < 5 && Util.isTrue(7, ConstRatio.PER100)) {
            Iterator<Player> i = this.zone.getPlayers().iterator();

            while (i.hasNext()) {
                Player pl = i.next();

                if (pl == null) {
                    continue;
                }

                double distance = Util.getDistance(this, pl); // Tính toán khoảng cách ngoài if

                // Kiểm tra người chơi không phải là Boss, không chết và chưa tấn công Broly
                if (!pl.equals(this) && distance <= DIS_ANGRY && !pl.isBoss && !pl.isDie() && !isInListPlayersAttack(pl)) {
                    this.moveToPlayer(pl);
                    if (!angryCountMap.containsKey(pl)) {
                        angryCountMap.put(pl, 0); // Nếu player chưa có trong map, khởi tạo giá trị angry là 0
                    }
                    int currentAngry = angryCountMap.get(pl);
                    angryCountMap.put(pl, currentAngry + 1);

                    // Tăng số lần tức giận
                    if (angryCountMap.getOrDefault(pl, 0) > 4) {
                        addPlayerAttack(pl);  // Chỉ làm Broly tức giận khi người chơi đạt đủ số lần tức giận
                        this.isAngry = true;
                    }

                    Service.gI().chat(this, "Tránh xa ta ra, đừng để ta nổi giận");
                    
                    effectCharger();
                }
            }
        }
    }

    protected boolean charge() {
        if (this.effectSkill.isCharging && Util.isTrue(15, 100)) {
            this.effectSkill.isCharging = false;
            return false;
        }
        if (Util.isTrue(1, 20)) {
            for (Skill skill : this.playerSkill.skills) {
                if (skill.template.id == Skill.TAI_TAO_NANG_LUONG) {
                    this.playerSkill.skillSelect = skill;
                    if (this.nPoint.getCurrPercentHP() < Util.nextInt(0, 100) && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().useSkill(this, playerTarger, null, -1, null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void die(Player plKill) {

        this.savedHp = this.nPoint.hpMax;
        if (this.savedHp > 1000000) {
            System.out.println("Đã tạo thành công Super Broly với HP: " + this.savedHp);
            try {
                Boss superBroly = new SuperBroly(this.zone, Util.nextInt(10000, 50000), this.savedHp, Util.randomBossId());
            } catch (Exception ex) {
                Logger.getLogger(Broly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.changeStatus(BossStatus.DIE);
//         super.die(plKill);
    }

    private void tangChiSo() {
        double hpMax = this.nPoint.hpMax;
        int rand = Util.nextInt(80, 100);
        hpMax = hpMax + hpMax / rand < 16_070_777 ? hpMax + hpMax / rand : 16_070_777;
        this.nPoint.hpMax = hpMax;
        this.nPoint.dame = hpMax / 10;
    }

    @Override
    public void leaveMap() {
        Zone zone = this.zone;
        int x = this.location.x;
        int y = this.location.y;
        ChangeMapService.gI().exitMap(this);
        try {
//           if(this.nPoint.hpMax >= 1000000){
//          Boss superBroly = new SuperBroly(this.zone, this.nPoint.hpMax , Util.randomBossId());
//            }
        } catch (Exception ex) {
        }
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.changeStatus(BossStatus.REST);
        this.wakeupAnotherBossWhenDisappear();
    }

}
