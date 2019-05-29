package chatter;

import chatter.messaging.Server;
import chatter.messaging.cache.ChatterCache;
import chatter.messaging.hazelcast.HazelcastConfiguration;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private Server server;

    @Autowired
    private ChatterCache chatterCache;

    @Autowired
    private HazelcastInstanceProvider hazelcastInstanceProvider;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        int port = 2001;

        chatterCache.setMessageTopicName("messaging-" + port);

        HazelcastConfiguration hazelcastCfg = new HazelcastConfiguration();
        Config config = new Config();
        hazelcastCfg.setConfig(config);

        hazelcastInstanceProvider.prepareEventMessageListener();

        server.build(port);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            hazelcastInstanceProvider.close();
        }));
    }
}
