package EventZ;

/**
 *
 * @author NgocThach
 */

import boss.BossID;
import consts.ConstNpc;
import Abstract.Event;

public class Halloween extends Event {

    @Override
    public void npc() {
    }

    @Override
    public void boss() {
        createBoss(BossID.BIMA, 10);
        createBoss(BossID.MATROI, 10);
        createBoss(BossID.DOI, 10);
    }
}
