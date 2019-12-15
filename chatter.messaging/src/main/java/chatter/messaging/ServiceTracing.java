package chatter.messaging;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class ServiceTracing {

    private static Logger logger = Logger.getLogger(ServiceTracing.class.getName());

    private List<IService> serviceList = new ArrayList<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public void addService(IService iService) {
        this.serviceList.add(iService);
    }

    public void start() {
        process();
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(checkApplicationService(), 20, 2, TimeUnit.MINUTES);
    }

    private Runnable checkApplicationService() {
        return () -> serviceList.forEach(service -> {
            if (!service.state().equals(ServiceState.STOPPED)) {
                logger.log(Level.INFO, "Service is running. Service: {0}", service.getName());
            } else {
                logger.log(Level.SEVERE, "Service is not running. Trying to start. Service: {0}", service.getName());
                service.start();
            }
        });
    }
}
