package chatter.messaging.cache;

import org.springframework.stereotype.Component;

@Component
public class ChatterCache {

    private String messageTopicName;

    public ChatterCache() {

    }

    public String getMessageTopicName() {
        return messageTopicName;
    }

    public void setMessageTopicName(String messageTopicName) {
        this.messageTopicName = messageTopicName;
    }
}
