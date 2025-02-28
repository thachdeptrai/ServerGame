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

public class BongBang extends DeathOrAliveArena {

    public BongBang(Player player) throws Exception {
        super(PHOBAN, BossID.BONG_BANG, BossesData.BONG_BANG);
        this.playerAtt = player;
    }
}
