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

@Component
public class ConnectionManager implements IService {

    private Logger logger  = Logger.getLogger(ConnectionManager.class.getName());

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
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
        threadPoolExecutor.shutdown();
    }

    @Override
    public String getName() {
        return "Connection Manager";
    }

    private void process() {
        while (!isStopSignal) {
            Future future = queue.poll();
            try {

                if (future != null && future.isDone()) {
                    ConnectedUserModel connection = (ConnectedUserModel) future.get();
                    cacheUpdate(connection, connection.getUser().getId());
                    threadPoolExecutor.submit(new WorkerTask(connection, messageSender, onlineUser, distributionCache));
                } else {
                    queue.add(future);
                }

            } catch (InterruptedException e) {
                isStopSignal = true;
                Thread.currentThread().interrupt();
                throw new ConnectionManagerException("Occurred exception while user operation", e);
            } catch (ExecutionException e) {
                //TODO log
            }
        }
    }

    private void cacheUpdate(ConnectedUserModel connection, Long id) {
        onlineUser.add(id, connection);
        distributionCache.add(new UserEventTopic(chatterConfCache.getMessageTopicName(), id));
    }

    public void addQueue(Future<ConnectedUserModel> connectedUserModel) {
        queue.add(connectedUserModel);
    }

    public ServiceState state() {
        return !isStopSignal ? ServiceState.RUNNNING : ServiceState.STOPPED;
    }
}
