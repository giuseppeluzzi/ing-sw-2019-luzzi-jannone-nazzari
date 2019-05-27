package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerKillScoreUpdate implements Event {

  private static final long serialVersionUID = -4970596236550079643L;
  private final int killScore;

  public PlayerKillScoreUpdate(int killScore) {
    this.killScore = killScore;
  }

  public int getKillScore() {
    return killScore;
  }

  @Override
  public EventType getEventType() {
    return null;
  }
}
