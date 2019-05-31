package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerDeathUpdate implements Event {

  private static final long serialVersionUID = -8987048353974663246L;
  private final PlayerColor playerColor;
  private final PlayerColor killerColor;

  public PlayerDeathUpdate(PlayerColor playerColor, PlayerColor killerColor) {
    this.playerColor = playerColor;
    this.killerColor = killerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PlayerColor getKillerColor() {
    return killerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DEATH_UPDATE;
  }
}
