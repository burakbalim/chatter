package chatter.messaging.event;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class EventHandler implements MessageListener<Object> {

    private ExecutorService executorService = new ThreadPoolExecutor(10, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public abstract void handle(EventPayload event);

    @Override
    public void onMessage(Message<Object> message) {
        EventPayload messageObject = (EventPayload) message.getMessageObject();

        handle(messageObject);
    }
}
