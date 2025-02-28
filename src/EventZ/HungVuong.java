package EventZ;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import Abstract.Event;

public class HungVuong extends Event {

    @Override
    public void boss() {
        createBoss(BossID.THUY_TINH, 10);
    }
}
