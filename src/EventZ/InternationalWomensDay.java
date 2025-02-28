package EventZ;

/**
 *
 * @author NgocThach
 */

import consts.ConstNpc;
import Abstract.Event;
import JDBCMysql.EventDAO;

public class InternationalWomensDay extends Event {

    @Override
    public void init() {
        super.init();
        EventDAO.loadInternationalWomensDayEvent();
    }

    @Override
    public void npc() {
    }
}
