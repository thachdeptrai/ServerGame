// 
// Decompiled by Procyon v0.6.0
// 

package network;

import java.io.IOException;
import java.net.Socket;
import network.inetwork.IMessageHandler;
import network.inetwork.IMessageSendCollect;
import java.io.DataInputStream;
import network.inetwork.ISession;

public final class Collector implements Runnable
{
    private ISession session;
    private DataInputStream dis;
    private IMessageSendCollect collect;
    private IMessageHandler messageHandler;
    
    public Collector(final ISession session, final Socket socket) {
        this.session = session;
        this.setSocket(socket);
    }
    
    public Collector setSocket(final Socket socket) {
        try {
            this.dis = new DataInputStream(socket.getInputStream());
        }
        catch (final IOException ex) {}
        return this;
    }
    
    @Override
    public void run() {
        try {
            while (this.session != null && this.session.isConnected()) {
                final Message msg = this.collect.readMessage(this.session, this.dis);
                if (msg.command == -27) {
                    this.session.sendKey();
                }
                else {
                    this.messageHandler.onMessage(this.session, msg);
                }
                msg.cleanup();
            }
        }
        catch (final Exception ex) {}
        try {
            Network.gI().getAcceptHandler().sessionDisconnect(this.session);
        }
        catch (final Exception ex2) {}
        if (this.session != null) {
            this.session.disconnect();
        }
    }
    
    public void setCollect(final IMessageSendCollect collect) {
        this.collect = collect;
    }
    
    public void setMessageHandler(final IMessageHandler handler) {
        this.messageHandler = handler;
    }
    
    public void close() {
        if (this.dis != null) {
            try {
                this.dis.close();
            }
            catch (final IOException ex) {}
        }
    }
    
    public void dispose() {
        this.session = null;
        this.dis = null;
        this.collect = null;
    }
}
