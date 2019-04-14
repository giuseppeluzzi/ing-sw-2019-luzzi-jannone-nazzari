package it.polimi.se2019.adrenalina.model;
import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Square extends Observable implements Target {

  private final int posX;
  private final int posY;
  private final PlayerColor color;

  private boolean spawnPoint;
  private AmmoCard ammoCard;

  private final HashMap<Direction, BorderType> borders;
  private final List<Weapon> weapons;

  public Square(int posX, int posY, PlayerColor color,
      BorderType edgeUp, BorderType edgeRight,
      BorderType edgeDown, BorderType edgeLeft) {

    this.posX = posX;
    this.posY = posY;
    this.color = color;

    spawnPoint = false;
    ammoCard = null;

    borders = new HashMap<>();
    borders.put(Direction.NORTH, edgeUp);
    borders.put(Direction.EAST, edgeRight);
    borders.put(Direction.SOUTH, edgeDown);
    borders.put(Direction.WEST, edgeLeft);

    weapons = new ArrayList<>();
  }

  public Square(Square square) {
    posX = square.posX;
    posY = square.posY;
    color = square.color;

    borders = new HashMap<>();
    borders.put(Direction.NORTH, square.getEdge(Direction.NORTH));
    borders.put(Direction.EAST, square.getEdge(Direction.EAST));
    borders.put(Direction.SOUTH, square.getEdge(Direction.SOUTH));
    borders.put(Direction.WEST, square.getEdge(Direction.WEST));

    weapons = square.getWeapons();
    ammoCard = square.ammoCard;
    spawnPoint = square.spawnPoint;
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
    List<Weapon> output = new ArrayList<>();
    for (Weapon weapon : weapons) {
      output.add(new Weapon(weapon));
    }
    return output;
  }

  public void addWeapon(Weapon weapon) {
    if (! spawnPoint) {
      throw new IllegalStateException("Square is not a spawnPoint");
    }
    weapons.add(weapon);
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static Square deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Square.class);
  }
}
