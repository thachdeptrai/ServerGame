package EventZ;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import Abstract.Event;

public class TrungThu extends Event {

    @Override
    public void boss() {
        createBoss(BossID.KHIDOT, 10);
        createBoss(BossID.NGUYETTHAN, 10);
    }
}
