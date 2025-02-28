package List_Boss;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import boss.BossesData;
import Abstract.DeathOrAliveArena;
import static boss.BossType.PHOBAN;
import player.Player;

public class ThoDauBac extends DeathOrAliveArena {

    public ThoDauBac(Player player) throws Exception {
        super(PHOBAN, BossID.THO_DAU_BAC, BossesData.THO_DAU_BAC);
        this.playerAtt = player;
    }
}
