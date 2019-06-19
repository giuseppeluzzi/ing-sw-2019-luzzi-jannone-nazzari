package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player unsuspends himself.
 */
public class PlayerUnsuspendEvent implements Event {
    private static final long serialVersionUID = 5239856920032659236L;

    private final PlayerColor playerColor;

    public PlayerUnsuspendEvent(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public EventType getEventType() {
        return EventType.PLAYER_UNSUSPEND_EVENT;
    }
}
