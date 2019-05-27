package chatter.messaging;

import chatter.messaging.cache.DistributionCache;
import chatter.messaging.exception.WorkerThreadException;
import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.ConnectedUserModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WorkerTask implements Runnable {

    private ConnectedUserModel connectedUserModel;
    private MessageSender messageSender = MessageSender.getInstance();
    private OnlineUser onlineUser = OnlineUser.getInstance();
    private DistributionCache distributionCache = DistributionCache.getInstance();

    public WorkerTask(ConnectedUserModel connectedUserModel) {
        this.connectedUserModel = connectedUserModel;
    }

    @Override
    public void run() {
        try{
            Socket socket = connectedUserModel.getClient();
            while (socket.isConnected()) {
                ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
                messageSender.send((CommunicationModel) stream.readObject());
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new WorkerThreadException("Occurred Exception while sending message", e);
        }
        finally {
            Long userId = connectedUserModel.getUser().getId();
            onlineUser.pop(userId);
            distributionCache.pop(userId);
        }
    }
}
