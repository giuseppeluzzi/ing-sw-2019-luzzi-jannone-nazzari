package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerDeathUpdate implements Event {

  private static final long serialVersionUID = -8987048353974663246L;
  private final PlayerColor playerColor;

  public PlayerDeathUpdate(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DEATH_UPDATE;
  }
}
