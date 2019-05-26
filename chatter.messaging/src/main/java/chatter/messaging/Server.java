package chatter.messaging;

import chatter.messaging.exception.ServerException;
import chatter.messaging.model.ConnectedUserModel;
import chatter.messaging.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    private ConnectionManager connectionManager = ConnectionManager.getInstance();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private ServerSocket serverSocket = null;
    private boolean stopSignal;

    public void build(int address) throws IOException {
        serverSocket = new ServerSocket(address);
    }

    public void start() {
        stopSignal = false;
        connectionManager.start();
        Thread thread = new Thread(this::process, "Server Main Thread");
        thread.start();
    }

    public void stop() {
        stopSignal = true;
        connectionManager.stop();
    }

    private void process() {
        while (!stopSignal) {
            try {
                Socket socket = serverSocket.accept();
                Future<ConnectedUserModel> connection = threadPoolExecutor.submit(new UserRegisterTask(socket));
                connectionManager.addQueue(connection);
            } catch (IOException e) {
                throw new ServerException("Occurred Exception while accept socket", e);
            }
        }
    }

    private class UserRegisterTask implements Callable<ConnectedUserModel> {
        private Socket socket;

        private UserRegisterTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public ConnectedUserModel call() throws Exception {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            User user = (User) objectInputStream.readObject();
            ConnectedUserModel connectedUserModel = new ConnectedUserModel();
            connectedUserModel.setUser(user);
            connectedUserModel.setClient(socket);
            return connectedUserModel;
        }
    }

}
