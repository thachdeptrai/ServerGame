package MiniGameZ;/*telegram @tomihjhj_bot*/

import DBConnect.NNTDB;/*telegram @tomihjhj_bot*/
import java.util.List;/*telegram @tomihjhj_bot*/
import java.util.concurrent.CopyOnWriteArrayList;/*telegram @tomihjhj_bot*/
import models.Item;/*telegram @tomihjhj_bot*/
import utils.Util;/*telegram @tomihjhj_bot*/
import java.sql.*;/*telegram @tomihjhj_bot*/
import java.sql.PreparedStatement;/*telegram @tomihjhj_bot*/
import npc.Npc;
import npc.NpcManager;
import player.Inventory;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.MapService;
import services.Service;

/**
 *
 * @author kagam
 */
public class AllinOne implements Runnable {

    public int goldNormar;/*telegram @tomihjhj_bot*/
    public int goldVip;/*telegram @tomihjhj_bot*/
    private static final int MAX_PLAYERS = 10;/*telegram @tomihjhj_bot*/
    private static final int REWARD_PERCENTAGE = 80;/*telegram @tomihjhj_bot*/
    private static final long GAME_DURATION_MS = 60000;/*telegram @tomihjhj_bot*/ // 60 giây
    public long lastTimeEnd;/*telegram @tomihjhj_bot*/
    private final List<Player> playersNormar = new CopyOnWriteArrayList<>();/*telegram @tomihjhj_bot*/
    private final List<Player> playersVip = new CopyOnWriteArrayList<>();/*telegram @tomihjhj_bot*/

    private static AllinOne instance;/*telegram @tomihjhj_bot*/

    public static AllinOne gI() {
        if (instance == null) {
            instance = new AllinOne();/*telegram @tomihjhj_bot*/
        }
        return instance;/*telegram @tomihjhj_bot*/
    }

    public synchronized void updateTime() {
        long currentTime = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
        if (lastTimeEnd < currentTime) {
            lastTimeEnd = currentTime + GAME_DURATION_MS;/*telegram @tomihjhj_bot*/
        }
    }

    public synchronized void addPlayerNormar(Player player) {
        // Kiểm tra điều kiện thêm người chơi vào danh sách
        if (playersNormar.size() < MAX_PLAYERS && !playersNormar.contains(player)) {
//            System.out.println("Thêm người chơi vào danh sách: " + player.name);/*telegram @tomihjhj_bot*/ // In ra tên người chơi để kiểm tra
            playersNormar.add(player);/*telegram @tomihjhj_bot*/

            if (player.inventory == null) {
                player.inventory = new Inventory();/*telegram @tomihjhj_bot*/  // Khởi tạo inventory nếu chưa có
            }

            player.status = "playing";/*telegram @tomihjhj_bot*/  // Cập nhật trạng thái là 'playing'
            savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái vào cơ sở dữ liệu
        } else {
//            System.out.println("Không thể thêm người chơi vào danh sách: " + player.name);/*telegram @tomihjhj_bot*/ // In ra nếu không thể thêm
        }
    }

    public synchronized void addPlayerVIP(Player player) {
        // Kiểm tra điều kiện thêm người chơi vào danh sách
        if (playersVip.size() < MAX_PLAYERS && !playersVip.contains(player)) {
//            System.out.println("Thêm người chơi vào danh sách: " + player.name);/*telegram @tomihjhj_bot*/ // In ra tên người chơi để kiểm tra
            playersVip.add(player);/*telegram @tomihjhj_bot*/

            if (player.inventory == null) {
                player.inventory = new Inventory();/*telegram @tomihjhj_bot*/  // Khởi tạo inventory nếu chưa có
            }

            player.status = "playing";/*telegram @tomihjhj_bot*/  // Cập nhật trạng thái là 'playing'
            savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái vào cơ sở dữ liệu
        } else {
//            System.out.println("Không thể thêm người chơi vào danh sách: " + player.name);/*telegram @tomihjhj_bot*/ // In ra nếu không thể thêm
        }
    }

    // Get the count of normal players
    public int getPlayerNormarCount() {
        return playersNormar.size();/*telegram @tomihjhj_bot*/
    }

    // Get the count of VIP players
    public int getPlayerVIPCount() {
        return playersVip.size();/*telegram @tomihjhj_bot*/
    }

    public synchronized void removePlayer(Player player) {
        playersNormar.remove(player);/*telegram @tomihjhj_bot*/
        playersVip.remove(player);/*telegram @tomihjhj_bot*/
        player.goldNormar = 0;/*telegram @tomihjhj_bot*/
        player.goldVIP = 0;/*telegram @tomihjhj_bot*/
        player.status = "notPlaying";/*telegram @tomihjhj_bot*/  // Cập nhật trạng thái là 'notPlaying'
        savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái vào cơ sở dữ liệu
    }
    public static long time;/*telegram @tomihjhj_bot*/

    @Override
    public void run() {
        while (true) {
            try {

                long timeRemaining = lastTimeEnd - System.currentTimeMillis();/*telegram @tomihjhj_bot*/
                if (timeRemaining <= 0) {
                    chooseWinner();/*telegram @tomihjhj_bot*/
                    resetGameData();/*telegram @tomihjhj_bot*/
                }
                // Kiểm tra người chơi đã thoát chưa và cập nhật trạng thái
                for (Player player : playersNormar) {
                    if (player.status.equals("notPlaying")) {
                        savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái khi thoát
                    }
                }
                for (Player player : playersVip) {
                    if (player.status.equals("notPlaying")) {
                        savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái khi thoát
                    }
                }

                Thread.sleep(1000);/*telegram @tomihjhj_bot*/
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();/*telegram @tomihjhj_bot*/
                break;/*telegram @tomihjhj_bot*/ // Thoát vòng lặp nếu bị gián đoạn
            } catch (Exception e) {
                e.printStackTrace();/*telegram @tomihjhj_bot*/
            }
        }
    }

    private void chooseWinner() {
        if (playersNormar.isEmpty() && playersVip.isEmpty()) {
            Service.gI().sendThongBaoAllPlayer("Không có người chơi nào tham gia All in One lần này.");/*telegram @tomihjhj_bot*/
            return;/*telegram @tomihjhj_bot*/
        }
        // Kiểm tra xem chỉ còn một người chơi và họ đã thoát
        if ((playersNormar.size() == 1 && playersNormar.get(0).status.equals("notPlaying"))
                || (playersVip.size() == 1 && playersVip.get(0).status.equals("notPlaying"))) {
            Service.gI().sendThongBaoAllPlayer("Không thể trao thưởng, vì người chơi đã thoát game.");/*telegram @tomihjhj_bot*/
            return;/*telegram @tomihjhj_bot*/
        }
        // Xử lý người chơi thường
        if (!playersNormar.isEmpty()) {
            Player winnerNormar = playersNormar.get(Util.nextInt(0, playersNormar.size() - 1));/*telegram @tomihjhj_bot*/
            if (winnerNormar.status.equals("notPlaying")) {
                Service.gI().sendThongBaoAllPlayer(winnerNormar.name + " đã thoát game, không thể trao thưởng.");/*telegram @tomihjhj_bot*/
            } else {
                distributeNormalReward(winnerNormar);/*telegram @tomihjhj_bot*/
            }
        }
        // Xử lý người chơi VIP
        if (!playersVip.isEmpty()) {
            Player winnerVip = playersVip.get(Util.nextInt(0, playersVip.size() - 1));/*telegram @tomihjhj_bot*/
            if (winnerVip.status.equals("notPlaying")) {
                Service.gI().sendThongBaoAllPlayer(winnerVip.name + " đã thoát game, không thể trao thưởng.");/*telegram @tomihjhj_bot*/
            } else {
                distributeVIPReward(winnerVip);/*telegram @tomihjhj_bot*/
            }
        }
    }

    private void distributeNormalReward(Player winner) {
        if (winner != null && playersNormar.contains(winner)) {
            int rewardGold = goldNormar * 80 / 100;/*telegram @tomihjhj_bot*/  // Thưởng 80% cho người thường
//        ChatGlobalService.gI().chat(winner, winner.name + " đã chiến thắng giải thưởng thường.");/*telegram @tomihjhj_bot*/
            Service.gI().sendThongBao(winner, "Chúc mừng bạn đã chiến thắng và nhận được " + rewardGold + " thỏi vàng (Thường)");/*telegram @tomihjhj_bot*/

            Item it = ItemService.gI().createNewItem((short) 457, rewardGold);/*telegram @tomihjhj_bot*/
            InventoryService.gI().addItemBag(winner, it);/*telegram @tomihjhj_bot*/
            InventoryService.gI().sendItemBag(winner);/*telegram @tomihjhj_bot*/
        }
    }

    private void distributeVIPReward(Player winner) {
        if (winner != null && playersVip.contains(winner)) {
            int rewardGold = goldVip * 90 / 100;/*telegram @tomihjhj_bot*/  // Thưởng 90% cho người VIP
//        ChatGlobalService.gI().chat(winner, winner.name + " đã chiến thắng giải thưởng VIP.");/*telegram @tomihjhj_bot*///chat thế giới
            Service.gI().sendThongBao(winner, "Chúc mừng bạn đã chiến thắng và nhận được " + rewardGold + " thỏi vàng (VIP)");/*telegram @tomihjhj_bot*/

            Item it = ItemService.gI().createNewItem((short) 457, rewardGold);/*telegram @tomihjhj_bot*/
            InventoryService.gI().addItemBag(winner, it);/*telegram @tomihjhj_bot*/
            InventoryService.gI().sendItemBag(winner);/*telegram @tomihjhj_bot*/
        }
    }

    private void resetGameData() {
        playersNormar.forEach(player -> {
            player.status = "notPlaying";/*telegram @tomihjhj_bot*/
            player.goldNormar = 0;/*telegram @tomihjhj_bot*/
            savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái vào cơ sở dữ liệu
        });/*telegram @tomihjhj_bot*/
        playersVip.forEach(player -> {
            player.status = "notPlaying";/*telegram @tomihjhj_bot*/
            player.goldVIP = 0;/*telegram @tomihjhj_bot*/
            savePlayerData(player);/*telegram @tomihjhj_bot*/  // Lưu trạng thái vào cơ sở dữ liệu
        });/*telegram @tomihjhj_bot*/
        playersNormar.clear();/*telegram @tomihjhj_bot*/
        playersVip.clear();/*telegram @tomihjhj_bot*/
        goldNormar = 0;/*telegram @tomihjhj_bot*/
        goldVip = 0;/*telegram @tomihjhj_bot*/
        lastTimeEnd = System.currentTimeMillis() + GAME_DURATION_MS;/*telegram @tomihjhj_bot*/
        announceRemainingTime();/*telegram @tomihjhj_bot*/

    }

    public void startGame() {
        lastTimeEnd = System.currentTimeMillis() + GAME_DURATION_MS;/*telegram @tomihjhj_bot*/
        Thread gameThread = new Thread(this);/*telegram @tomihjhj_bot*/
        gameThread.start();/*telegram @tomihjhj_bot*/
        announceRemainingTime();/*telegram @tomihjhj_bot*/

    }

    // Kết nối cơ sở dữ liệu và lưu dữ liệu người chơi
    private void savePlayerData(Player player) {
        String query = "INSERT INTO all_in_one (player_id, gold_normar, gold_vip, status) "
                + "VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE gold_normar = ?, gold_vip = ?, status = ?, last_update = CURRENT_TIMESTAMP";/*telegram @tomihjhj_bot*/

        try (Connection conn = NNTDB.getConnectionServer();/*telegram @tomihjhj_bot*/ PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, (int) player.id);/*telegram @tomihjhj_bot*/  // player_id
            stmt.setInt(2, (int) player.goldNormar);/*telegram @tomihjhj_bot*/  // gold_normar
            stmt.setInt(3, (int) player.goldVIP);/*telegram @tomihjhj_bot*/  // gold_vip
            stmt.setString(4, player.status);/*telegram @tomihjhj_bot*/  // status
            stmt.setInt(5, (int) player.goldNormar);/*telegram @tomihjhj_bot*/  // gold_normar (cập nhật nếu đã có)
            stmt.setInt(6, (int) player.goldVIP);/*telegram @tomihjhj_bot*/  // gold_vip (cập nhật nếu đã có)
            stmt.setString(7, player.status);/*telegram @tomihjhj_bot*/  // status (cập nhật nếu đã có)

            stmt.executeUpdate();/*telegram @tomihjhj_bot*/  // Thực thi câu lệnh

        } catch (SQLException e) {
            e.printStackTrace();/*telegram @tomihjhj_bot*/
        }
    }

    public void loadPlayerData(Player player) {
        String query = "SELECT gold_normar, gold_vip, status FROM all_in_one WHERE player_id = ?";/*telegram @tomihjhj_bot*/

        try (Connection conn = NNTDB.getConnectionServer();/*telegram @tomihjhj_bot*/ PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, (int) player.id);/*telegram @tomihjhj_bot*/  // player_id

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    player.goldNormar = rs.getInt("gold_normar");/*telegram @tomihjhj_bot*/
                    player.goldVIP = rs.getInt("gold_vip");/*telegram @tomihjhj_bot*/
                    player.status = rs.getString("status");/*telegram @tomihjhj_bot*/
                }
            }

            if (player.inventory == null) {
                player.inventory = new Inventory();/*telegram @tomihjhj_bot*/  // Khởi tạo nếu chưa có
            }
        } catch (SQLException e) {
            e.printStackTrace();/*telegram @tomihjhj_bot*/
        }
    }

    public void announceRemainingTime() {
        Npc npc = NpcManager.getNpc((byte) 78);/*telegram @tomihjhj_bot*/
        new Thread(() -> {
            while (true) {
                try {
                    long timeRemaining = (lastTimeEnd - System.currentTimeMillis()) / 1000;/*telegram @tomihjhj_bot*/ // Thời gian còn lại tính bằng giây
                    if (timeRemaining <= 0) {
                        String text = ("Phiên All in One đã kết thúc!");/*telegram @tomihjhj_bot*/

                        List<Player> players = MapService.gI().getAllPlayerInMap(5);/*telegram @tomihjhj_bot*/
                        if (npc != null) {

                            for (Player pl : players) {
                                npc.npcChat(pl, text);/*telegram @tomihjhj_bot*/
                            }
                        }

                        break;/*telegram @tomihjhj_bot*/ // Kết thúc thông báo khi hết thời gian
                    }
                    String text = ("Còn " + timeRemaining + " giây nữa sẽ kết thúc phiên All in One.");/*telegram @tomihjhj_bot*/
                    List<Player> players = MapService.gI().getAllPlayerInMap(5);/*telegram @tomihjhj_bot*/
                    if (npc != null) {
                        for (Player pl : players) {
                            npc.npcChat(pl, text);/*telegram @tomihjhj_bot*/
                        }
                    }
                    Thread.sleep(1000);/*telegram @tomihjhj_bot*/ // Thông báo mỗi 5 giây
                } catch (InterruptedException e) {
                    System.out.println("Thông báo thời gian bị gián đoạn: " + e.getMessage());/*telegram @tomihjhj_bot*/
                    break;/*telegram @tomihjhj_bot*/
                }
            }
        }).start();/*telegram @tomihjhj_bot*/
    }

}
