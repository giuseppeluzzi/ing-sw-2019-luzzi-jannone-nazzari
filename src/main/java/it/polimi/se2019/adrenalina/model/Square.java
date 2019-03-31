package it.polimi.se2019.adrenalina.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Square extends Observable implements Target {

  private final int posX;
  private final int posY;
  private final PlayerColor color;

  private boolean spawnPoint;
  private AmmoCard ammoCard;

  private final BorderType[] borders;
  private final List<Weapon> weapons;

  public Square(int posX, int posY, PlayerColor color,
      BorderType edgeUp, BorderType edgeRight,
      BorderType edgeDown, BorderType edgeLeft) {

    this.posX = posX;
    this.posY = posY;
    this.color = color;

    spawnPoint = false;
    ammoCard = null;

    borders = new BorderType[4];
    borders[Direction.NORTH.ordinal()] = edgeUp;
    borders[Direction.EAST.ordinal()] = edgeRight;
    borders[Direction.SOUTH.ordinal()] = edgeDown;
    borders[Direction.WEST.ordinal()] = edgeLeft;

    weapons = new ArrayList<>();
  }

  @Override
  public boolean isPlayer() {
    return false;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public PlayerColor getColor() {
    return color;
  }

  public boolean isSpawnPoint() {
    return spawnPoint;
  }

  public void setSpawnPoint(boolean spawnPoint) {
    this.spawnPoint = spawnPoint;
  }

  public boolean hasAmmoCard() {
    return ammoCard != null;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }

  public void setAmmoCard(AmmoCard ammoCard) {
    this.ammoCard = ammoCard;
  }

  public BorderType getEdge(Direction direction) {
    return borders[direction.ordinal()];
  }

  public List<Weapon> getWeapons() {
    // TODO: weapons is mutable
    return new ArrayList<>();
  }

  public void addWeapon(Weapon weapon) {
    // TODO: exception if this square is not a spawnpoint
    weapons.add(weapon);
  }
}
