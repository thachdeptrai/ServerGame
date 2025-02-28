package EventZ;

/**
 *
 * @author NgocThach
 */

import boss.BossID;
import Abstract.Event;

public class LunarNewYear extends Event {

    @Override
    public void npc() {
//        createNpc(0, 49, 850, 432);
    }

    @Override
    public void boss() {
        createBoss(BossID.LAN_CON, 10);
    }
}
