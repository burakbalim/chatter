package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.exception.WorkerThreadException;
import chatter.messaging.model.Communication;
import chatter.messaging.model.ConnectedUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WorkerTask implements Runnable {

    private ConnectedUser connectedUser;
    private MessageSender messageSender;
    private OnlineUser onlineUser;
    private DistributionCache distributionCache;

    WorkerTask(ConnectedUser connectedUser, MessageSender messageSender,
               OnlineUser onlineUser, DistributionCache distributionCache) {
        this.connectedUser = connectedUser;
        this.messageSender = messageSender;
        this.onlineUser = onlineUser;
        this.distributionCache = distributionCache;
    }

    @Override
    public void run() {
        try (Socket socket = connectedUser.getClient()){
            while (socket.isConnected()) {
                ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
                messageSender.send((Communication) stream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new WorkerThreadException("Occurred Exception while sending message", e);
        } finally {
            Long userId = connectedUser.getUser().getId();
            onlineUser.pop(userId);
            distributionCache.pop(userId);
        }
    }
}
