package chatter.messaging.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastInstanceProvider {

    private HazelcastInstance hazelcastInstance;
    private EventProcess eventProcess = new EventProcess();
    private static HazelcastInstanceProvider instance;

    private HazelcastInstanceProvider() {

    }

    public static synchronized HazelcastInstanceProvider  getInstance() {
        if (instance == null) {
            instance = new HazelcastInstanceProvider();
        }
        return instance;
    }

    public void start(HazelcastConfiguration hazelcastCfg) {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        eventProcess.start(hazelcastInstance);
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
