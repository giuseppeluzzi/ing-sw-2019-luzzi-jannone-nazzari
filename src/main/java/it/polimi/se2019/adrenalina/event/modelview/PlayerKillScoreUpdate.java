package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event sent when the kill score of a player is updated.
 */
public class PlayerKillScoreUpdate implements Event {

  private static final long serialVersionUID = -4970596236550079643L;
  private final PlayerColor playerColor;
  private final int killScore;

  public PlayerKillScoreUpdate(PlayerColor playerColor, int killScore) {
    this.killScore = killScore;
    this.playerColor = playerColor;
  }

  public int getKillScore() {
    return killScore;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return null;
  }
}
