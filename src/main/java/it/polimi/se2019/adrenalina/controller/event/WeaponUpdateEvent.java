package it.polimi.se2019.adrenalina.controller.event;

public class WeaponUpdateEvent implements Event {

  private static final long serialVersionUID = 7227880409446405882L;
  private final int squareX;
  private final int squareY;
  private final String weaponName;
  private final boolean remove;

  public WeaponUpdateEvent(int squareX, int squareY, String weaponName, boolean remove) {
    this.squareX = squareX;
    this.squareY = squareY;
    this.weaponName = weaponName;
    this.remove = remove;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }

  public String getWeaponName() {
    return weaponName;
  }

  public boolean isRemove() {
    return remove;
  }

  @Override
  public EventType getEventType() {
    return EventType.WEAPON_UPDATE_EVENT;
  }
}
