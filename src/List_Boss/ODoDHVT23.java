package List_Boss;

/*
 *
 *
 * @author NgocThach
 */

import boss.BossID;
import boss.BossesData;
import Abstract.The23rdMartialArtCongress;
import static boss.BossType.PHOBAN;
import player.Player;

public class ODoDHVT23 extends The23rdMartialArtCongress {

    public ODoDHVT23(Player player) throws Exception {
        super(PHOBAN, BossID.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }
}
