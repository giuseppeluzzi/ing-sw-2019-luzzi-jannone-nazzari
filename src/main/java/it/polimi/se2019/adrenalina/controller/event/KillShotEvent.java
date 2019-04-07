package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Kill;

public class KillShotEvent implements Event {
  private final Kill kill;

  public KillShotEvent(Kill kill) {
    this.kill = kill;
  }

  @Override
  public String getEventName() {
    return "KillShot";
  }

  public Kill getKill() {
    return kill;
  }

}
