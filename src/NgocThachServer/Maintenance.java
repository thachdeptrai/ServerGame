package NgocThachServer;

/*
 *
 *
 * @author NgocThach
 */
import utils.Functions;
import services.Service;
import utils.Logger;

public class Maintenance extends Thread {

    public static boolean isRunning = false;

    private static Maintenance i;

    private int time;

    private Maintenance() {

    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int min) {
        if (!isRunning) {
            isRunning = true;
            this.time = min;
            this.start();
        }
    }

    public void startNew(int min) {
        if (!isRunning) {
            isRunning = true;
            this.time = min;
            new Thread(Maintenance.gI(), "Thread Bảo Trì").start();
        }
    }

    public void startImmediately() {
        if (!isRunning) {
            isRunning = true;
            Logger.log(Logger.YELLOW, "BEGIN MAINTENANCE\n");
            NgocRongOnline.gI().close();
        }
    }

    public void baotriNew(int phut) {
        this.time = phut * 60;
        while (this.time > 0) {
            String message = "";

            // Thông báo mỗi phút
            if (this.time == 60) {
                message = "Hệ thống sẽ bảo trì sau 1 phút nữa. Hãy thoát game ngay để tránh mất mát vật phẩm.";
            } else if (this.time < 30) {
                // Thông báo cho mỗi giây còn lại dưới 60
                message = "Hệ thống sẽ bảo trì sau " + this.time + " giây nữa.";
            } else {
                // Thông báo theo giờ, phút, giây
                int hours = this.time / 3600;
                int minutes = (this.time % 3600) / 60;
                int seconds = this.time % 60;

                String hourStr = (hours > 0) ? hours + " giờ " : "";
                String minuteStr = (minutes > 0) ? minutes + " phút " : "";
                String secondStr = (seconds > 0) ? seconds + " giây " : "";

                message = "Hệ thống sẽ bảo trì sau " + hourStr + minuteStr + secondStr + "nữa.";
            }

            // Gửi thông báo đến tất cả người chơi
            Service.gI().sendThongBaoAllPlayer(message);
            Logger.log(Logger.YELLOW, message);

            // Tính toán thời gian chờ tiếp theo
            int sleepTime;
            if (this.time <= 30) {
                // Nếu còn dưới 30 giây, gọi liên tục mỗi giây
                sleepTime = 1;
            } else {
                // Nếu trên 1 phút, gửi thông báo mỗi phút
                sleepTime = 60;
            }

            try {
                Functions.sleep(sleepTime * 1000); // Chờ trong khoảng thời gian tính toán
            } catch (Exception e) {
                Logger.error("Lỗi khi chờ thời gian bảo trì: " + e.getMessage());
            }

            // Giảm thời gian còn lại
            this.time -= sleepTime;
        }

        // Kết thúc bảo trì
        Logger.log(Logger.YELLOW, "BEGIN MAINTENANCE\n");
        NgocRongOnline.gI().close2();
    }

    public void baotri(int timeInSeconds) {
        this.time = timeInSeconds;
        while (this.time > 0) {
            String message;
            if (this.time == 60) {
                message = "Hệ thống sẽ bảo trì sau 1 phút nữa. Hãy thoát game ngay để tránh mất mát vật phẩm.";
            } else if (this.time < 60) {
                message = "Hệ thống sẽ bảo trì sau " + this.time + " giây nữa.";
            } else {
                int hours = this.time / 3600;
                int minutes = (this.time % 3600) / 60;
                int seconds = this.time % 60;

                String hourStr = (hours > 0) ? hours + " giờ " : "";
                String minuteStr = (minutes > 0) ? minutes + " phút " : "";
                String secondStr = (seconds > 0) ? seconds + " giây " : "";

                message = "Hệ thống sẽ bảo trì sau " + hourStr + minuteStr + secondStr + "nữa.";
            }

            // Gửi thông báo đến tất cả người chơi
            Service.gI().sendThongBaoAllPlayer(message);
            Logger.log(Logger.YELLOW, message);

            // Tính toán khoảng thời gian chờ tiếp theo
            int sleepTime = (this.time >= 60) ? 60 : this.time; // Giảm 60 giây hoặc số giây còn lại
            try {
                Functions.sleep(sleepTime * 1000); // Chờ trong khoảng thời gian tính toán
            } catch (Exception e) {
                Logger.error("Lỗi khi chờ thời gian bảo trì: " + e.getMessage());
            }

            // Giảm thời gian còn lại
            this.time -= sleepTime;
        }

        // Kết thúc bảo trì
        Logger.log(Logger.YELLOW, "BEGIN MAINTENANCE\n");
        NgocRongOnline.gI().close2();
    }

    @Override
    public void run() {
        while (this.time > 0) {
            if (this.time == 60) {
                Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau 1 phút nữa hãy thoát game ngay để tránh mất mát vật phẩm.");
                try {
                    Functions.sleep(1000);
                } catch (Exception e) {
                }
                this.time--;
            } else if (time < 60) {
                Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + time + " giây nữa");
                try {
                    Functions.sleep(1000);
                } catch (Exception e) {
                }
                this.time--;
            } else {
                int hour = this.time / 3600;
                int min = (this.time - hour * 3600) / 60;
                int sec = this.time % 60;

                String hourStr = (hour > 0) ? hour + " giờ " : "";
                String minStr = (min > 0) ? min + " phút " : "";
                String secStr = (sec > 0) ? sec + " giây " : "";

                Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + hourStr + minStr + secStr
                        + "nữa");
                Logger.log(Logger.YELLOW, "Hệ thống sẽ bảo trì sau " + hourStr + minStr + secStr
                        + "nữa\n");
                if (sec == 0 && this.time > 60) {
                    sec = 60;
                } else if (sec == 0) {
                    sec = 1;
                }
                this.time -= sec;
                try {
                    Functions.sleep(sec * 1000);
                } catch (Exception e) {
                }
            }
        }
        Logger.log(Logger.YELLOW, "BEGIN MAINTENANCE\n");
        NgocRongOnline.gI().close();
    }

}
