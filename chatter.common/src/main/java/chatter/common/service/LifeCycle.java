package chatter.common.service;


public interface LifeCycle {

    default void start() throws Exception {
        registerShutdownHook();
    }

    void stop() throws Exception;

    default void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stop();
            }
            catch (Exception e) {
                //logger.error("Error when trying to shutdown a lifecycle component: " + this.getClass().getName(), e);
            }
        }));
    }
}
