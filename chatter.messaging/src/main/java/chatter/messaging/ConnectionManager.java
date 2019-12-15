package chatter.messaging;

import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.cache.DistributionCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.exception.ConnectionManagerException;
import chatter.messaging.model.ConnectedUser;
import chatter.messaging.model.UserReceiverTopic;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class ConnectionManager implements IService {

    private Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private static ConnectedUserQueue<Future> connectedUserQueue = new ConnectedUserQueue<>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 1000, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private DistributionCache distributionCache;
    private ChatterConfCache chatterConfCache;
    private OnlineUser onlineUser;
    private MessageSender messageSender;
    private boolean isStopSignal;

    public ConnectionManager(OnlineUser onlineUser, ChatterConfCache chatterConfCache, DistributionCache distributionCache, MessageSender messageSender) {
        this.onlineUser = onlineUser;
        this.chatterConfCache = chatterConfCache;
        this.distributionCache = distributionCache;
        this.messageSender = messageSender;
    }

    @Override
    public void start() {
        isStopSignal = false;

        Thread connectionMainThread = new Thread(this::process, "Connection-Manager Main Thread");
        connectionMainThread.start();
    }

    @Override
    public void stop() {
        isStopSignal = true;
        executor.shutdownNow();
    }

    @Override
    public String getName() {
        return "Connection Manager";
    }

    private void process() {
        while (!isStopSignal) {
            Future future = connectedUserQueue.poll();
            if(future != null) {
                executeForConnectedUser(future);
            }
        }
    }

    private void executeForConnectedUser(Future future) {
        try {
            if (future.isDone()) {
                ConnectedUser connection = (ConnectedUser) future.get();
                populateCache(connection);
                executor.submit(new WorkerTask(connection, messageSender, onlineUser, distributionCache));
            } else {
                connectedUserQueue.add(future);
            }
        } catch (InterruptedException e) {
            isStopSignal = true;
            Thread.currentThread().interrupt();
            throw new ConnectionManagerException("Occurred exception while user operation.", e);
        }  catch (ExecutionException e) {
            logger.log(Level.WARNING, "Connection Manager execution error. Exception: {0}", e);
        }
    }

    private void populateCache(ConnectedUser connection) {
        long id = connection.getUser().getId();
        onlineUser.put(id, connection);
        distributionCache.put(id, new UserReceiverTopic(id, chatterConfCache.getMessageTopicName()));
    }

    void addQueue(Future<ConnectedUser> connectedUserModel) {
        connectedUserQueue.add(connectedUserModel);
    }

    public ServiceState state() {
        return !isStopSignal ? ServiceState.RUNNING : ServiceState.STOPPED;
    }
}
