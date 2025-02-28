// 
// Decompiled by Procyon v0.6.0
// 

package network;

import utils.StringUtil;
import java.io.IOException;
import network.inetwork.IMessageHandler;
import network.inetwork.IMessageSendCollect;
import java.net.InetSocketAddress;
import java.net.SocketException;
import network.inetwork.IKeySessionHandler;
import java.net.Socket;
import network.inetwork.ISession;

public class Session implements ISession
{
    private static ISession instance;
    private static int ID_INIT;
    private byte[] KEYS;
    private boolean sentKey;
    public int id;
    private Socket socket;
    private boolean connected;
    private Sender sender;
    private Collector collector;
    private final Thread tSender;
    private final Thread tCollector;
    private IKeySessionHandler keyHandler;
    private String ip;
    
    public static ISession gI() throws Exception {
        if (Session.instance == null) {
            throw new Exception("Instance has not been initialized!");
        }
        return Session.instance;
    }
    
    public Session(final Socket socket) {
        this.KEYS = "NguyenDucVuEntertainment".getBytes();
        this.id = Session.ID_INIT++;
        this.socket = socket;
        try {
            this.socket.setSendBufferSize(1048576);
            this.socket.setReceiveBufferSize(1048576);
        }
        catch (final SocketException ex) {}
        this.connected = true;
        this.ip = ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        this.sender = ((this.sender != null) ? this.sender.setSocket(this.socket) : new Sender(this, this.socket));
        this.collector = ((this.collector != null) ? this.collector.setSocket(this.socket) : new Collector(this, this.socket));
        this.tSender = new Thread((this.sender != null) ? this.sender.setSocket(this.socket) : (this.sender = new Sender(this, this.socket)), "Sender - IP : " + this.ip);
        this.tCollector = new Thread((this.collector != null) ? this.collector.setSocket(this.socket) : (this.collector = new Collector(this, this.socket)), "Collector - IP : " + this.ip);
    }
    
    @Override
    public void sendMessage(final Message msg) {
        if (this.isConnected() && msg != null) {
            this.sender.sendMessage(msg);
        }
    }
    
    @Override
    public ISession setSendCollect(final IMessageSendCollect collect) {
        this.sender.setSend(collect);
        this.collector.setCollect(collect);
        return this;
    }
    
    @Override
    public ISession setMessageHandler(final IMessageHandler handler) {
        this.collector.setMessageHandler(handler);
        return this;
    }
    
    @Override
    public ISession setKeyHandler(final IKeySessionHandler handler) {
        this.keyHandler = handler;
        return this;
    }
    
    @Override
    public ISession startSend() {
        this.tSender.start();
        return this;
    }
    
    @Override
    public ISession startCollect() {
        this.tCollector.start();
        return this;
    }
    
    @Override
    public String getIP() {
        return this.ip;
    }
    
    @Override
    public long getID() {
        return this.id;
    }
    
    @Override
    public void disconnect() {
        this.connected = false;
        this.sentKey = false;
        if (this.sender != null) {
            this.sender.close();
        }
        if (this.collector != null) {
            this.collector.close();
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            }
            catch (final IOException ex) {}
        }
        this.dispose();
    }
    
    @Override
    public void dispose() {
        if (this.sender != null) {
            this.sender.dispose();
        }
        if (this.collector != null) {
            this.collector.dispose();
        }
        this.socket = null;
        this.sender = null;
        this.collector = null;
        this.ip = null;
        SessionManager.gI().removeSession(this);
    }
    
    @Override
    public void sendKey() throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler has not been initialized!");
        }
        if (Network.gI().isRandomKey()) {
            this.KEYS = StringUtil.randomText(7).getBytes();
        }
        this.keyHandler.sendKey(this);
    }
    
    @Override
    public boolean sentKey() {
        return this.sentKey;
    }
    
    @Override
    public void setSentKey(final boolean sent) {
        this.sentKey = sent;
    }
    
    @Override
    public void doSendMessage(final Message msg) throws Exception {
        this.sender.doSendMessage(msg);
    }
    
    @Override
    public ISession start() {
        this.tSender.start();
        this.tCollector.start();
        return this;
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
    }
    
    @Override
    public byte[] getKey() {
        return this.KEYS;
    }
    
    @Override
    public int getNumMessages() {
        if (this.isConnected()) {
            return this.sender.getNumMessages();
        }
        return -1;
    }
}
