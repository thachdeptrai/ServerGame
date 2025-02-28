package SessionZ;

import Interface.IServerClose;
import network.inetwork.ISessionAcceptHandler;
import network.inetwork.ISession;
import network.Session;
import network.SessionFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import Interface.INTTServer;
import NgocThachServer.NgocRongOnline;

public class NNTServer
        implements INTTServer {

    private static NNTServer I;
    private int port;
    private ServerSocket serverListen;
    private Class sessionClone;

    public static NNTServer gI() {
        if (I == null) {
            I = new NNTServer();
        }
        return I;
    }
    private boolean start;
    private boolean randomKey;
    private IServerClose serverClose;
    private ISessionAcceptHandler acceptHandler;
    private Thread loopServer;

    private NNTServer() {
        this.port = -1;
        this.sessionClone = Session.class;
    }

    public static HashMap<String, Integer> firewall = new HashMap<>();
    public static HashMap<String, Integer> firewallDownDataGame = new HashMap<>();

    public INTTServer init() {
        this.loopServer = new Thread(this);
        return this;
    }

    public INTTServer start(int port) throws Exception {
        if (port < 0) {
            throw new Exception("Vui lòng khởi tạo port server!");
        }
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        }
        if (!ISession.class.isAssignableFrom(this.sessionClone)) {
            throw new Exception("Type session clone không hợp lệ!");
        }
        try {
            this.port = port;
            this.serverListen = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Lỗi khởi tạo server tại port " + port);
            System.exit(0);
        }
        this.start = true;
        this.loopServer.start();

        System.out.println("Server " + NgocRongOnline.NAME + " đang chạy tại port " + this.port);
        return this;
    }

    public INTTServer close() {
        this.start = false;
        if (this.serverListen != null) {
            try {
                this.serverListen.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (this.serverClose != null) {
            this.serverClose.serverClose();
        }
        System.out.println("Server đã đóng!");
        return this;
    }

    public INTTServer dispose() {
        this.acceptHandler = null;
        this.loopServer = null;
        this.serverListen = null;
        return this;
    }

    public INTTServer setAcceptHandler(ISessionAcceptHandler handler) {
        this.acceptHandler = handler;
        return this;
    }

    public void run() {
        while (this.start) {
            try {
                Socket socket = this.serverListen.accept();
                String ip = socket.getInetAddress().getHostAddress();
                if (firewall.containsKey(ip) && firewall.get(ip).intValue() > 21) {
                    socket.close();
                } else {
                    network.inetwork.ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
                    this.acceptHandler.sessionInit(session);
                    NNTSessionManager.gI().putSession(session);
                    if (firewall.containsKey(ip)) {
                        int value = firewall.get(ip).intValue();
                        firewall.put(ip, value += 1);
                    } else {
                        firewall.put(ip, 1);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                Logger.getLogger(NNTServer.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }
//     public void run() {
//        \while (this.start) {
//             try {
//               Socket socket = this.serverListen.accept();
//               ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
//               this.acceptHandler.sessionInit(session);
//               GirlkunSessionManager.gI().putSession(session);
//                           } catch (IOException ex) {
//               ex.printStackTrace();
//               } catch (Exception ex) {
//                Logger.getLogger(EMTIServer.class.getName()).log(Level.SEVERE, (String) null, ex);
//                            }
//                    }
//            }

    public INTTServer setDoSomeThingWhenClose(IServerClose serverClose) {
        this.serverClose = serverClose;
        return this;
    }

    public INTTServer randomKey(boolean isRandom) {
        this.randomKey = isRandom;
        return this;
    }

    public boolean isRandomKey() {
        return this.randomKey;
    }

    public INTTServer setTypeSessioClone(Class clazz) throws Exception {
        this.sessionClone = clazz;
        return this;
    }

    public ISessionAcceptHandler getAcceptHandler() throws Exception {
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        }
        return this.acceptHandler;
    }

    public void stopConnect() {
        this.start = false;
    }

}
