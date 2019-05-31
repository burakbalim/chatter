package chatter.messaging.hazelcast;

import chatter.messaging.bus.MessagingBus;
import chatter.messaging.cache.ChatterCache;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HazelcastInstanceProvider {

    private HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void init() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public void close(){
        hazelcastInstance.shutdown();
    }


    public <T> void addListener(String topic, MessageListener<T> object) {
        ITopic<T> iTopic = hazelcastInstance.getTopic(topic);

        iTopic.addMessageListener(object);
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
