package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerScoreUpdate implements Event {

  private static final long serialVersionUID = -3766423338803118999L;
  private final int score;

  public PlayerScoreUpdate(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SCORE_UPDATE;
  }
}
