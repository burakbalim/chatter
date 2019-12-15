package chatter.messaging.model;

import java.net.Socket;
import java.util.Objects;

public class ConnectedUser {

    private Socket client;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedUser that = (ConnectedUser) o;
        return client.equals(that.client) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, user);
    }

    @Override
    public String toString() {
        return "ConnectedUserModel{" +
                "client=" + client +
                ", user=" + user +
                '}';
    }
}
