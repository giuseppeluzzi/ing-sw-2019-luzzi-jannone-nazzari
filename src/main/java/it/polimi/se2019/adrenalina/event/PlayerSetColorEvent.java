package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

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
