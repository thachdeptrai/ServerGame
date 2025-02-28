// 
// Decompiled by Procyon v0.6.0
// 

package network;

import java.net.Socket;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import network.inetwork.ISession;
import java.io.IOException;
import utils.Logger;
import java.nio.channels.Selector;
import network.inetwork.ISessionAcceptHandler;
import network.inetwork.IServerClose;
import java.nio.channels.ServerSocketChannel;
import network.inetwork.INetwork;

public class Network implements INetwork, Runnable
{
    private static Network instance;
    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Class sessionClone;
    private boolean start;
    private boolean randomKey;
    private IServerClose serverClose;
    private ISessionAcceptHandler acceptHandler;
    private Thread loopServer;
    private Selector selector;
    
    public static Network gI() {
        if (Network.instance == null) {
            Network.instance = new Network();
        }
        return Network.instance;
    }
    
    private Network() {
        this.port = -1;
        this.sessionClone = Session.class;
    }
    
    @Override
    public INetwork init() {
        try {
            this.selector = Selector.open();
        }
        catch (final IOException ex) {
            Logger.error(ex.toString());
        }
        this.loopServer = new Thread(this, "Network");
        return this;
    }
    
    @Override
    public INetwork start(final int port) throws Exception {
        if (port < 0) {
            throw new Exception("Please initialize the server port!");
        }
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler has not been initialized!");
        }
        if (!ISession.class.isAssignableFrom(this.sessionClone)) {
            throw new Exception("The session clone type is invalid!");
        }
        try {
            this.port = port;
            (this.serverSocketChannel = ServerSocketChannel.open()).configureBlocking(false);
            this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
            this.serverSocketChannel.register(this.selector, 16);
        }
        catch (final IOException ex) {
            Logger.error("Error initializing server at port " + port + "\n");
            System.exit(0);
        }
        this.start = true;
        this.loopServer.start();
        Logger.success("Server initialized and listening on port " + this.port + "\n");
        return this;
    }
    
    @Override
    public INetwork close() {
        this.start = false;
        if (this.serverSocketChannel != null) {
            try {
                this.serverSocketChannel.close();
            }
            catch (final IOException ex) {}
        }
        if (this.serverClose != null) {
            this.serverClose.serverClose();
        }
        return this;
    }
    
    @Override
    public INetwork dispose() {
        this.acceptHandler = null;
        this.loopServer = null;
        this.serverSocketChannel = null;
        return this;
    }
    
    @Override
    public INetwork setAcceptHandler(final ISessionAcceptHandler handler) {
        this.acceptHandler = handler;
        return this;
    }
    
    @Override
    public void run() {
        while (this.start) {
            try {
                this.selector.select();
                for (final SelectionKey key : this.selector.selectedKeys()) {
                    if (!key.isAcceptable()) {
                        continue;
                    }
                    final ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    final Socket socket = server.accept().socket();
                    final ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
                    this.acceptHandler.sessionInit(session);
                    SessionManager.gI().putSession(session);
                }
                this.selector.selectedKeys().clear();
            }
            catch (final IOException ex3) {}
            catch (final Exception ex2) {
                Logger.error(ex2.toString());
            }
        }
    }
    
    @Override
    public INetwork setDoSomeThingWhenClose(final IServerClose serverClose) {
        this.serverClose = serverClose;
        return this;
    }
    
    @Override
    public INetwork randomKey(final boolean isRandom) {
        this.randomKey = isRandom;
        return this;
    }
    
    @Override
    public boolean isRandomKey() {
        return this.randomKey;
    }
    
    @Override
    public INetwork setTypeSessioClone(final Class clazz) throws Exception {
        this.sessionClone = clazz;
        return this;
    }
    
    @Override
    public ISessionAcceptHandler getAcceptHandler() throws Exception {
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler has not been initialized!");
        }
        return this.acceptHandler;
    }
    
    @Override
    public void stopConnect() {
        this.start = false;
    }
}
