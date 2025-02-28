package EventZ;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import Abstract.Event;

public class Christmas extends Event {

    @Override
     public void npc() {
        createNpc(14, 99, 700, 408);
        createNpc(7, 99, 700, 432);
        createNpc(0, 99, 800, 432);
    }
    @Override
    public void boss() {
        createBoss(BossID.ONG_GIA_NOEL, 30);
    }
}
