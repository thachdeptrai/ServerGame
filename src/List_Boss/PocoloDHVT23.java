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

public class PocoloDHVT23 extends The23rdMartialArtCongress {

    public PocoloDHVT23(Player player) throws Exception {
        super(PHOBAN, BossID.JACKY_CHUN, BossesData.JACKY_CHUN);
        this.playerAtt = player;
    }
}
