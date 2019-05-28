package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerScoreUpdate implements Event {

  private static final long serialVersionUID = -3766423338803118999L;
  private final int score;
  private final PlayerColor playerColor;

  public PlayerScoreUpdate(PlayerColor playerColor, int score) {
    this.score = score;
    this.playerColor = playerColor;
  }

  public int getScore() {
    return score;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SCORE_UPDATE;
  }
}
