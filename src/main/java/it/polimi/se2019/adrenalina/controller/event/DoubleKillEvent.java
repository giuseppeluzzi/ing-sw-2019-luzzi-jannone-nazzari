package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Kill;

public class DoubleKillEvent implements Event {
  private final Kill kill;

  public DoubleKillEvent(Kill kill) {
    this.kill = kill;
  }

  @Override
  public String getEventName() {
    return "DoubleKill";
  }

  public Kill getKill() {
    return kill;
  }
}
