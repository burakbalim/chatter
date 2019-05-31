package chatter;

import chatter.messaging.Server;
import chatter.messaging.cache.ChatterCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.hazelcast.HazelcastConfiguration;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import com.hazelcast.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ChatterCache chatterCache;

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    @Autowired
    private ApplicationContext appContext;

    public Main(Server server, ChatterCache chatterCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.server = server;
        this.chatterCache = chatterCache;
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
    }

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

        registerEventBus();

        server.build(port);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            hazelcastInstanceProvider.close();
        }));
    }

    private void registerEventBus() {
        String[] beanDefinitionNames = appContext.getBeanDefinitionNames();
        for(String item : beanDefinitionNames) {
            Object bean = appContext.getBean(item);
            if(bean instanceof EventHandler) {
                EventHandler bean1 = (EventHandler) bean;
                bean1.register();
            }
        }
    }
}
