package it.polimi.se2019.adrenalina.controller.event.invocations;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.EventType;

public class SwitchToFinalFrenzyInvocation implements Invocation {

  private static final long serialVersionUID = -1499996498562483140L;
  private final PlayerColor playerColor;

  public SwitchToFinalFrenzyInvocation(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.SWITCH_TO_FINAL_FRENZY_INVOCATION;
  }
}
