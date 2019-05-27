package it.polimi.se2019.adrenalina.controller.event.invocations;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.EventType;

public class ShowDeath implements Invocation {

  private static final long serialVersionUID = -1913436647104483700L;
  private final PlayerColor playerColor;

  public ShowDeath(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_DEATH_INVOCATION;
  }
}
