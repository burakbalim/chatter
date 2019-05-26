package chatter.messaging.model;

import java.io.Serializable;
import java.util.List;

public class CommunicationModel implements Serializable {

    private String message;
    private List<Integer> users;
    private Integer senderUser;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public Integer getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(Integer senderUser) {
        this.senderUser = senderUser;
    }
}
