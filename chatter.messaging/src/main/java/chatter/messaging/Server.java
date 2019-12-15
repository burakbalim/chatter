package chatter.messaging;

import chatter.common.service.LifeCycle;
import chatter.messaging.exception.ServerException;
import chatter.messaging.model.ConnectedUser;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

}
