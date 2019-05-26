package chatter.messaging;

import chatter.messaging.exception.ConnectionManagerException;
import chatter.messaging.model.ConnectedUserModel;

import java.net.Socket;
import java.util.concurrent.*;

public class ConnectionManager {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private LinkedBlockingQueue<Future> queue = new LinkedBlockingQueue<>();
    private OnlineUser onlineUser = OnlineUser.getInstance();
    private boolean isStopSignal;

    private static ConnectionManager instance;

    private ConnectionManager() {

    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void start() {
        isStopSignal = false;

        Thread thread = new Thread(this::process, "Connection-Manager Main Thread");
        thread.start();
    }

    public void stop() {
        isStopSignal = true;
        threadPoolExecutor.shutdown();
    }

    private void process() {
        while (!isStopSignal) {
            Future future = queue.poll();
            try {
                if (future != null) {
                    if (future.isDone()) {
                        ConnectedUserModel connection = (ConnectedUserModel) future.get();
                        onlineUser.add(connection.getUser().getId(), connection);

                        threadPoolExecutor.submit(new WorkerTask(connection));
                    } else {
                        queue.add(future);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ConnectionManagerException("Occurred exception while user operation", e);
            } catch (ExecutionException e) {
                throw new ConnectionManagerException("Occurred exception while user operation", e);
            }
        }
    }

    public void addQueue(Future connectedUserModel) {
        queue.add(connectedUserModel);
    }
}
