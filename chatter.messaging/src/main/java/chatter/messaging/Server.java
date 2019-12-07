package chatter.messaging;

import chatter.common.exception.ChatterException;
import chatter.common.service.LifeCycle;
import chatter.messaging.exception.ServerException;
import chatter.messaging.model.ConnectedUser;
import chatter.messaging.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Service
public class Server implements LifeCycle {

    private ThreadPoolExecutor connectionExecutor = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private ConnectionManager connectionManager;
    private ServerSocket serverSocket = null;
    private boolean stopSignal;

    public Server(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void build(int address) {
        try {
            serverSocket = new ServerSocket(address);
        } catch (IOException e) {
            throw new ServerException("Occurred exception while creating server socket", e);
        }
    }

    @Override
    public void start() {
        stopSignal = false;
        connectionManager.start();
        Thread mainServerThread = new Thread(this::process, "Server Main Thread");
        mainServerThread.start();
        registerShutdownHook();
    }

    @Override
    public void stop() {
        stopSignal = true;
        connectionManager.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Exception while server closing", e);
        }
    }

    private void process() {
        while (!stopSignal) {
            try {
                Socket socket = serverSocket.accept();
                Future<ConnectedUser> connection = connectionExecutor.submit(new UserRegisterTask(socket));
                connectionManager.addQueue(connection);
            }
            catch (IOException e) {
                throw new ServerException("Occurred Exception in Server Main Thread", e);
            }
        }
    }

    private class UserRegisterTask implements Callable<ConnectedUser> {
        private Socket socket;

        private UserRegisterTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public ConnectedUser call() throws ChatterException {
            ConnectedUser connectedUser;
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                User user = (User) objectInputStream.readObject();
                connectedUser = new ConnectedUser();
                connectedUser.setUser(user);
                connectedUser.setClient(socket);
            }
            catch (IOException | ClassNotFoundException e) {
                throw new ChatterException("Occurred Connection Exception ", e);
            }
            return connectedUser;
        }
    }

}
