package chatter.messaging.event;

import java.io.Serializable;

public interface IEvent {

    public void fire(Event ievent);

}
