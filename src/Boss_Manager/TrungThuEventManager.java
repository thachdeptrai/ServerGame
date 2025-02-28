package Boss_Manager;

import boss.BossManager;


/*
 *
 *
 * @author NgocThach
 */

public class TrungThuEventManager extends BossManager {

    private static TrungThuEventManager instance;

    public static TrungThuEventManager gI() {
        if (instance == null) {
            instance = new TrungThuEventManager();
        }
        return instance;
    }

}
