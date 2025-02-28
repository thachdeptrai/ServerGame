package NgocThachServer;

/*
 *
 *
 * @author NgocThach
 */
import utils.FileRunner;
import utils.Functions;
import Boss_Manager.AnTromManager;
import Boss_Manager.BrolyManager;
import MiniGameZ.DecisionMaker;
import MiniGameZ.LuckyNumber;
import Manager.ConsignShopManager;
import JDBCMysql.HistoryTransactionDAO;
import boss.BossManager;
import Boss_Manager.*;
import Bot.BotManager;
import BotTelegram.MyTelegramBot;
import BotTelegram.SendKeyWeb;
import DBConnect.NNTDB;
import java.time.*;
import consts.ConstDataEventNAP;
import consts.ConstDataEventSM;
import java.io.IOException;
import network.inetwork.ISession;
import network.Network;
import SessionZ.MyKeyHandler;
import SessionZ.MySession;
import services.ClanService;
import services.NgocRongNamecService;
import utils.Logger;
import utils.TimeUtil;
import java.util.*;
import Manager.The23rdMartialArtCongressManager;
import Manager.DeathOrAliveArenaManager;
import Manager.EventManager;
import JDBCMysql.EventDAO;
import JDBCMysql.PlayerDAO;
import Manager.ChanLeManager;
import Manager.WorldMartialArtsTournamentManager;
import network.MessageSendCollect;
import Manager.ShenronEventManager;
import Manager.SuperRankManager;
import Manager.TransactionManager;
import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;
import java.util.zip.*;
import network.inetwork.ISessionAcceptHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import player.Player;
import services.Service;
import utils.Util;
import java.sql.Timestamp;

public class NgocRongOnline {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Local";
    public static String IP = "127.0.0.1";
    public static int PORT = 14445;
    public MyTelegramBot myTelegramBot;

    private static NgocRongOnline instance;

    public static boolean isRunning;

    public void init() {
        Manager.gI();
        HistoryTransactionDAO.deleteHistory();
        TransactionManager.getInstance().deleteOldTransactions();
    }

    public static NgocRongOnline gI() {
        if (instance == null) {
            instance = new NgocRongOnline();
            instance.init();
        }
        return instance;
    }
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");

        NgocRongOnline.gI().runBottele();
        NgocRongOnline.gI().run();
        scheduler.scheduleAtFixedRate(() -> deleteOldUnprocessedTransactions(), 0, 5, TimeUnit.MINUTES);
        try {
            Logger.error("Waiting for 5 minutes before starting backup...\n");
            Functions.sleep(5 * 60 * 1000); // 5 phút = 5 * 60 * 1000 milliseconds
        } catch (Exception e) {
            Logger.error("Lỗi khi đợi trước khi sao lưu");
        }
//         Thực hiện sao lưu trong một luồng riêng biệt
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                backupSrcFolder();
                backupDatabase();
            } catch (Exception e) {
                Logger.error("Lỗi trong quá trình sao lưu");
            }
        });
    }

    public void runBottele() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            myTelegramBot = new MyTelegramBot();
            botsApi.registerBot(myTelegramBot);
            SendKeyWeb.gI().sendStartupMessage();
            System.out.println("Bot đã khởi chạy thành công!");
        } catch (TelegramApiException e) {
            Logger.error("Lỗi khi khởi chạy Telegram Bot");
        }
    }

    public void run() {
        isRunning = true;
        activeServerSocket();
        activeCommandLine();
        new Thread(NgocRongNamecService.gI(), "Update NRNM").start();
        new Thread(SuperRankManager.gI(), "Update Super Rank").start();
        new Thread(The23rdMartialArtCongressManager.gI(), "Update DHVT23").start();
        new Thread(DeathOrAliveArenaManager.gI(), "Update Võ Đài Sinh Tử").start();
        new Thread(WorldMartialArtsTournamentManager.gI(), "Update WMAT").start();
        new Thread(AutoMaintenance.gI(), "Update Bảo Trì Tự Động").start();
        new Thread(ShenronEventManager.gI(), "Update Shenron").start();
        new Thread(BotManager.gI(), " thread bot").start();
        BossManager.gI().loadBoss();
        Manager.MAPS.forEach(MapZ.Map::initBoss);
        EventManager.gI().init();
        new Thread(BossManager.gI(), "Update boss").start();
        new Thread(YardartManager.gI(), "Update yardart boss").start();
        new Thread(FinalBossManager.gI(), "Update final boss").start();
        new Thread(SkillSummonedManager.gI(), "Update Skill-summoned boss").start();
        new Thread(BrolyManager.gI(), "Update broly boss").start();
        new Thread(AnTromManager.gI(), "Update antrom boss").start();
        new Thread(OtherBossManager.gI(), "Update other boss").start();
        new Thread(RedRibbonHQManager.gI(), "Update reb ribbon hq boss").start();
        new Thread(TreasureUnderSeaManager.gI(), "Update treasure under sea boss").start();
        new Thread(SnakeWayManager.gI(), "Update snake way boss").start();
        new Thread(GasDestroyManager.gI(), "Update gas destroy boss").start();
        new Thread(TrungThuEventManager.gI(), "Update trung thu event boss").start();
        new Thread(HalloweenEventManager.gI(), "Update halloween event boss").start();
        new Thread(ChristmasEventManager.gI(), "Update christmas event boss").start();
        new Thread(HungVuongEventManager.gI(), "Update Hung Vuong event boss").start();
        new Thread(LunarNewYearEventManager.gI(), "Update lunar new year event boss").start();
        new Thread(LuckyNumber.gI(), "Update Lucky Number").start();
        new Thread(DecisionMaker.gI(), "Update Decision Maker").start();
        new Thread(ChanLeManager.gI(), "Update Chan Le").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long st = System.currentTimeMillis();
                    ConstDataEventSM.isRunningSK = ConstDataEventSM.isActiveEvent();
                    ConstDataEventNAP.isRunningSK = ConstDataEventNAP.isActiveEvent();
                    Functions.sleep(Math.max(500 - (System.currentTimeMillis() - st), 10));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update SK").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    processPendingTransactions();
                    try {
                        Functions.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "update autobank").start();
    }

    private void activeServerSocket() {
        try {
            Network.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
                @Override
                public void sessionInit(ISession is) {
                    if (!canConnectWithIp(is.getIP())) {
                        is.disconnect();
                        return;
                    }
                    is.setMessageHandler(Controller.gI())
                            .setSendCollect(new MessageSendCollect())
                            .setKeyHandler(new MyKeyHandler())
                            .startCollect();
                }

                @Override
                public void sessionDisconnect(ISession session) {
                    Client.gI().kickSession((MySession) session);
                }
            }).setTypeSessioClone(MySession.class)
                    .setDoSomeThingWhenClose(() -> {
                        Logger.error("SERVER CLOSE\n");
                        System.exit(0);
                    })
                    .start(PORT);
        } catch (Exception e) {
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    new Thread(() -> {
                        Maintenance.gI().start(5);
                    }).start();
                } else if (line.equals("athread")) {
                    System.out.println("Số thread hiện tại của Server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    System.out.println("Số lượng người chơi hiện tại của Server: " + Client.gI().getPlayers().size());
                } else if (line.equals("shop")) {
                    Manager.gI().updateShop();
                    System.out.println("===========================DONE UPDATE SHOP===========================");
                } else if (line.equals("a")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                }
            }
        }, "Active line").start();
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    public void close() {
        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Logger.error("Lỗi save clan!\n");
        }
        try {
            ConsignShopManager.gI().save();
        } catch (Exception e) {
            Logger.error("Lỗi save shop ký gửi!\n");
        }
        Client.gI().close();
        EventDAO.save();
        String a = ("SUCCESSFULLY MAINTENANCE!\n");
        Logger.success(a);
        myTelegramBot = new MyTelegramBot();
        myTelegramBot.sendLogMessage(myTelegramBot.chatid, a);
        try {
            String batchFilePath = "run.bat";
            FileRunner.runBatchFile(batchFilePath);
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void close2() {
        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Logger.error("Lỗi save clan!\n");
        }
        try {
            ConsignShopManager.gI().save();
        } catch (Exception e) {
            Logger.error("Lỗi save shop ký gửi!\n");
        }
        Client.gI().close();
        EventDAO.save();
        String a = ("SUCCESSFULLY MAINTENANCE!\n");
        Logger.success("SUCCESSFULLY MAINTENANCE!\n");
        MyTelegramBot bot = new MyTelegramBot();
        bot.sendLogMessage(bot.chatid, a);
        System.exit(0);
    }
    // Phương thức sao lưu cơ sở dữ liệu

    public static void backupDatabase() {
        // Tạo tên file sao lưu với thời gian hiện tại
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFilePath = "backupsql/nro_backup_" + dateTime + ".sql";

        // Lệnh mysqldump để sao lưu cơ sở dữ liệu
        String command = "mysqldump -u root --databases " + NNTDB.DB_DATA + " --password=  --result-file=\"" + backupFilePath + "\"";

        try {
            System.out.println("Đang thực hiện lệnh sao lưu: " + command);

            // Thực thi lệnh mysqldump
            Process process = Runtime.getRuntime().exec(command);

            // Đọc đầu ra của lệnh (nếu có)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Output: " + line);
            }

            // Đọc lỗi nếu có
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            // Kiểm tra kết quả của process
            int processComplete = process.waitFor();
            if (processComplete != 0) {
                System.out.println("Lệnh mysqldump không thành công.");
            } else {
                System.out.println("Sao lưu thành công: " + backupFilePath);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Lỗi khi thực thi lệnh: " + e.getMessage());
        }
    }

    // Phương thức chạy sao lưu tự động
    public void scheduleDatabaseBackup() {
        scheduler.scheduleAtFixedRate(NgocRongOnline::backupDatabase, 0, 30, TimeUnit.MINUTES);
    }

    public static void backupSrcFolder() {
        // Đặt tên file sao lưu với định dạng ngày giờ
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFilePath = "backupsrc/src_backup_" + dateTime + ".zip";

        // Thư mục cần sao lưu
        String srcFolderPath = "src";

        // Tạo file ZIP từ thư mục src
        try (FileOutputStream fos = new FileOutputStream(backupFilePath); ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            Path srcFolder = Paths.get(srcFolderPath);
            Files.walk(srcFolder).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                try {
                    // Tạo entry trong file ZIP cho mỗi file trong thư mục
                    ZipEntry zipEntry = new ZipEntry(srcFolder.relativize(path).toString());
                    zipOut.putNextEntry(zipEntry);
                    Files.copy(path, zipOut);
                    zipOut.closeEntry();
                } catch (IOException e) {
                    System.out.println("Lỗi khi sao lưu file: " + path);
                }
            });

            System.out.println("Sao lưu thành công: " + backupFilePath);
        } catch (IOException e) {
            System.out.println("Lỗi khi sao lưu thư mục: " + e.getMessage());
        }
    }

//    public void mahoaJAR() {
//        try {
//            String jarPath = "dist/MeoNro.jar"; // Đường dẫn tới file .jar gốc
//            String outputJarPath = "dist/MeoNro-mahoa.jar"; // Đường dẫn tới file .jar đã làm rối
//
//            // Set để theo dõi các tên đã được sử dụng để tránh trùng lặp
//            Set<String> existingNames = new HashSet<>();
//
//            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarPath)); ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputJarPath))) {
//
//                ZipEntry entry;
//                while ((entry = zis.getNextEntry()) != null) {
//                    // Thay thế tên file và thư mục thành "l..." và đảm bảo không bị trùng lặp
//                    String obfuscatedName = generateUniqueName(entry.getName(), existingNames);
//                    existingNames.add(obfuscatedName);
//
//                    // Ghi entry đã được làm rối vào file .jar mới
//                    zos.putNextEntry(new ZipEntry(obfuscatedName));
//                    if (!entry.isDirectory()) {
//                        byte[] buffer = zis.readAllBytes();
//                        zos.write(buffer);
//                    }
//                    zos.closeEntry();
//                }
//            }
//
//            System.out.println("File obfuscated successfully: " + outputJarPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    // Hàm thay đổi tên file/thư mục thành chuỗi "l..."
    private static String generateUniqueName(String originalName, Set<String> existingNames) {
        // Thay thế tên gốc bằng chuỗi lặp "l"
        String obfuscatedName = originalName.replaceAll("[a-zA-Z0-9]", "l");

        // Đảm bảo không có tên trùng lặp
        while (existingNames.contains(obfuscatedName)) {
            obfuscatedName = obfuscatedName + "l"; // Thêm ký tự "l" nếu trùng lặp
        }

        return obfuscatedName;
    }

    public static void processPendingTransactions() {
        try (Connection con = NNTDB.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "SELECT t.*, p.account_id\n"
                + "FROM transactions t\n"
                + "JOIN player p ON t.player_name = p.name\n"
                + "WHERE t.status_deposit = TRUE AND t.status_receive = FALSE")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int transactionId = rs.getInt("p.account_id");
                    int id = rs.getInt("t.id");
                    int amount = rs.getInt("t.amount");
                    String playerName = rs.getString("t.player_name");
                    Player pl = null;
                    pl = Client.gI().getPlayer(playerName);
                    // Gọi hàm xử lý
                    PlayerDAO.addcash(transactionId, amount);
                    if (pl != null) {
                        Service.gI().sendThongBao(pl, "Nạp Thành Công: " + Util.numberToMoney(amount) + " VNĐ Vào Tài Khoản");
                        pl.getSession().cash += amount;
                    }
                    PlayerDAO.updatePlayer(pl);
                    updateStatusReceive(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStatusReceive(int transactionId) {
        try (Connection con = NNTDB.getConnectionServer(); PreparedStatement ps = con.prepareStatement(
                "UPDATE transactions SET status_receive = TRUE WHERE id = ?")) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOldUnprocessedTransactions() {
        String selectQuery = "SELECT id, created_at, status_deposit FROM transactions WHERE status_deposit != 1";
        String deleteQuery = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = NNTDB.getConnectionServer(); PreparedStatement selectStmt = conn.prepareStatement(selectQuery); ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                LocalDateTime createdTime = createdAt.toLocalDateTime();
                LocalDateTime currentTime = LocalDateTime.now();

                // Kiểm tra nếu đã quá 10 phút kể từ khi tạo mà status_deposit vẫn chưa thành 1
                if (Duration.between(createdTime, currentTime).toMinutes() >= 10) {
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                        System.out.println("Đã xóa giao dịch ID: " + id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
