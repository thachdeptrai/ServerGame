// 
// Decompiled by Procyon v0.6.0
// 

package network;

import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.net.Socket;
import network.inetwork.IMessageSendCollect;
import java.io.DataOutputStream;
import java.util.concurrent.BlockingDeque;
import lombok.NonNull;
import network.inetwork.ISession;

public final class Sender implements Runnable
{
    @NonNull
    private ISession session;
    @NonNull
    private BlockingDeque<Message> messages;
    private DataOutputStream dos;
    private IMessageSendCollect sendCollect;
    
    public Sender(@NonNull final ISession session, @NonNull final Socket socket) {
        if (session == null) {
            throw new NullPointerException("session is marked non-null but is null");
        }
        if (socket == null) {
            throw new NullPointerException("socket is marked non-null but is null");
        }
        try {
            this.session = session;
            this.messages = new LinkedBlockingDeque<Message>();
            this.setSocket(socket);
        }
        catch (final Exception ex) {}
    }
    
    public Sender setSocket(@NonNull final Socket socket) {
        if (socket == null) {
            throw new NullPointerException("socket is marked non-null but is null");
        }
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
        }
        catch (final IOException ex) {}
        return this;
    }
    
    @Override
    public void run() {
        try {
            while (this.session.isConnected()) {
                while (!this.messages.isEmpty()) {
                    final Message message = this.messages.poll(5L, TimeUnit.SECONDS);
                    if (message == null) {
                        continue;
                    }
                    this.doSendMessage(message);
                    message.cleanup();
                }
                TimeUnit.MILLISECONDS.sleep(10L);
            }
        }
        catch (final Exception ex) {}
    }
    
    public synchronized void doSendMessage(final Message message) throws Exception {
        this.sendCollect.doSendMessage(this.session, this.dos, message);
    }
    
    public void sendMessage(final Message msg) {
        try {
            if (this.session.isConnected()) {
                this.messages.add(msg);
            }
        }
        catch (final Exception ex) {}
    }
    
    public void setSend(final IMessageSendCollect sendCollect) {
        this.sendCollect = sendCollect;
    }
    
    public int getNumMessages() {
        return this.messages.size();
    }
    
    public void close() {
        this.messages.clear();
        if (this.dos != null) {
            try {
                this.dos.close();
            }
            catch (final IOException ex) {}
        }
    }
    
    public void dispose() {
        this.session = null;
        this.messages = null;
        this.sendCollect = null;
        this.dos = null;
    }
}
