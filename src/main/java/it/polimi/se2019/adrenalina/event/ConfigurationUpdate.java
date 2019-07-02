package it.polimi.se2019.adrenalina.event;

import static it.polimi.se2019.adrenalina.event.EventType.CONFIGURATION_UPDATE;

/**
 * Event that shares server's configuration with clients.
 */
public class ConfigurationUpdate implements Event {

    private static final long serialVersionUID = 4801527815151807452L;
    private final int turnTimeout;
    private final int minNumPlayers;

    public ConfigurationUpdate(int turnTimeout, int minNumPlayers) {
        this.turnTimeout = turnTimeout;
        this.minNumPlayers = minNumPlayers;
    }

    public int getTurnTimeout() {
        return turnTimeout;
    }

    public int getMinNumPlayers() {
        return minNumPlayers;
    }

    @Override
    public EventType getEventType() {
        return CONFIGURATION_UPDATE;
    }
}
