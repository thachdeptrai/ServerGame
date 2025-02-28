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

public class LiuLiuDHVT23 extends The23rdMartialArtCongress {

    public LiuLiuDHVT23(Player player) throws Exception {
        super(PHOBAN, BossID.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }
}
