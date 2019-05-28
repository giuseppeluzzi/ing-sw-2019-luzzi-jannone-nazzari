package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerSetColorEvent implements Event {

  private static final long serialVersionUID = 203613633799913352L;
  private final PlayerColor playerColor;

  public PlayerSetColorEvent(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SET_COLOR;
  }
}
