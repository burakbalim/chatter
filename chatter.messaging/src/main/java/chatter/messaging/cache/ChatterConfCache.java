package chatter.messaging.cache;

import chatter.common.model.ChatterConfiguration;
import org.springframework.stereotype.Component;

@Component
public class ChatterConfCache {

    private ChatterConfiguration chatterConfiguration;
    private String messageTopicName;

    public String getMessageTopicName() {
        return messageTopicName;
    }

    public void setMessageTopicName(String messageTopicName) {
        this.messageTopicName = messageTopicName;
    }

    public ChatterConfiguration getChatterConfiguration() {
        return chatterConfiguration;
    }

    public void setChatterConfiguration(ChatterConfiguration chatterConfiguration) {
        this.chatterConfiguration = chatterConfiguration;
    }
}
