// 
// Decompiled by Procyon v0.6.0
// 

package network.inetwork;

import network.Message;

public interface ISession
{
    ISession setSendCollect(final IMessageSendCollect p0);
    
    ISession setMessageHandler(final IMessageHandler p0);
    
    ISession setKeyHandler(final IKeySessionHandler p0);
    
    ISession startSend();
    
    ISession startCollect();
    
    ISession start();
    
    String getIP();
    
    boolean isConnected();
    
    long getID();
    
    void sendMessage(final Message p0);
    
    void doSendMessage(final Message p0) throws Exception;
    
    void disconnect();
    
    void dispose();
    
    int getNumMessages();
    
    void sendKey() throws Exception;
    
    byte[] getKey();
    
    boolean sentKey();
    
    void setSentKey(final boolean p0);
}
