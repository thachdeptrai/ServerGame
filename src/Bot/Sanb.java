package Bot;/*telegram @tomihjhj_bot*/

import FunC.ChangeMapService;
import MobZ.Mob;
import boss.Boss;
import boss.BossManager;
import java.util.Random;/*telegram @tomihjhj_bot*/
import java.util.ArrayList;/*telegram @tomihjhj_bot*/
import java.util.List;/*telegram @tomihjhj_bot*/
import  consts.ConstPlayer;/*telegram @tomihjhj_bot*/
import services.MapService;
import services.PlayerService;
import services.SkillService;
import skill.Skill;
import  utils.Util;/*telegram @tomihjhj_bot*/

public class Sanb {

    public Bot bot;/*telegram @tomihjhj_bot*/

    public Boss bossAttack;/*telegram @tomihjhj_bot*/

    public long lastTimeSkill1;/*telegram @tomihjhj_bot*/

    public Sanb(Bot b) {
        this.bot = b;/*telegram @tomihjhj_bot*/
    }

    public void update() {
        this.SanBot();/*telegram @tomihjhj_bot*/
    }

    public boolean isMap(int mapId) {
        return (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMaBu(mapId)
                || MapService.gI().isMapMaBu(mapId)
                || MapService.gI().isMapConDuongRanDoc(mapId));/*telegram @tomihjhj_bot*/
    }

    public void GetBoss(int status) {
        if (this.bossAttack == null || this.bossAttack.isDie()) {
            this.bossAttack = BossManager.gI().getBosses().get(new Random().nextInt(BossManager.gI().getBosses().size()));/*telegram @tomihjhj_bot*/

            boolean bosAction = (!this.bossAttack.isDie() && !this.isMap(this.bossAttack.zone.map.mapId) && !this.bossAttack.zone.isFullPlayer() && this.bossAttack.zone.mobs.size() >= 1);/*telegram @tomihjhj_bot*/
            if (bosAction) {
                ChangeMapService.gI().goToMap(this.bot, this.bossAttack.zone);/*telegram @tomihjhj_bot*/
                this.bot.zone.load_Me_To_Another(this.bot);/*telegram @tomihjhj_bot*/
            }

            if (!bosAction && status < 3) {
                this.bossAttack = null;/*telegram @tomihjhj_bot*/
                this.GetBoss(status + 1);/*telegram @tomihjhj_bot*/
            } else if (!bosAction) {
                BotManager.gI().bot.remove(this.bot);/*telegram @tomihjhj_bot*/
                ChangeMapService.gI().exitMap(this.bot);/*telegram @tomihjhj_bot*/
                this.bossAttack = null;/*telegram @tomihjhj_bot*/
            }
        }
    }

    public void GetSkil() {
        if (Util.isTrue(50, 100)) {
        // Chọn kỹ năng đầu tiên nếu tỷ lệ rơi vào 50-100%
        this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(0);/*telegram @tomihjhj_bot*/
    } else {
        // Chọn kỹ năng ngẫu nhiên từ danh sách skills
        int skillIndex = Util.nextInt(this.bot.playerSkill.skills.size());/*telegram @tomihjhj_bot*/ // Đảm bảo chỉ số hợp lệ
        this.bot.playerSkill.skillSelect = this.bot.playerSkill.skills.get(skillIndex);/*telegram @tomihjhj_bot*/
    }

        if (this.lastTimeSkill1 < System.currentTimeMillis() - 50000) {
            switch (this.bot.gender) {
                case ConstPlayer.XAYDA:
                    this.bot.useSkill(Skill.BIEN_KHI );/*telegram @tomihjhj_bot*/
                    break;/*telegram @tomihjhj_bot*/
                case ConstPlayer.TRAI_DAT:
                    this.bot.useSkill(Skill.QUA_CAU_KENH_KHI);/*telegram @tomihjhj_bot*/
                    break;/*telegram @tomihjhj_bot*/
                case ConstPlayer.NAMEC:
                    this.bot.useSkill(Skill.MAKANKOSAPPO);/*telegram @tomihjhj_bot*/
                    break;/*telegram @tomihjhj_bot*/
            }
            this.lastTimeSkill1 = System.currentTimeMillis() - new Random().nextInt(150000);/*telegram @tomihjhj_bot*/
        }
    }

   public void SanBot() {
    this.GetBoss(0);/*telegram @tomihjhj_bot*/  // Cập nhật boss
    this.GetSkil();/*telegram @tomihjhj_bot*/  // Cập nhật kỹ năng

    if (this.bossAttack != null && !this.bossAttack.isDie()) {
        // Nếu bot có thể sử dụng kỹ năng
        if (this.bot.UseLastTimeSkill()) {
            int y = 0;/*telegram @tomihjhj_bot*/
            int x = 0;/*telegram @tomihjhj_bot*/

            // Lấy vị trí của boss hoặc mob gần nhất
            for (Mob m : this.bot.zone.mobs) {
                if (y < m.location.y) {
                    y = m.location.y;/*telegram @tomihjhj_bot*/
                    x = m.location.x;/*telegram @tomihjhj_bot*/
                }
            }

            // Kiểm tra xem boss có ở vị trí cụ thể hay không
            if (this.bot.zone.map.mapId == 72) {
                y = 312;/*telegram @tomihjhj_bot*/  // Điều chỉnh vị trí trong bản đồ cụ thể
            }

            // Di chuyển bot tới vị trí của boss
            PlayerService.gI().playerMove(this.bot, this.bossAttack.location.x, this.bossAttack.location.y);/*telegram @tomihjhj_bot*/

            // Sử dụng kỹ năng để tấn công boss
            SkillService.gI().useSkill(this.bot, this.bossAttack, null, -1, null);/*telegram @tomihjhj_bot*/
        }
    }
}
}
