package SessionZ;

import java.util.ArrayList;
import java.util.List;
import network.inetwork.ISession;

public class NNTSessionManager {

    private static NNTSessionManager i;
    private final List<ISession> sessions;

    public static NNTSessionManager gI() {
        if (i == null) {
            i = new NNTSessionManager();
        }
        return i;
    }

    public NNTSessionManager() {
        this.sessions = new ArrayList<>();
    }

    public void putSession(ISession session) {
        this.sessions.add(session);
    }

    public void removeSession(ISession session) {
        this.sessions.remove(session);
    }

    public List<ISession> getSessions() {
        return this.sessions;
    }

    public ISession findByID(long id) throws Exception {
        if (this.sessions.isEmpty()) {
            throw new Exception("Session " + id + " không tồn tại");
        }
        for (ISession session : this.sessions) {
            if (session.getID() > id) {
                throw new Exception("Session " + id + " không tồn tại");
            }
            if (session.getID() == id) {
                return session;
            }
        }
        throw new Exception("Session " + id + " không tồn tại");
    }

    public int getNumSession() {
        return this.sessions.size();
    }
}
