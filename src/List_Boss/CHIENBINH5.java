package List_Boss;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import boss.BossesData;
import Abstract.Yardart;
import static boss.BossType.YARDART;

public class CHIENBINH5 extends Yardart {

    public CHIENBINH5() throws Exception {
        super(YARDART, BossID.CHIEN_BINH_5, BossesData.CHIEN_BINH_5);
    }

    @Override
    protected void init() {
        x = 1199;
        x2 = 1269;
        y = 456;
        y2 = 432;
        range = 1000;
        range2 = 150;
        timeHoiHP = 20000;
        rewardRatio = 3;
    }

}
