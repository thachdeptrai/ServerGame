// 
// Decompiled by Procyon v0.6.0
// 

package network.inetwork;

import network.Message;

public interface IMessageHandler
{
    void onMessage(final ISession p0, final Message p1) throws Exception;
}
