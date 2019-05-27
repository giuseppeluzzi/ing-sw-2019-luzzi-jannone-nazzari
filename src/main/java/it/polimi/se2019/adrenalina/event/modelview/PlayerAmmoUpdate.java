package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerAmmoUpdate implements Event {

  private static final long serialVersionUID = -7574479394997959563L;
  private final int blue;
  private final int red;
  private final int yellow;

  public PlayerAmmoUpdate(int blue, int red, int yellow) {
    this.blue = blue;
    this.red = red;
    this.yellow = yellow;
  }

  public int getBlue() {
    return blue;
  }

  public int getRed() {
    return red;
  }

  public int getYellow() {
    return yellow;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_AMMO_UPDATE;
  }
}
