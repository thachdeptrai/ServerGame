package NgocThachServer;

import DBConnect.NNTDB;
import FunC.TopService;
import JDBCMysql.ShopDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;
import Manager.ConsignShopManager;
import Manager.NNTManager;
import MapZ.EffectMap;
import MapZ.WayPoint;
import static NgocThachServer.Manager.MAP_TEMPLATES;
import static NgocThachServer.Manager.NOTIFY;
import consts.ConstSQL;
import java.awt.Button;
import network.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import models.Template;
import network.Network;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import services.ClanService;
import utils.FileRunner;
import utils.Logger;

public class ServerManagerUI extends JFrame {

    public JButton loadButton1, loadButton2, loadButton3, loadButton4, loadButton5;
    public Preferences preferences;
    public JLabel ssCountLabel;
    public JLabel plCountLabel;
    public JLabel threadCountLabel;
    public JLabel threadCountLabel2;
    public JLabel threadCountLabel3;
    public JTextField minutesField;
    public JLabel messageLabel;
    public JLabel messageLabe2;
    public JLabel countdownLabel;
    public Timer countdownTimer;
    public int remainingSeconds;
    public ButtonGroup maintenanceGroup;
    public boolean autoMaintenance;
    public JCheckBox maintenanceOption1;
    public JCheckBox maintenanceOption2;
    public JLabel info;
    //
    private JLabel delayLabel;
    private JLabel intervalLabel;
    private JButton increaseDelayButton;
    private JButton decreaseDelayButton;
    private JButton increaseIntervalButton;
    private JButton decreaseIntervalButton;
    private NNTManager autoSaveManager = NNTManager.getInstance();
    public JComboBox<String> dataTypeComboBox;
    public JButton saveButton;

    private static ServerManagerUI instance;

    public static ServerManagerUI gI() {
        if (instance == null) {
            instance = new ServerManagerUI();
        }
        return instance;
    }

    private JComboBox<Integer> createComboBox(int min, int max, int selectedValue) {
        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
        for (int i = min; i <= max; i++) {
            model.addElement(i);
        }
        JComboBox<Integer> comboBox = new JComboBox<>(model);
        comboBox.setSelectedItem(selectedValue);
        return comboBox;
    }

    private void scheduleMaintenance(JComboBox<Integer> hoursComboBox, JComboBox<Integer> minutesComboBox, JComboBox<Integer> secondsComboBox) {
        int hours = hoursComboBox.getItemAt(hoursComboBox.getSelectedIndex());
        int minutes = minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex());
        int seconds = secondsComboBox.getItemAt(secondsComboBox.getSelectedIndex());
        String configFilePath = "data/config/maintenanceConfig.properties";

        if (hours == -1 || minutes == -1 || seconds == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng thời gian");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("hours=" + hours + "\n");
            writer.write("mins=" + minutes + "\n");
            writer.write("seconds=" + seconds + "\n");
            writer.write("AutoMaintenance=" + autoMaintenance + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        info.setText("Đã cài đặt quá trình bảo trì tự động vào lúc " + hours + ":" + minutes + ":" + seconds);
        Logger.error("Đã cài đặt quá trình bảo trì tự động vào lúc " + hours + ":" + minutes + ":" + seconds + "\n");
        AtomicBoolean timeReached = new AtomicBoolean(false);
        new Thread(() -> {
            while (!timeReached.get()) {
                try {
                    LocalTime currentTime = LocalTime.now();
                    if (hours == currentTime.getHour() && minutes == currentTime.getMinute() && seconds == currentTime.getSecond()) {
                        performMaintenance();
                        timeReached.set(true);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public ServerManagerUI() {
        preferences = Preferences.userNodeForPackage(ServerManagerUI.class);
        setTitle("Chương trình Bảo trì server " + NgocRongOnline.gI().NAME);
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        JButton saveButton = new JButton("Lưu Data");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.error("Saving data in progress\n");
                try {
                    Logger.success("saving clan data in progress\n");
                    ClanService.gI().close();
                    Thread.sleep(1000);
                    Logger.success("clan data saved successfully\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error("clan data saved error\n");
                }
                try {
                    Logger.success("saving kygui data in progress\n");
                    ConsignShopManager.gI().save();
                    Thread.sleep(1000);
                    Logger.success("kygui data saved successfully\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error("kygui data saved error\n");
                }
                try {
                    Logger.success("Pushing player in progress\n");
                    Client.gI().close();
                    Thread.sleep(1000);
                    Logger.success("User data saved successfully\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.error("Error saving user data\n");
                }
//                System.exit(0);
            }
        });
        panel.add(saveButton);
        // Thêm 4 nút Load với công dụng khác nhau
        loadButton1 = new JButton("Load Top ");

        loadButton1.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                Manager.timeRealTop = System.currentTimeMillis();
                try (Connection con = NNTDB.getConnectionServer()) {
                    Manager.topNV = Manager.realTop(ConstSQL.TOP_NV, con);
//                    Manager.topDC = Manager.realTop(ConstSQL.TOP_DC, con);
                    Manager.topVDST = Manager.realTop(ConstSQL.TOP_VDST, con);
                    Manager.topWHIS = Manager.realTop(ConstSQL.TOP_WHIS, con);
                    Manager.topSM = Manager.realTop(ConstSQL.TOP_SM, con);
                    Manager.topNap = Manager.realTop(ConstSQL.TOP_NAP, con);
                    Manager.topHongNgoc = Manager.realTop(ConstSQL.TOP_HONGNGOC, con);
//                Manager.topDuaSM = Manager.realTop(ConstSQL.TOP_DUA_SM, con);
//                Manager.topDuaNap = Manager.realTop(ConstSQL.TOP_DUA_NAP, con);
                } catch (Exception ignored) {
                    Logger.error("Lỗi đọc top");
                }
                System.out.println("Load Top is executed");
                // Bạn có thể thay đổi phần này để thực hiện hành động mong muốn
            }
        }
        );
        panel.add(loadButton1);

        loadButton2 = new JButton("Load giftcode ");

        loadButton2.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                Manager.gI().loadGiftcode();
                System.out.println("Load Giftcode is executed");
            }
        }
        );
        panel.add(loadButton2);

        // Tạo nút chính
        loadButton3 = new JButton("Backup SQL & Src");

// Tạo popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem backupSqlMenuItem = new JMenuItem("Backup SQL");
        JMenuItem backupSrcMenuItem = new JMenuItem("Backup Src");

// Thêm các tùy chọn vào popup menu
        popupMenu.add(backupSqlMenuItem);
        popupMenu.add(backupSrcMenuItem);

// Thêm hành động cho từng menu item
        backupSqlMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NgocRongOnline.backupDatabase();
                Logger.success("Backup SQL is executed\n");
            }
        });

        backupSrcMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NgocRongOnline.backupSrcFolder();
                Logger.success("Backup Src is executed\n");
            }
        });

// Gắn sự kiện hiển thị popup menu vào nút chính
        loadButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.show(loadButton3, loadButton3.getWidth() / 2, loadButton3.getHeight() / 2);
            }
        });

// Thêm nút vào panel
        panel.add(loadButton3);

        loadButton4 = new JButton("\nLoad Map");

        loadButton4.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                ServerManagerUI.loadMap();
//                System.out.println("Load Action 4 is executed");
            }
        }
        );
        panel.add(loadButton4);

        loadButton5 = new JButton("\nThông Báo Bảo trì Sau 30p");

        loadButton5.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                int phut = 30;
                Maintenance.gI().baotriNew(phut);
                System.err.println("Đag bắt đầu bảo trì sau " + phut + " phút");
            }
        }
        );
        panel.add(loadButton5);
        JButton b9 = new JButton("xem nhân vật\n olnine");
        b9.setBounds(30, 380, 140, 30);
        b9.setActionCommand("xem");
        panel.add(b9);
        b9.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                if (e.getActionCommand().equals("xem")) {
                    OnlinePlayersFrame.display();
                }
//                System.out.println("Load Action 4 is executed");
            }
        }
        );
        saveButton = new JButton("Load shop");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Manager.gI().updateShop();
            }
        });
        panel.add(saveButton);

        JButton maintenanceButton = new JButton("Bảo trì");
        maintenanceButton.addActionListener(e -> showMaintenanceDialog());
        panel.add(maintenanceButton);

        JLabel jLabel2 = new JLabel("\nCài đặt giờ bảo trì");
        panel.add(jLabel2);
        info = new JLabel("");
        String configFilePath = "data/config/maintenanceConfig.properties";

        // Đọc giá trị từ tệp tin
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            Properties properties = new Properties();
            properties.load(reader);

            int hours = Integer.parseInt(properties.getProperty("hours", "-1"));
            int minutes = Integer.parseInt(properties.getProperty("mins", "-1"));
            int seconds = Integer.parseInt(properties.getProperty("seconds", "-1"));
            autoMaintenance = Boolean.parseBoolean(properties.getProperty("AutoMaintenance", "false"));

            info.setText("Giờ: " + hours + ", Phút: " + minutes + ", Giây: " + seconds + " AutoMaintenance: " + autoMaintenance);

            DefaultComboBoxModel<Integer> hoursModel = new DefaultComboBoxModel<>();
            for (int i = -1; i < 24; i++) {
                hoursModel.addElement(i);
            }
            JComboBox<Integer> hoursComboBox = new JComboBox<>(hoursModel);
            panel.add(hoursComboBox);
            hoursComboBox.setSelectedItem(hours);

            DefaultComboBoxModel<Integer> minutesModel = new DefaultComboBoxModel<>();
            for (int i = -1; i < 60; i++) {
                minutesModel.addElement(i);
            }
            JComboBox<Integer> minutesComboBox = new JComboBox<>(minutesModel);
            panel.add(minutesComboBox);
            minutesComboBox.setSelectedItem(minutes);

            DefaultComboBoxModel<Integer> secondsModel = new DefaultComboBoxModel<>();
            for (int i = -1; i < 60; i++) {
                secondsModel.addElement(i);
            }
            JComboBox<Integer> secondsComboBox = new JComboBox<>(secondsModel);
            panel.add(secondsComboBox);
            secondsComboBox.setSelectedItem(seconds);

            JButton scheduleButton2 = new JButton("Hẹn giờ bảo trì");
            scheduleButton2.addActionListener(e -> scheduleMaintenance(hoursComboBox, minutesComboBox, secondsComboBox));
            panel.add(scheduleButton2);

            JCheckBox autoMaintenanceCheckBox = new JCheckBox("\nBật AutoMaintenance", autoMaintenance);
            autoMaintenanceCheckBox.addActionListener(e -> {
                autoMaintenance = autoMaintenanceCheckBox.isSelected();
                saveAutoMaintenanceSetting();
            });
            panel.add(autoMaintenanceCheckBox);

            if (autoMaintenance && hours != -1 && minutes != -1 && seconds != -1) {
                scheduleMaintenance(hoursComboBox, minutesComboBox, secondsComboBox);
            }
            delayLabel = new JLabel("Autosave delay (giây): " + autoSaveManager.getAutoSaveDelayInSeconds()+"\n", JLabel.CENTER);
            panel.add(delayLabel);
            
            // Tạo các nút để tăng giảm delay và interval
            increaseDelayButton = new JButton("+1 giây");
            decreaseDelayButton = new JButton("-1 giây");

            // Đưa các nút vào panel
            panel.add(increaseDelayButton);
            panel.add(decreaseDelayButton);

            // Xử lý sự kiện cho các nút
            increaseDelayButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    autoSaveManager.increaseAutoSaveDelay();
                    updateAutoSaveLabels();
                }
            });
            decreaseDelayButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    autoSaveManager.decreaseAutoSaveDelay();
                    updateAutoSaveLabels();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageLabel = new JLabel();
        panel.add(messageLabel);
        countdownLabel = new JLabel();
        panel.add(countdownLabel);
        panel.add(info);
        threadCountLabel = new JLabel("Số Thread : ");
        panel.add(threadCountLabel);
        plCountLabel = new JLabel("Online :");
        panel.add(plCountLabel);
        ssCountLabel = new JLabel("Session :");
        panel.add(ssCountLabel);

        ScheduledExecutorService threadCountExecutor = Executors.newSingleThreadScheduledExecutor();
        threadCountExecutor.scheduleAtFixedRate(() -> {
            int threadCount = Thread.activeCount();

            threadCountLabel.setText(
                    "\nSố thread: " + threadCount);
        }, 1, 1, TimeUnit.SECONDS);

        ScheduledExecutorService plCountExecutor = Executors.newSingleThreadScheduledExecutor();
        plCountExecutor.scheduleAtFixedRate(() -> {
            int plcount = Client.gI().getPlayers().size();
            plCountLabel.setText("Online : " + plcount);
        }, 5, 1, TimeUnit.SECONDS);

        ScheduledExecutorService ssCountExecutor = Executors.newSingleThreadScheduledExecutor();
        ssCountExecutor.scheduleAtFixedRate(() -> {
            int sscount = SessionManager.gI().getSessions().size();
            ssCountLabel.setText("Session : " + sscount);
        }, 5, 1, TimeUnit.SECONDS);

        setVisible(true);
        messageLabel.setText("Server đang chạy tại port: " + NgocRongOnline.gI().PORT);

        autoSaveManager.startAutoSave();
    }
    // Cập nhật lại nhãn AutoSave
    private void updateAutoSaveLabels() {
        delayLabel.setText("Thời gian delay (giây): " + autoSaveManager.getAutoSaveDelayInSeconds());
    }

    private void showMaintenanceDialog() {
        try {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Bắt đầu bảo trì?", "Bảo trì", dialogButton);
            if (dialogResult == 0) {
                Logger.error("Server tiến hành bảo trì");
                Maintenance.gI().start(15);

            } else {
//                System.out.println("No Option");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void performMaintenance() {
        Logger.error("Maintenance in progress\n");
        Network.gI().stopConnect();
        NgocRongOnline.isRunning = false;
        try {
            Logger.success("Guild maintenance in progress");
            ClanService.gI().close();
            Thread.sleep(1000);
            Logger.success("Guild maintenance successful\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("Guild maintenance error \n");
        }
        try {
            Logger.success("Consignment maintenance in progress\n");
            ConsignShopManager.gI().save();
            Thread.sleep(1000);
            Logger.success("Successful deposit maintenance\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("Consignment maintenance error\n");
        }
        try {
            Logger.success("Player maintenance in progress\n");
            Client.gI().close();
            Thread.sleep(1000);
            Logger.success("Successful Player Maintenance\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error("Player maintenance error\n");
        }
        //sleep
        try {
            Logger.error("Pause 1 minute before restarting the server...\n");
            Thread.sleep(60000); // Dừng 60,000 milliseconds (1 phút)
            Logger.success("Timeout ends, server restart begins.\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.error("Error when stopping timeout\n");
        }
        // Khởi động file run.bat
        try {
            String batFilePath = "run.bat";  //  đường dẫn đúng tới file run.bat 
            FileRunner.runBatchFile(batFilePath);
            Logger.success("Restarted server from run.bat successfully.\n");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Error when restarting run.bat file\n");
        }
        System.exit(0);
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void saveAutoMaintenanceSetting() {
        String configFilePath = "data/config/maintenanceConfig.properties";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("AutoMaintenance=" + autoMaintenance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMap() {
        JSONArray dataArray;
        JSONObject dataObject;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = NNTDB.getConnectionServer()) {
            ps = con.prepareStatement("select count(id) from map_template");
            rs = ps.executeQuery();
            if (rs.next()) {
                int countRow = rs.getShort(1);
                MAP_TEMPLATES = new Template.MapTemplate[countRow];
                ps = con.prepareStatement("select * from map_template");
                rs = ps.executeQuery();
                short i = 0;
                while (rs.next()) {
                    Template.MapTemplate mapTemplate = new Template.MapTemplate();
                    int mapId = rs.getInt("id");
                    String mapName = rs.getString("name");
                    mapTemplate.id = mapId;
                    mapTemplate.name = mapName;
                    mapTemplate.type = rs.getByte("type");
                    mapTemplate.planetId = rs.getByte("planet_id");
                    mapTemplate.bgType = rs.getByte("bg_type");
                    mapTemplate.tileId = rs.getByte("tile_id");
                    mapTemplate.bgId = rs.getByte("bg_id");
                    mapTemplate.zones = rs.getByte("zones");
                    mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
                    //load waypoints
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("waypoints")
                            .replaceAll("\\[\"\\[", "[[")
                            .replaceAll("\\]\"\\]", "]]")
                            .replaceAll("\",\"", ",")
                    );
                    for (int j = 0; j < dataArray.size(); j++) {
                        WayPoint wp = new WayPoint();
                        JSONArray dtwp = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
                        wp.name = String.valueOf(dtwp.get(0));
                        wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
                        wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
                        wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
                        wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
                        wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
                        wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
                        wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
                        wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
                        wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
                        mapTemplate.wayPoints.add(wp);
                        dtwp.clear();
                    }
                    dataArray.clear();
                    //load mobs
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("mobs").replaceAll("\\\"", ""));
                    mapTemplate.mobTemp = new byte[dataArray.size()];
                    mapTemplate.mobLevel = new byte[dataArray.size()];
                    mapTemplate.mobHp = new long[dataArray.size()];
                    mapTemplate.mobX = new short[dataArray.size()];
                    mapTemplate.mobY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtm = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
                        mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
                        mapTemplate.mobHp[j] = Long.parseLong(String.valueOf(dtm.get(2)));
                        mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
                        mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
                        dtm.clear();
                    }
                    dataArray.clear();
                    //load npcs
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("npcs").replaceAll("\\\"", ""));
                    mapTemplate.npcId = new byte[dataArray.size()];
                    mapTemplate.npcX = new short[dataArray.size()];
                    mapTemplate.npcY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtn = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
                        mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
                        mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
                        dtn.clear();
                    }
                    dataArray.clear();
                    // load eff
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("effect"));
                    EffectMap em = new EffectMap();
                    em.setKey("beff");
                    em.setValue("15");
                    mapTemplate.effectMaps.add(em);

                    dataArray.clear();
                    MAP_TEMPLATES[i++] = mapTemplate;
                }
                Logger.success("Successfully loaded map template (" + MAP_TEMPLATES.length + ")\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
