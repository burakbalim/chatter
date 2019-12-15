package chatter.messaging;

import chatter.common.exception.ChatterException;
import chatter.messaging.model.ConnectedUser;
import chatter.messaging.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

class UserRegisterTask implements Callable<ConnectedUser> {
    private Socket client;

    UserRegisterTask(Socket client) {
        this.client = client;
    }

    @Override
    public ConnectedUser call() throws ChatterException {
        ConnectedUser connectedUser;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            User user = (User) objectInputStream.readObject();
            connectedUser = new ConnectedUser();
            connectedUser.setUser(user);
            connectedUser.setClient(client);
        }
        catch (IOException | ClassNotFoundException e) {
            throw new ChatterException("Occurred Connection Exception ", e);
        }
        return connectedUser;
    }
}
