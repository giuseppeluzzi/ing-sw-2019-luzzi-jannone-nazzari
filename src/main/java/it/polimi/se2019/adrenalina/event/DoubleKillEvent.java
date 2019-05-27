package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.model.Kill;

public class DoubleKillEvent implements Event {

  private static final long serialVersionUID = 7819216682678576756L;
  private final Kill kill;

  public DoubleKillEvent(Kill kill) {
    this.kill = kill;
  }

  public Kill getKill() {
    return kill;
  }

  @Override
  public EventType getEventType() {
    return EventType.DOUBLE_KILL_EVENT;
  }
}
