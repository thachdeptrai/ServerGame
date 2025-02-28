// 
// Decompiled by Procyon v0.6.0
// 

package network;

import network.inetwork.ISession;
import java.net.Socket;

public class SessionFactory
{
    private static SessionFactory instance;
    
    public static SessionFactory gI() {
        if (SessionFactory.instance == null) {
            SessionFactory.instance = new SessionFactory();
        }
        return SessionFactory.instance;
    }
    
    public ISession cloneSession( Class<ISession> clazz, Socket socket) throws Exception {
        return clazz.getConstructor(new Class[]{Socket.class}).newInstance(new Object[]{socket});
    }
}
