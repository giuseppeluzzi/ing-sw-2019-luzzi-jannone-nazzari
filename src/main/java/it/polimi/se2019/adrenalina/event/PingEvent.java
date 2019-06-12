package it.polimi.se2019.adrenalina.event;

import static it.polimi.se2019.adrenalina.event.EventType.PING_EVENT;

public class PingEvent implements Event {

    private static final long serialVersionUID = -1200532418855248702L;

    @Override
    public EventType getEventType() {
        return PING_EVENT;
    }
}
