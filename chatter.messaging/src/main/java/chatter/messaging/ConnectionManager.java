package chatter.messaging;

import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.cache.DistributionCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.exception.ConnectionManagerException;
import chatter.messaging.model.ConnectedUserModel;
import chatter.messaging.model.UserEventTopic;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class ConnectionManager implements IService {

    private Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private ThreadPoolExecutor workerTaskExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private LinkedBlockingQueue<Future> queue = new LinkedBlockingQueue<>();
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
        workerTaskExecutor.shutdownNow();
    }

    @Override
    public String getName() {
        return "Connection Manager";
    }

    private void process() {
        while (!isStopSignal) {
            Future future = queue.poll();
            executeFuture(future);
        }
    }

    private void executeFuture(Future future) {
        try {
            if (future == null) {
                return;
            }

            if (future.isDone()) {
                ConnectedUserModel connection = (ConnectedUserModel) future.get();
                cacheUpdate(connection);
                workerTaskExecutor.submit(new WorkerTask(connection, messageSender, onlineUser, distributionCache));
            } else {
                queue.add(future);
            }

        } catch (InterruptedException e) {
            isStopSignal = true;
            Thread.currentThread().interrupt();
            throw new ConnectionManagerException("Occurred exception while user operation", e);
        }  catch (ExecutionException e) {
            logger.log(Level.WARNING, "Connection Manager execution error. Exception: {0}", e);
        }
    }

    private void cacheUpdate(ConnectedUserModel connection) {
        long id = connection.getUser().getId();
        onlineUser.put(id, connection);
        distributionCache.put(id, new UserEventTopic(id, chatterConfCache.getMessageTopicName()));
    }

    public void addQueue(Future<ConnectedUserModel> connectedUserModel) {
        queue.add(connectedUserModel);
    }

    public ServiceState state() {
        return !isStopSignal ? ServiceState.RUNNING : ServiceState.STOPPED;
    }
}
