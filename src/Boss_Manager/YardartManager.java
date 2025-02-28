package Boss_Manager;

//import Boss_Manager;

import boss.BossManager;


/*
 *
 *
 * @author NgocThach
 */

public class YardartManager extends BossManager {

    private static YardartManager instance;

    public static YardartManager gI() {
        if (instance == null) {
            instance = new YardartManager();
        }
        return instance;
    }

}
