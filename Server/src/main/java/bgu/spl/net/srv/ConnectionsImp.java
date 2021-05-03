package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.Messages.OpMessage;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp<T> implements Connections<T> {

    ConcurrentHashMap<Integer, ConnectionHandler> connectionHandlerMap =new ConcurrentHashMap<>();


    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler c = connectionHandlerMap.get(connectionId);
        if (c != null) {
            c.send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        for( ConcurrentHashMap.Entry<Integer, ConnectionHandler> entry:connectionHandlerMap.entrySet()){
            entry.getValue().send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connectionHandlerMap.remove(connectionId);
    }

    public void addConnection(int connectionId, ConnectionHandler handler){
        connectionHandlerMap.put(connectionId,handler);
    }

}
