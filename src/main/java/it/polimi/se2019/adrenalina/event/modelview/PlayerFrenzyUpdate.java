package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerFrenzyUpdate implements Event {

  private static final long serialVersionUID = -387739189095181951L;
  private final PlayerColor playerColor;
  private final boolean frenzy;

  public PlayerFrenzyUpdate(PlayerColor playerColor, boolean frenzy) {
    this.playerColor = playerColor;
    this.frenzy = frenzy;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public boolean isFrenzy() {
    return frenzy;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_FRENZY_UPDATE;
  }
}
