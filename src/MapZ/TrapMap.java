package MapZ;

/*
 *
 *
 * @author NgocThach
 */

import player.Player;
import services.PlayerService;
import FunC.EffectMapService;
import utils.Util;

public class TrapMap {

    public int x;
    public int y;
    public int w;
    public int h;
    public int effectId;
    public double dame;

    public void doPlayer(Player player) {
        switch (this.effectId) {
            case 49 -> {
                if (!player.isDie() && Util.canDoWithTime(player.iDMark.getLastTimeAnXienTrapBDKB(), 1000)) {
                    player.injured(null, (dame + (Util.nextLong(-10L, 10L) * dame / 100L)), false, false);
                    PlayerService.gI().sendInfoHp(player);
                    EffectMapService.gI().sendEffectMapToAllInMap(player.zone, effectId, 2, 1, player.location.x - 32, 1040, 1);
                    player.iDMark.setLastTimeAnXienTrapBDKB(System.currentTimeMillis());
                }
            }
        }
    }

}
