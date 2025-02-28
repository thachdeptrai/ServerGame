package Interface;

import network.inetwork.ISessionAcceptHandler;

public interface INTTServer extends Runnable {
  INTTServer init();
  
  INTTServer start(int paramInt) throws Exception;
  
  INTTServer setAcceptHandler(ISessionAcceptHandler paramISessionAcceptHandler);
  
  INTTServer close();
  
  INTTServer dispose();
  
  INTTServer randomKey(boolean paramBoolean);
  
  INTTServer setDoSomeThingWhenClose(IServerClose paramIServerClose);
  
  INTTServer setTypeSessioClone(Class paramClass) throws Exception;
  
    ISessionAcceptHandler getAcceptHandler() throws Exception;
  
  boolean isRandomKey();
  
  void stopConnect();
}


 
