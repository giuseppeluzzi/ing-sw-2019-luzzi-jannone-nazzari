package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Kill;

public class DoubleKillEvent implements Event {
  private final Kill kill;

  public DoubleKillEvent(Kill kill) {
    this.kill = kill;
  }

  public Kill getKill() {
    return kill;
  }
}
