package Bot;/*telegram @tomihjhj_bot*/

import java.util.ArrayList;/*telegram @tomihjhj_bot*/
import java.util.List;/*telegram @tomihjhj_bot*/
import  NgocThachServer.NgocRongOnline;/*telegram @tomihjhj_bot*/

public class BotManager implements Runnable {

    public static BotManager i;/*telegram @tomihjhj_bot*/
    
    public List<Bot> bot =  new ArrayList<>();/*telegram @tomihjhj_bot*/
    
    
    public static BotManager gI(){
        if(i == null){
            i = new BotManager();/*telegram @tomihjhj_bot*/
        }
            return i;/*telegram @tomihjhj_bot*/
    }
       @Override
    public void run() {
        while (NgocRongOnline.isRunning) {
            try {
                long st = System.currentTimeMillis();/*telegram @tomihjhj_bot*/
                for (Bot bot : this.bot) {
                    bot.update();/*telegram @tomihjhj_bot*/
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));/*telegram @tomihjhj_bot*/
            } catch (Exception ignored) {
            }

        }
    }
}
