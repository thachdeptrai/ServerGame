// 
// Decompiled by Procyon v0.6.0
// 

package network;

import network.inetwork.ISession;
import network.inetwork.IKeySessionHandler;

public class KeyHandler implements IKeySessionHandler
{
    @Override
    public void sendKey(final ISession session) {
        final Message msg = new Message((byte)(-27));
        try {
            final byte[] KEYS = session.getKey();
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; ++i) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            session.doSendMessage(msg);
            msg.cleanup();
            session.setSentKey(true);
        }
        catch (final Exception ex) {}
    }
}
