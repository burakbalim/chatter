package chatter.messaging.cache;

import org.springframework.stereotype.Component;

@Component
public class ChatterConfCache {

    private String messageTopicName;

    public String getMessageTopicName() {
        return messageTopicName;
    }

    public void setMessageTopicName(String messageTopicName) {
        this.messageTopicName = messageTopicName;
    }
}
