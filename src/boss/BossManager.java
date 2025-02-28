package boss;

/*
 *
 *
 * @author NgocThach
 */
import EventZ.SoiHecQuynEvent;
import EventZ.Xinbato;
import utils.Functions;
import List_Boss.AnTrom;
import List_Boss.AnTromTV;
import List_Boss.BlackGoku;

import List_Boss.Rambo;
import List_Boss.MapDauDinh;
import List_Boss.Kuku;
import List_Boss.Android19;
import List_Boss.Pic;
import List_Boss.Android14;
import List_Boss.Poc;
import List_Boss.Android13;
import List_Boss.KingKong;
import List_Boss.DrKore;
import List_Boss.Android15;
import List_Boss.DeathBeam1;
import List_Boss.DeathBeam2;
import List_Boss.DeathBeam3;
import List_Boss.DeathBeam4;
import List_Boss.DeathBeam5;
import List_Boss.GoldenFrieza;
import List_Boss.Cooler;
import List_Boss.SieuBoHung;
import List_Boss.XenBoHung;
//import boss.boss_manifest.BrolyFix.Broly;
//import boss.boss_manifest.BrolyFix.BrolySuper;
import List_Boss.Broly;
import List_Boss.SuperBroly;
import List_Boss.TaoPaiPai;
import List_Boss.Fide;
import List_Boss.SonTinh;
import List_Boss.ThuyTinh;
import List_Boss.BiMa;
import List_Boss.Doi;
import List_Boss.MaTroi;
import List_Boss.KhiDot;
import List_Boss.NguyetThan;
import List_Boss.NhatThan;
import List_Boss.Mabu;
import List_Boss.BuiBui;
import List_Boss.BuiBui2;
import List_Boss.Cadic;
import List_Boss.Drabura;
import List_Boss.Drabura2;
import List_Boss.Drabura3;
import List_Boss.Goku;
import List_Boss.Yacon;
import List_Boss.Mabu2H;
import List_Boss.SuperBu;
import List_Boss.SO1;
import List_Boss.SO2;
import List_Boss.SO3;
import List_Boss.SO4;
import List_Boss.TDT;
import List_Boss.SO1_NM;
import List_Boss.SO2_NM;
import List_Boss.SO3_NM;
import List_Boss.SO4_NM;
import List_Boss.TDT_NM;
import List_Boss.BIDO;
import List_Boss.BOJACK;
import List_Boss.BUJIN;
import List_Boss.KOGU;
import List_Boss.SUPER_BOJACK;
import List_Boss.ZANGYA;
import List_Boss.CHIENBINH0;
import List_Boss.CHIENBINH1;
import List_Boss.CHIENBINH2;
import List_Boss.CHIENBINH3;
import List_Boss.CHIENBINH4;
import List_Boss.CHIENBINH5;
import List_Boss.DOITRUONG5;
import List_Boss.TANBINH0;
import List_Boss.TANBINH1;
import List_Boss.TANBINH2;
import List_Boss.TANBINH3;
import List_Boss.TANBINH4;
import List_Boss.TANBINH5;
import List_Boss.TAPSU0;
import List_Boss.TAPSU1;
import List_Boss.TAPSU2;
import List_Boss.TAPSU3;
import List_Boss.TAPSU4;
import List_Boss.XENCON1;
import List_Boss.XENCON2;
import List_Boss.XENCON3;
import List_Boss.XENCON4;
import List_Boss.XENCON5;
import List_Boss.XENCON6;
import List_Boss.XENCON7;
import List_Boss.Cumber;
import List_Boss.DUONG_TANG;
import List_Boss.LanCon;
import List_Boss.Ma_Ton;
import List_Boss.OngGiaNoel;
import List_Boss.PHONG_VAN;
import List_Boss.THIEN_DE;
import List_Boss.THIEN_MA;
import player.Player;
import network.Message;
import services.MapService;

import java.util.ArrayList;
import java.util.List;

import MapZ.Zone;
import NgocThachServer.Maintenance;
import utils.Logger;

public class BossManager implements Runnable {

    private static BossManager instance;
    public static byte ratioReward = 10;

    public static BossManager gI() {
        if (instance == null) {
            instance = new BossManager();
        }
        return instance;
    }

    public List<Boss> getBosses() {
        return this.bosses;/*telegram @tomihjhj_bot*/
    }

    public BossManager() {
        this.bosses = new ArrayList<>();
    }

    protected final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        this.createBoss(BossID.TIEU_DOI_TRUONG);
        this.createBoss(BossID.TIEU_DOI_TRUONG_NM);
        this.createBoss(BossID.THIENDE);
        this.createBoss(BossID.BOJACK);
        this.createBoss(BossID.SUPER_BOJACK);
        this.createBoss(BossID.KING_KONG);
        this.createBoss(BossID.XEN_BO_HUNG);
        this.createBoss(BossID.SIEU_BO_HUNG);
        this.createBoss(BossID.KUKU, 5);
        this.createBoss(BossID.MAP_DAU_DINH, 5);
        this.createBoss(BossID.RAMBO, 5);
        this.createBoss(BossID.FIDE);
        this.createBoss(BossID.ANDROID_14);
        this.createBoss(BossID.DR_KORE);
        this.createBoss(BossID.COOLER);
        this.createBoss(BossID.BLACK_GOKU, 5);
        this.createBoss(BossID.GOLDEN_FRIEZA, 5);
        this.createBoss(BossID.BLACK_GOKU);
//        this.createBoss(BossID.AN_TROM);
//        this.createBoss(BossID.AN_TROM_TV);
        this.createBoss(BossID.BROLY, 50);
//        this.createBoss(BossID.SUPER_BROLY, 3);
        this.createBoss(BossID.CUMBER);
        this.createBoss(BossID.SOI_HEC_QUYN_EVENT);
        this.createBoss(BossID.XINBATO_EVENT);
    }

    public void createBoss(int bossID, int total) {
        for (int i = 0; i < total; i++) {
            createBoss(bossID);
        }
    }

    public Boss createBoss(int bossID) {
        try {
            return switch (bossID) {
                case BossID.TAP_SU_0 ->
                    new TAPSU0();
                case BossID.TAP_SU_1 ->
                    new TAPSU1();
                case BossID.TAP_SU_2 ->
                    new TAPSU2();
                case BossID.TAP_SU_3 ->
                    new TAPSU3();
                case BossID.TAP_SU_4 ->
                    new TAPSU4();
                case BossID.TAN_BINH_5 ->
                    new TANBINH5();
                case BossID.TAN_BINH_0 ->
                    new TANBINH0();
                case BossID.TAN_BINH_1 ->
                    new TANBINH1();
                case BossID.TAN_BINH_2 ->
                    new TANBINH2();
                case BossID.TAN_BINH_3 ->
                    new TANBINH3();
                case BossID.TAN_BINH_4 ->
                    new TANBINH4();
                case BossID.CHIEN_BINH_5 ->
                    new CHIENBINH5();
                case BossID.CHIEN_BINH_0 ->
                    new CHIENBINH0();
                case BossID.CHIEN_BINH_1 ->
                    new CHIENBINH1();
                case BossID.CHIEN_BINH_2 ->
                    new CHIENBINH2();
                case BossID.CHIEN_BINH_3 ->
                    new CHIENBINH3();
                case BossID.CHIEN_BINH_4 ->
                    new CHIENBINH4();
                case BossID.DOI_TRUONG_5 ->
                    new DOITRUONG5();
                case BossID.SO_4 ->
                    new SO4();
                case BossID.SO_3 ->
                    new SO3();
                case BossID.SO_2 ->
                    new SO2();
                case BossID.SO_1 ->
                    new SO1();
                case BossID.TIEU_DOI_TRUONG ->
                    new TDT();
                case BossID.SO_4_NM ->
                    new SO4_NM();
                case BossID.SO_3_NM ->
                    new SO3_NM();
                case BossID.SO_2_NM ->
                    new SO2_NM();
                case BossID.SO_1_NM ->
                    new SO1_NM();
                case BossID.TIEU_DOI_TRUONG_NM ->
                    new TDT_NM();
                case BossID.BUJIN ->
                    new BUJIN();
                case BossID.KOGU ->
                    new KOGU();
                case BossID.ZANGYA ->
                    new ZANGYA();
                case BossID.BIDO ->
                    new BIDO();
                case BossID.BOJACK ->
                    new BOJACK();
                case BossID.SUPER_BOJACK ->
                    new SUPER_BOJACK();
                case BossID.KUKU ->
                    new Kuku();
                case BossID.MAP_DAU_DINH ->
                    new MapDauDinh();
                case BossID.RAMBO ->
                    new Rambo();
                case BossID.TAU_PAY_PAY_DONG_NAM_KARIN ->
                    new TaoPaiPai();
                case BossID.DRABURA ->
                    new Drabura();
                case BossID.BUI_BUI ->
                    new BuiBui();
                case BossID.BUI_BUI_2 ->
                    new BuiBui2();
                case BossID.YA_CON ->
                    new Yacon();
                case BossID.DRABURA_2 ->
                    new Drabura2();
                case BossID.GOKU ->
                    new Goku();
                case BossID.CADIC ->
                    new Cadic();
                case BossID.MABU_12H ->
                    new Mabu();
                case BossID.DRABURA_3 ->
                    new Drabura3();
                case BossID.MABU ->
                    new Mabu2H();
                case BossID.SUPERBU ->
                    new SuperBu();
                case BossID.FIDE ->
                    new Fide();
                case BossID.DR_KORE ->
                    new DrKore();
                case BossID.ANDROID_19 ->
                    new Android19();
                case BossID.ANDROID_13 ->
                    new Android13();
                case BossID.ANDROID_14 ->
                    new Android14();
                case BossID.ANDROID_15 ->
                    new Android15();
                case BossID.PIC ->
                    new Pic();
                case BossID.POC ->
                    new Poc();
                case BossID.KING_KONG ->
                    new KingKong();
                case BossID.XEN_BO_HUNG ->
                    new XenBoHung();
                case BossID.SIEU_BO_HUNG ->
                    new SieuBoHung();
                case BossID.XEN_CON_1 ->
                    new XENCON1();
                case BossID.XEN_CON_2 ->
                    new XENCON2();
                case BossID.XEN_CON_3 ->
                    new XENCON3();
                case BossID.XEN_CON_4 ->
                    new XENCON4();
                case BossID.XEN_CON_5 ->
                    new XENCON5();
                case BossID.XEN_CON_6 ->
                    new XENCON6();
                case BossID.XEN_CON_7 ->
                    new XENCON7();
                case BossID.COOLER ->
                    new Cooler();
                case BossID.BROLY ->
                    new Broly();
                case BossID.AN_TROM ->
                    new AnTrom();
                case BossID.AN_TROM_TV ->
                    new AnTromTV();
                case BossID.KHIDOT ->
                    new KhiDot();
                case BossID.NGUYETTHAN ->
                    new NguyetThan();
                case BossID.NHATTHAN ->
                    new NhatThan();
                case BossID.GOLDEN_FRIEZA ->
                    new GoldenFrieza();
                case BossID.DEATH_BEAM_1 ->
                    new DeathBeam1();
                case BossID.DEATH_BEAM_2 ->
                    new DeathBeam2();
                case BossID.DEATH_BEAM_3 ->
                    new DeathBeam3();
                case BossID.DEATH_BEAM_4 ->
                    new DeathBeam4();
                case BossID.DEATH_BEAM_5 ->
                    new DeathBeam5();
                case BossID.BIMA ->
                    new BiMa();
                case BossID.MATROI ->
                    new MaTroi();
                case BossID.DOI ->
                    new Doi();
                case BossID.ONG_GIA_NOEL ->
                    new OngGiaNoel();
                case BossID.SON_TINH ->
                    new SonTinh();
                case BossID.THUY_TINH ->
                    new ThuyTinh();
                case BossID.LAN_CON ->
                    new LanCon();
                case BossID.BLACK_GOKU ->
                    new BlackGoku();
                case BossID.XINBATO_EVENT ->
                    new Xinbato();
                case BossID.SOI_HEC_QUYN_EVENT ->
                    new SoiHecQuynEvent();
                case BossID.CUMBER ->
                    new Cumber();
                case BossID.MATON ->
                    new Ma_Ton();
                case BossID.DUONGTANG ->
                    new DUONG_TANG();
                case BossID.THIENMA ->
                    new THIEN_MA();
                case BossID.PHONGVAN ->
                    new PHONG_VAN();
                case BossID.THIENDE ->
                    new THIEN_DE();
                default ->
                    null;
            };
        } catch (Exception e) {
            Logger.error(e + "\n");
            return null;
        }
    }

    public Boss getBoss(int id) {
        try {
            Boss boss = this.bosses.get(id);
            if (boss != null) {
                return boss;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        player.iDMark.setMenuType(3);
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapBossFinal(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapHuyDiet(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapYardart(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapBossFinal(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapYardart(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapHuyDiet(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt(i);
                msg.writer().writeInt(i);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version >= 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF(boss.bossStatus.toString());
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF(boss.bossStatus.toString());
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public Boss getBossById(int bossId) {
        return this.bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    public boolean checkBosses(Zone zone, int BossID) {
        return this.bosses.stream().filter(boss -> boss.id == BossID && boss.zone != null && boss.zone.equals(zone) && !boss.isDie()).findFirst().orElse(null) != null;
    }

    public Player findBossClone(Player player) {
        return player.zone.getBosses().stream().filter(boss -> boss.id < -100_000_000 && !boss.isDie()).findFirst().orElse(null);
    }

    public Boss getBossById(int bossId, int mapId, int zoneId) {
        return this.bosses.stream().filter(boss -> boss.id == bossId && boss.zone != null && boss.zone.map.mapId == mapId && boss.zone.zoneId == zoneId && !boss.isDie()).findFirst().orElse(null);
    }

    @Override
    public void run() {
        while (!Maintenance.isRunning) {
            try {
                int delay = 150;
                long st = System.currentTimeMillis();
                for (int i = this.bosses.size() - 1; i >= 0; i--) {
                    try {
                        this.bosses.get(i).update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                if (delay - (System.currentTimeMillis() - st) > 0) {
//                    Thread.sleep(delay - (System.currentTimeMillis() - st));
//                }
                Functions.sleep(Math.max(delay - (System.currentTimeMillis() - st), 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
