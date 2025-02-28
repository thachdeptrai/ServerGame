package Manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import JDBCMysql.PlayerDAO;
import SessionZ.NNTSessionManager;

import NgocThachServer.Client;
import NgocThachServer.Maintenance;
import utils.Logger;
import utils.TimeUtil;

/**
 *
 * @author NgocThach
 */
public class NNTManager {

    private static NNTManager instance = null;

    public static synchronized NNTManager getInstance() {
        if (instance == null) {
            instance = new NNTManager();
        }
        return instance;
    }
    private ScheduledExecutorService scheduler;
    private int autoSaveDelayInSeconds = 1;  // Thời gian delay ban đầu là 1 giây (mặc định lưu mỗi giây)

    public void startAutoSave() {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                handleAutoSave();
            } catch (Exception e) {
                System.out.println("[AutoSaveManager] start autosave error: " + e.getLocalizedMessage());
            }
        }, autoSaveDelayInSeconds,autoSaveDelayInSeconds , TimeUnit.SECONDS);
    }

    public void handleAutoSave() {
        Client.gI().getPlayers().forEach(player -> {
            long st = System.currentTimeMillis();
            PlayerDAO.updatePlayer(player);
//            Logger.success(TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin() + "m: Tự động lưu dữ liệu người chơi thành công! " + (System.currentTimeMillis() - st) + "ms\n");
        });
    }

    public void increaseAutoSaveDelay() {
        autoSaveDelayInSeconds += 1;  
        restartAutoSave();  
    }

    public void decreaseAutoSaveDelay() {
        autoSaveDelayInSeconds = Math.max(1, autoSaveDelayInSeconds - 1);    
        restartAutoSave();  
    }
    private void restartAutoSave() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        startAutoSave(); 
    }

    public int getAutoSaveDelayInSeconds() {
        return autoSaveDelayInSeconds;
    }
}
