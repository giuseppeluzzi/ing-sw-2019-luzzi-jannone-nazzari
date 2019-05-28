package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.EventType;

public class ShowDeathInvocation implements Invocation {

  private static final long serialVersionUID = -1913436647104483700L;
  private final PlayerColor playerColor;

  public ShowDeathInvocation(PlayerColor playerColor) {
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
