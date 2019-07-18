package chatter.messaging.model;

import java.io.Serializable;
import java.util.List;

public class CommunicationModel implements Serializable {

    private String message;
    private List<Long> sentUserIds;
    private Long senderUserId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Long> getSentUserIds() {
        return sentUserIds;
    }

    public void setSentUserIds(List<Long> sentUserIds) {
        this.sentUserIds = sentUserIds;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }
}
