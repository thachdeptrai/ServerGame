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

public class YamchaDHVT23 extends The23rdMartialArtCongress {

    public YamchaDHVT23(Player player) throws Exception {
        super(PHOBAN, BossID.YAMCHA, BossesData.YAMCHA);
        this.playerAtt = player;
    }
}
