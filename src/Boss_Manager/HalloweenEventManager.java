package Boss_Manager;

//import Boss_Manager;

import boss.BossManager;


/*
 *
 *
 * @author NgocThach
 */

public class HalloweenEventManager extends BossManager {

    private static HalloweenEventManager instance;

    public static HalloweenEventManager gI() {
        if (instance == null) {
            instance = new HalloweenEventManager();
        }
        return instance;
    }
}
