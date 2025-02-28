package Boss_Manager;

//import Boss_Manager;

import boss.BossManager;


/*
 *
 *
 * @author NgocThach
 */

public class BrolyManager extends BossManager {

    private static BrolyManager instance;

    public static BrolyManager gI() {
        if (instance == null) {
            instance = new BrolyManager();
        }
        return instance;
    }

}
