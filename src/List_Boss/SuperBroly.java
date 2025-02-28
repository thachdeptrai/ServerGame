package List_Boss;/*telegram @tomihjhj_bot*/

/*
 * @Author: NgocThach
 * @Description: NgocThach
 */
import Boss_Manager.BrolyManager;
import FunC.ChangeMapService;
import MapZ.Zone;
import boss.Boss;
import boss.BossData;
import boss.BossID;
import boss.BossStatus;
import boss.BossType;
import consts.ConstPlayer;
import consts.ConstRatio;/*telegram @tomihjhj_bot*/
import java.util.ArrayList;/*telegram @tomihjhj_bot*/
import java.util.*;/*telegram @tomihjhj_bot*/
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import player.Player;
import services.EffectSkillService;
import services.PetService;
import services.Service;
import services.SkillService;
import skill.Skill;
import utils.SkillUtil;/*telegram @tomihjhj_bot*/
import utils.Util;/*telegram @tomihjhj_bot*/

public class SuperBroly extends Boss {

    public SuperBroly(Zone zone, double dame, double hp, int id) throws Exception {

        super(BossType.BROLY, BossID.SUPER_BROLY, false, true, new BossData(
                "Super Broly", //name
                ConstPlayer.XAYDA, //gender
                new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                100 + dame, //dame
                new double[]{100000 + hp}, //hp
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
                new String[]{}, //text chat 2
                new String[]{}, //text chat 3
                600//type appear
        ));/*telegram @tomihjhj_bot*/
    }

    @Override
    public void reward(Player plKill) {
        if (plKill.pet == null) {
            PetService.gI().createNormalPet(plKill);/*telegram @tomihjhj_bot*/
        }
    }

    @Override
    public void active() {
        super.active();/*telegram @tomihjhj_bot*/
    }
    Broly broly = new Broly();/*telegram @tomihjhj_bot*/

    @Override
    public void joinMap() {
        this.name = "Super Broly " + Util.nextInt(10, 100);/*telegram @tomihjhj_bot*/
        this.nPoint.crit = Util.nextInt(50);/*telegram @tomihjhj_bot*/
        if (Util.isTrue(3, 5) && this.zone != null) {
            ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);/*telegram @tomihjhj_bot*/
            this.changeStatus(BossStatus.CHAT_S);/*telegram @tomihjhj_bot*/
            this.notifyJoinMap();/*telegram @tomihjhj_bot*/
        } else {
            super.joinMap();/*telegram @tomihjhj_bot*/
        }
        PetService.gI().createNormalPet(this);/*telegram @tomihjhj_bot*/
//        PlayerService.gI().changeAndSendTypePK(this.pet, ConstPlayer.PK_ALL);/*telegram @tomihjhj_bot*/
        st = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
    }

    private long st;/*telegram @tomihjhj_bot*/

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.leaveMap();/*telegram @tomihjhj_bot*/ st = System.currentTimeMillis();
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
        }
    }
    protected void effectCharger() {
        if (Util.isTrue(99, ConstRatio.PER100)) {
            EffectSkillService.gI().sendEffectCharge(this);/*telegram @tomihjhj_bot*/
        }
    }
    private Map<Player, Integer> angryPlayers = new HashMap<>();/*telegram @tomihjhj_bot*/

    public final List<Player> playersAttack = new ArrayList<>();/*telegram @tomihjhj_bot*/
    private static final int DIS_ANGRY = 100;/*telegram @tomihjhj_bot*/


    private void addPlayerAttack(Player plAtt) {
        boolean haveInList = false;/*telegram @tomihjhj_bot*/
        for (Player pl : playersAttack) {
            if (pl.equals(plAtt)) {
                haveInList = true;/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/
            }
        }
        if (!haveInList || !plAtt.isDie() && !isAngry) {
            playersAttack.add(plAtt);/*telegram @tomihjhj_bot*/
            angryPlayers.put(plAtt, 0);/*telegram @tomihjhj_bot*/
            isAngry = true;/*telegram @tomihjhj_bot*/
            Service.getInstance().chat(this, "Mi làm ta nổi giận rồi "
                    + plAtt.name.replaceAll("$", "").replaceAll("#", ""));/*telegram @tomihjhj_bot*/
        }
    }

    private boolean isInListPlayersAttack(Player player) {
        for (Player pl : playersAttack) {
            if (player.equals(pl)) {
                return true;/*telegram @tomihjhj_bot*/
            }
        }
        return false;/*telegram @tomihjhj_bot*/
    }
     private boolean isAngry = false;/*telegram @tomihjhj_bot*/
    private Map<Player, Integer> angryCountMap = new HashMap<>();/*telegram @tomihjhj_bot*/
    
    @Override
    public void angry(){
         if (this.playersAttack.size() < 5 && Util.isTrue(7, ConstRatio.PER100)) {
            Iterator<Player> i = this.zone.getPlayers().iterator();/*telegram @tomihjhj_bot*/

            while (i.hasNext()) {
                Player pl = i.next();/*telegram @tomihjhj_bot*/

                if (pl == null) {
                    continue;/*telegram @tomihjhj_bot*/
                }

                double distance = Util.getDistance(this, pl);/*telegram @tomihjhj_bot*/ // Tính toán khoảng cách ngoài if

                // Kiểm tra người chơi không phải là Boss, không chết và chưa tấn công Broly
                if (!pl.equals(this) && distance <= DIS_ANGRY && !pl.isBoss && !pl.isDie() && !isInListPlayersAttack(pl)) {
                    this.moveToPlayer(pl);/*telegram @tomihjhj_bot*/
                    if (!angryCountMap.containsKey(pl)) {
                        angryCountMap.put(pl, 0);/*telegram @tomihjhj_bot*/ // Nếu player chưa có trong map, khởi tạo giá trị angry là 0
                    }
                    int currentAngry = angryCountMap.get(pl);/*telegram @tomihjhj_bot*/
                    angryCountMap.put(pl, currentAngry + 1);/*telegram @tomihjhj_bot*/

                    // Tăng số lần tức giận
                    if (angryCountMap.getOrDefault(pl, 0) > 4) {
                        addPlayerAttack(pl);/*telegram @tomihjhj_bot*/  // Chỉ làm Broly tức giận khi người chơi đạt đủ số lần tức giận
                        this.isAngry = true;/*telegram @tomihjhj_bot*/
                    }

                    Service.getInstance().chat(this, "Tránh xa ta ra, đừng để ta nổi giận");/*telegram @tomihjhj_bot*/
                    effectCharger();/*telegram @tomihjhj_bot*/
                }
            }
        }
    }
    @Override
     public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            this.addPlayerAttack(plAtt);/*telegram @tomihjhj_bot*/
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");/*telegram @tomihjhj_bot*/
                return 0;/*telegram @tomihjhj_bot*/
            }
            if (Util.isTrue(1, 30)) {
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));/*telegram @tomihjhj_bot*/
                this.tangChiSo();/*telegram @tomihjhj_bot*/
                SkillService.gI().useSkill(this, null, null, -1, null);/*telegram @tomihjhj_bot*/
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);/*telegram @tomihjhj_bot*/
            if (!piercing && plAtt.playerSkill.skillSelect.template.id != Skill.TU_SAT && damage > this.nPoint.hpMax / 100) {
                damage = this.nPoint.hpMax / 100;/*telegram @tomihjhj_bot*/
            }
            
            this.nPoint.subHP(damage);/*telegram @tomihjhj_bot*/
            if (isDie()) {
                this.setDie(plAtt);/*telegram @tomihjhj_bot*/
                die(plAtt);/*telegram @tomihjhj_bot*/
            }
            return  damage;/*telegram @tomihjhj_bot*/
        } else {
            return 0;/*telegram @tomihjhj_bot*/
        }
    }

    private long lastTimeAttack;/*telegram @tomihjhj_bot*/

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
            try {
                Player pl = getPlayerAttack();/*telegram @tomihjhj_bot*/
                if (pl == null || pl.isDie()) {
                    return;/*telegram @tomihjhj_bot*/
                }
                angry();/*telegram @tomihjhj_bot*/
                if (!playersAttack.contains(pl)) {
                    if (Util.isTrue(1, 30)) {
                        this.goToPlayer(pl, false);/*telegram @tomihjhj_bot*/
                }
                }else{
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(7, this.playerSkill.skills.size() - 1));/*telegram @tomihjhj_bot*/
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));/*telegram @tomihjhj_bot*/
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));/*telegram @tomihjhj_bot*/
                        }
                    }
                    if (Util.isTrue(1, 100)) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));/*telegram @tomihjhj_bot*/
                        this.tangChiSo();/*telegram @tomihjhj_bot*/
                    }

                    SkillService.gI().useSkill(this, pl, null, -1, null);/*telegram @tomihjhj_bot*/
                    checkPlayerDie(pl);/*telegram @tomihjhj_bot*/
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.goToPlayer(pl,false);/*telegram @tomihjhj_bot*/
                    }
                }
                }
            } catch (Exception ex) {
                ex.printStackTrace();/*telegram @tomihjhj_bot*/
            }
        }
    }

    private void tangChiSo() {
        double hpMax = this.nPoint.hpMax;/*telegram @tomihjhj_bot*/
        int rand = Util.nextInt(80, 100);/*telegram @tomihjhj_bot*/
        hpMax = hpMax + hpMax / rand < 16_070_777 ? hpMax + hpMax / rand : 16_070_777;/*telegram @tomihjhj_bot*/
        this.nPoint.hpMax = hpMax;/*telegram @tomihjhj_bot*/
        this.nPoint.dame = hpMax / 10;/*telegram @tomihjhj_bot*/
    }
  @Override
    public void checkPlayerDie(Player pl) {
        if (pl.isDie()) {
            this.chat("Chừa nha " + pl.name + " động vào ta chỉ có chết.");/*telegram @tomihjhj_bot*/
            this.playersAttack.remove(pl);/*telegram @tomihjhj_bot*/
            isAngry = false;/*telegram @tomihjhj_bot*/
        }
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);/*telegram @tomihjhj_bot*/
        if (this.pet != null) {
            ChangeMapService.gI().exitMap(this.pet);/*telegram @tomihjhj_bot*/
        }
        this.lastZone = null;/*telegram @tomihjhj_bot*/
        this.lastTimeRest = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
        this.changeStatus(BossStatus.REST);/*telegram @tomihjhj_bot*/
        BrolyManager.gI().removeBoss(this);/*telegram @tomihjhj_bot*/
        this.dispose();/*telegram @tomihjhj_bot*/
    }
}
