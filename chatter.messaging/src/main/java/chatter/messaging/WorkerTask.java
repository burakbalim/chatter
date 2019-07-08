package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.exception.WorkerThreadException;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.ConnectedUserModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WorkerTask implements Runnable {

    private ConnectedUserModel connectedUserModel;
    private MessageSender messageSender;
    private OnlineUser onlineUser;
    private DistributionCache distributionCache;

    public WorkerTask(ConnectedUserModel connectedUserModel, MessageSender messageSender, OnlineUser onlineUser, DistributionCache distributionCache) {
        this.connectedUserModel = connectedUserModel;
        this.messageSender = messageSender;
        this.onlineUser = onlineUser;
        this.distributionCache = distributionCache;
    }

    @Override
    public void run() {
        try {
            Socket socket = connectedUserModel.getClient();
            while (socket.isConnected()) {
                ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
                messageSender.send((CommunicationModel) stream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new WorkerThreadException("Occurred Exception while sending message", e);
        } finally {
            Long userId = connectedUserModel.getUser().getId();
            onlineUser.pop(userId);
            distributionCache.pop(userId);
        }
    }
}
