// 
// Decompiled by Procyon v0.6.0
// 

package network.inetwork;

public interface INetwork extends Runnable
{
    INetwork init();
    
    INetwork start(final int p0) throws Exception;
    
    INetwork setAcceptHandler(final ISessionAcceptHandler p0);
    
    INetwork close();
    
    INetwork dispose();
    
    INetwork setDoSomeThingWhenClose(final IServerClose p0);
    
    INetwork randomKey(final boolean p0);
    
    INetwork setTypeSessioClone(final Class p0) throws Exception;
    
    ISessionAcceptHandler getAcceptHandler() throws Exception;
    
    boolean isRandomKey();
    
    void stopConnect();
}
