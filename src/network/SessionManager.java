// 
// Decompiled by Procyon v0.6.0
// 

package network;

import java.util.Iterator;
import java.util.ArrayList;
import network.inetwork.ISession;
import java.util.List;

public class SessionManager
{
    private static SessionManager instance;
    private final List<ISession> sessions;
    
    public static SessionManager gI() {
        if (SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }
        return SessionManager.instance;
    }
    
    public SessionManager() {
        this.sessions = new ArrayList<ISession>();
    }
    
    public void putSession(final ISession session) {
        this.sessions.add(session);
    }
    
    public void removeSession(final ISession session) {
        this.sessions.remove(session);
    }
    
    public List<ISession> getSessions() {
        return this.sessions;
    }
    
    public ISession findByID(final long id) throws Exception {
        if (this.sessions.isEmpty()) {
            throw new Exception("Session " + id + " does not exist");
        }
        for (final ISession session : this.sessions) {
            if (session.getID() > id) {
                throw new Exception("Session " + id + " does not exist");
            }
            if (session.getID() == id) {
                return session;
            }
        }
        throw new Exception("Session " + id + " does not exist");
    }
    
    public int getNumSession() {
        return this.sessions.size();
    }
}
