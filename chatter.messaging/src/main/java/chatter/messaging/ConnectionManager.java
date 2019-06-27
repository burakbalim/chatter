package chatter.messaging;

import chatter.messaging.cache.ChatterCache;
import chatter.messaging.cache.DistributionCache;
import chatter.messaging.cache.OnlineUser;
import chatter.messaging.exception.ConnectionManagerException;
import chatter.messaging.model.ConnectedUserModel;
import chatter.messaging.model.MessageCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ConnectionManager implements IService {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private LinkedBlockingQueue<Future> queue = new LinkedBlockingQueue<>();
    private DistributionCache distributionCache;
    private ChatterCache chatterCache;
    private OnlineUser onlineUser;
    private MessageSender messageSender;
    private boolean isStopSignal;

    public ConnectionManager(OnlineUser onlineUser, ChatterCache chatterCache, DistributionCache distributionCache, MessageSender messageSender) {
        this.onlineUser = onlineUser;
        this.chatterCache = chatterCache;
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
                if (future != null) {
                    if (future.isDone()) {
                        ConnectedUserModel connection = (ConnectedUserModel) future.get();
                        Long id = connection.getUser().getId();
                        onlineUser.add(id, connection);
                        distributionCache.add(new MessageCache(chatterCache.getMessageTopicName(), id));
                        threadPoolExecutor.submit(new WorkerTask(connection, messageSender, onlineUser, distributionCache));
                    } else {
                        queue.add(future);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ConnectionManagerException("Occurred exception while user operation", e);
            } catch (ExecutionException e) {
                //log
            }
        }
    }

    public void addQueue(Future<ConnectedUserModel> connectedUserModel) {
        queue.add(connectedUserModel);
    }

    public ServiceState state() {
        return !isStopSignal ? ServiceState.RUNNNING : ServiceState.STOPPED;
    }
}
