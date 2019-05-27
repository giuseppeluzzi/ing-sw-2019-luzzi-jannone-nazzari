package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class OwnPowerUpUpdate implements Event {

  private static final long serialVersionUID = 7471834268745266766L;
  private final String name;
  private final AmmoColor powerUpColor;

  public OwnPowerUpUpdate(String name, AmmoColor powerUpColor) {
    this.name = name;
    this.powerUpColor = powerUpColor;
  }

  public String getName() {
    return name;
  }

  public AmmoColor getPowerUpColor() {
    return powerUpColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.OWN_POWER_UP_UPDATE;
  }
}
