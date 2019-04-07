package it.polimi.se2019.adrenalina.model;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Square extends Observable implements Target {

  private final int posX;
  private final int posY;
  private final PlayerColor color;

  private boolean spawnPoint;
  private AmmoCard ammoCard;

  private final EnumMap<Direction, BorderType> borders;
  private final List<Weapon> weapons;

  public Square(int posX, int posY, PlayerColor color,
      BorderType edgeUp, BorderType edgeRight,
      BorderType edgeDown, BorderType edgeLeft) {

    this.posX = posX;
    this.posY = posY;
    this.color = color;

    spawnPoint = false;
    ammoCard = null;

    borders = new EnumMap<>(Direction.class);
    borders.put(Direction.NORTH, edgeUp);
    borders.put(Direction.EAST, edgeRight);
    borders.put(Direction.SOUTH, edgeDown);
    borders.put(Direction.WEST, edgeLeft);

    weapons = new ArrayList<>();
  }

  public Square(Square square) {
    // TODO: create a copy of square
    posX = square.posX;
    posY = square.posY;
    color = square.color;

    borders = new EnumMap<>(Direction.class);
    borders.put(Direction.NORTH, square.getEdge(Direction.NORTH));
    borders.put(Direction.EAST, square.getEdge(Direction.EAST));
    borders.put(Direction.SOUTH, square.getEdge(Direction.SOUTH));
    borders.put(Direction.WEST, square.getEdge(Direction.WEST));

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
    return borders.get(direction);
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
