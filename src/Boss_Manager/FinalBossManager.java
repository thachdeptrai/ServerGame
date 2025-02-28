package Boss_Manager;

//import Boss_Manager;

import boss.BossManager;


/*
 *
 *
 * @author NgocThach
 */

public class FinalBossManager extends BossManager {

    private static FinalBossManager instance;

    public static FinalBossManager gI() {
        if (instance == null) {
            instance = new FinalBossManager();
        }
        return instance;
    }

}
