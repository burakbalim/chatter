package chatter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import chatter.common.exception.ChatterException;
import chatter.common.model.ChatterConfiguration;
import chatter.common.util.ConfigurationHelper;
import chatter.messaging.IService;
import chatter.messaging.Server;
import chatter.messaging.ServiceTracing;
import chatter.messaging.cache.ChatterConfCache;
import chatter.messaging.event.EventHandler;
import chatter.messaging.exception.ServerException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private Server server;

    private ServiceTracing serviceTracing;

    private ChatterConfCache confCache;

    private ApplicationContext appContext;

    private ConfigurationHelper configurationHelper;

    public Main(ApplicationContext appContext,
                Server server, ServiceTracing serviceTracing,
                ChatterConfCache chatterConfCache, ConfigurationHelper configurationHelper) {
        this.server = server;
        this.confCache = chatterConfCache;
        this.serviceTracing = serviceTracing;
        this.appContext = appContext;
        this.configurationHelper = configurationHelper;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        closeIfInterrupt();

        populateConfigurationCache(args);

        registerForEmployeeBean();

        serviceTracing.start();
        server.build(confCache.getChatterConfiguration().getPort());
        server.start();
    }

    private void registerForEmployeeBean() {
        String[] beanNames = appContext.getBeanDefinitionNames();
        Arrays.stream(beanNames).map(appContext::getBean).forEach(bean -> {
            if (bean instanceof EventHandler) {
                EventHandler eventHandler = (EventHandler) bean;
                eventHandler.register();
            } else if (bean instanceof IService) {
                serviceTracing.addService((IService) bean);
            }
        });
    }

    private void populateConfigurationCache(String... args) {
        ChatterConfiguration config;
        try {
            config = configurationHelper.getConfiguration(args);
            confCache.setChatterConfiguration(config);
            confCache.setMessageTopicName("messaging-" + UUID.randomUUID());
        } catch (ChatterException e) {
            throw new ServerException("Configuration read error", e);
        }
    }

    private void closeIfInterrupt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> serviceTracing.stop()));
    }
}
