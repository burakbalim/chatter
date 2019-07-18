package chatter;

import chatter.common.exception.ChatterException;
import chatter.common.exception.OrchestrationException;
import chatter.common.util.ChatterUtil;
import chatter.messaging.IService;
import chatter.messaging.Server;
import chatter.messaging.ServiceState;
import chatter.messaging.ServiceTracing;
import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.hazelcast.HazelcastInstanceProvider;
import chatter.messaging.model.ChatterConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ServiceTracing serviceTracing;

    private ChatterConfCache chatterConfCache;

    private HazelcastInstanceProvider hazelcastInstanceProvider;

    private ApplicationContext appContext;

    public Main(ApplicationContext appContext,
                Server server, ServiceTracing serviceTracing,
                ChatterConfCache chatterConfCache, HazelcastInstanceProvider hazelcastInstanceProvider) {
        this.server = server;
        this.chatterConfCache = chatterConfCache;
        this.hazelcastInstanceProvider = hazelcastInstanceProvider;
        this.serviceTracing = serviceTracing;
        this.appContext = appContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        closeIfInterrupt();

        ChatterConfiguration configuration = getChatterConfiguration();
        Integer port = configuration.getPort();

        chatterConfCache.setMessageTopicName("messaging-" + port);

        serviceTracing.start();

        registerForEmployeeBean();

        server.build(port);
        server.start();
    }

    private ChatterConfiguration getChatterConfiguration() {
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("test")).getFile();
        try {
            String confPath = ChatterUtil.readFile(path);
            return ChatterUtil.readJson(confPath, ChatterConfiguration.class);
        } catch (ChatterException e) {
            throw new OrchestrationException("Occured Excetion while reading configuration file", e);
        }
    }

    private void closeIfInterrupt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server.state() == ServiceState.RUNNING) {
                server.stop();
            }
            hazelcastInstanceProvider.close();
        }));
    }

    private void registerForEmployeeBean() {
        String[] beanDefinitionNames = appContext.getBeanDefinitionNames();
        for (String item : beanDefinitionNames) {
            Object bean = appContext.getBean(item);
            if (bean instanceof EventHandler) {
                EventHandler eventHandler = (EventHandler) bean;
                eventHandler.register();
            } else if (bean instanceof IService) {
                serviceTracing.addService((IService) bean);
            }
        }
    }
}
