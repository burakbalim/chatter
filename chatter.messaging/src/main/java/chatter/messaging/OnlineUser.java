package chatter.messaging;

import chatter.messaging.model.ConnectedUserModel;

import java.util.concurrent.ConcurrentHashMap;

public class OnlineUser {

    private ConcurrentHashMap<Long, ConnectedUserModel> concurrentHashMap = new ConcurrentHashMap<>();
    private static OnlineUser instance;

    public static synchronized OnlineUser getInstance() {
        if (instance == null) {
            instance = new OnlineUser();
        }
        return instance;
    }

    public ConnectedUserModel get(long id) {
        return this.concurrentHashMap.get(id);
    }

    public synchronized void add(long id, ConnectedUserModel connectedUserModel) {
        this.concurrentHashMap.put(id, connectedUserModel);
    }

    public synchronized void pop(long id) {
        this.concurrentHashMap.remove(id);
    }
}
