package List_Boss;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import boss.BossesData;
import Abstract.The23rdMartialArtCongress;
import static boss.BossType.PHOBAN;
import player.Player;
import services.EffectSkillService;
import utils.Util;

public class ThienXinHangDHVT23 extends The23rdMartialArtCongress {

    private long lastTimePhanThan = System.currentTimeMillis();

    public ThienXinHangDHVT23(Player player) throws Exception {
        super(PHOBAN, BossID.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        this.playerAtt = player;
    }

    @Override
    public void attack() {
        try {
            EffectSkillService.gI().removeStun(this);
            if (Util.canDoWithTime(lastTimePhanThan, 30000)) {
                lastTimePhanThan = System.currentTimeMillis();
                phanThan();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.attack();
    }

    private void phanThan() {
        try {
            new ThienXinHangCloneDHVT23(BossID.THIEN_XIN_HANG_CLONE, playerAtt);
            new ThienXinHangCloneDHVT23(BossID.THIEN_XIN_HANG_CLONE1, playerAtt);
            new ThienXinHangCloneDHVT23(BossID.THIEN_XIN_HANG_CLONE2, playerAtt);
            new ThienXinHangCloneDHVT23(BossID.THIEN_XIN_HANG_CLONE3, playerAtt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
