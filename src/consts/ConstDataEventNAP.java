package consts;

import BotTelegram.MyTelegramBot;
import utils.Functions;
import models.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import services.ItemService;
import services.Service;
import DBConnect.NNTDB;

import JDBCMysql.NNTSqlFetcher;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import player.Player;
import utils.Logger;
import DBConnect.NTTResultSet;
import NgocThachServer.NgocRongOnline;

// su kien 1/6
public class ConstDataEventNAP {

    public static ConstDataEventNAP gI;

    public static ConstDataEventNAP gI() {
        if (gI == null) {
            gI = new ConstDataEventNAP();
        }
        return gI;
    }

    public static boolean isEventActive() {
        return false;
    }

    public static boolean isTraoQua = true;

    public static Calendar startEvent;

    public static Calendar endEvents;

    public static boolean initsukien = false;

    public static final byte MONTH_OPEN = 02;
    public static final byte DATE_OPEN = 01;
    public static final byte HOUR_OPEN = 00;
    public static final byte MIN_OPEN = 00;

    public static final byte MONTH_END = 01;
    public static final byte DATE_END = 03;
    public static final byte HOUR_END = 00;
    public static final byte MIN_END = 00;

    public static boolean isActiveEvent() {
        if (!initsukien) {
            initsukien = true;
            startEvent = Calendar.getInstance();

            // Thiết lập ngày và giờ bắt đầu
            startEvent.set(2025, MONTH_OPEN - 1, DATE_OPEN, HOUR_OPEN, MIN_OPEN);
            System.out.println("Ngày bắt đầu sự kiện đua top nạp: " + startEvent.getTime());

            endEvents = Calendar.getInstance();
            // Thiết lập ngày và giờ kết thúc
            endEvents.set(2025, MONTH_END - 1, DATE_END, HOUR_END, MIN_END);
            System.out.println("Ngày kết thúc sự kiện đua top nap: " + endEvents.getTime());
        }

        Calendar currentTime = Calendar.getInstance();
        if (System.currentTimeMillis() >= startEvent.getTimeInMillis() && System.currentTimeMillis() <= endEvents.getTimeInMillis()) {
            if (isTraoQua && System.currentTimeMillis() + 60000 >= endEvents.getTimeInMillis()) {
                String sql = "SELECT  player.id as plid, player.name as name, account.danap FROM account, player WHERE account.id = player.account_id AND account.create_time > '2025-" + MONTH_OPEN + "-" + DATE_OPEN + " " + HOUR_OPEN + ":" + MIN_OPEN + ":00' AND account.danap >= 100000 ORDER BY account.danap DESC LIMIT 10;";
                List<Integer> AccIdPlayer = new ArrayList<>();
                NTTResultSet rs = null;
                try {
                    rs = NNTDB.executeQuery(sql);
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        AccIdPlayer.add(id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < AccIdPlayer.size(); i++) {
                    Player player = NNTSqlFetcher.loadById(AccIdPlayer.get(i));
                    TraoQuaSuKien(player, i + 1);
                    String b = ("Đã trao quà nạp top " + (i + 1) + " cho: " + player.name + "\n");
                    Logger.error(b);
                    try {
                        //Thread.sleep(100);
                        Functions.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isTraoQua = false;
            }
            return true;
        } else {

            return false;
        }
    }

    public static boolean isRunningSK = isActiveEvent();

    public static void TraoQuaSuKien(Player pl, int rank) {
        Item item = null;
        JSONArray dataArray;
        JSONObject dataObject;
        try (Connection con2 = NNTDB.getConnectionServer(); PreparedStatement ps = con2.prepareStatement("SELECT detail FROM moc_nap_top WHERE id = ?")) {
            // Sử dụng rank (thứ hạng) để truy vấn phần thưởng tương ứng
            ps.setInt(1, rank);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("detail"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        int tempid = Integer.parseInt(String.valueOf(dataObject.get("temp_id")));
                        int quantity = Integer.parseInt(String.valueOf(dataObject.get("quantity")));
                        item = ItemService.gI().createNewItem((short) tempid);
                        item.quantity = quantity;
                        JSONArray optionsArray = (JSONArray) dataObject.get("options");
                        for (int j = 0; j < optionsArray.size(); j++) {
                            JSONObject optionObject = (JSONObject) optionsArray.get(j);
                            int param = Integer.parseInt(String.valueOf(optionObject.get("param")));
                            int optionId = Integer.parseInt(String.valueOf(optionObject.get("id")));
                            item.itemOptions.add(new Item.ItemOption(optionId, param));
                        }
                        pl.inventory.itemsMailBox.add(item);
                    }
                    if (NNTSqlFetcher.updateMailBox(pl)) {
                        Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
