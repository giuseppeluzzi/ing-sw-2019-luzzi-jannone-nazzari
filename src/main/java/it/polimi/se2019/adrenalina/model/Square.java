package it.polimi.se2019.adrenalina.model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Square extends Observable implements Target, Serializable {

  private final int posX;
  private final int posY;
  private final SquareColor color;
  @NotExpose
  private HashMap<Direction, Square> neighbours;

  private boolean spawnPoint;
  private AmmoCard ammoCard;

  private final HashMap<Direction, BorderType> borders;
  private final List<Weapon> weapons;

  public Square(int posX, int posY, SquareColor color,
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

    neighbours = new HashMap<>();
    weapons = new ArrayList<>();
  }

  /**
   * Copy constructor, creates an exact copy of square.
   * @param square, Square object that has to be copied, can't be null
   * @exception IllegalArgumentException thrown if square argument is null
   */
  public Square(Square square) {
    if (square == null) {
      throw new IllegalArgumentException("Argument square can't be null");
    }
    posX = square.posX;
    posY = square.posY;
    color = square.color;

    borders = new HashMap<>();
    borders.put(Direction.NORTH, square.getEdge(Direction.NORTH));
    borders.put(Direction.EAST, square.getEdge(Direction.EAST));
    borders.put(Direction.SOUTH, square.getEdge(Direction.SOUTH));
    borders.put(Direction.WEST, square.getEdge(Direction.WEST));

    neighbours = new HashMap<>(square.neighbours);

    weapons = new ArrayList<>();
    for (Weapon weapon : weapons) {
      weapons.add(new Weapon(weapon));
    }

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

  public SquareColor getColor() {
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
    return new ArrayList<>(weapons);
  }

  public Square getNeighbour(Direction direction) {
    return neighbours.get(direction);
  }

  public void setNeighbour(Direction direction, Square square) {
    if (square == null) {
      throw new IllegalArgumentException("Argument square cannot be null");
    }
    neighbours.put(direction, square);
  }

  public void resetNeighbours() {
    neighbours = new HashMap<>();
  }

  /**
   * Verify if a square is visible
   * @param square a square to be checked
   * @return visibility condition
   */
  public boolean isVisible(Square square) {
    // TODO: is square visible from this?
    return true;
  }

  /**
   * Calculate the distance from a specific square
   * @param square a specific square
   * @return distance from the square
   */
  public int getDistance(Square square) {
    return Math.abs(posX - square.posX) + Math.abs(posY - square.posY);
  }

  /**
   * Calculate the cardinal direction between two squares
   * @param square another square
   * @return direction between the squares
   * @throws InvalidSquareException if the squares are equal or if the squares aren't aligned to a
   * cardinal direction
   */
  public Direction getCardinalDirection(Square square) throws InvalidSquareException {
    int diffX = posX - square.posX;
    int diffY = posY - square.posY;

    if (diffX == 0 && diffY == 0) {
      throw new InvalidSquareException("Must be a different square");
    }

    if (diffX == 0) {
      if (diffY < 0) {
        return Direction.NORTH;
      } else {
        return Direction.SOUTH;
      }
    }
    if (diffY == 0) {
      if (diffX < 0) {
        return Direction.EAST;
      } else {
        return Direction.WEST;
      }
    }
    throw new InvalidSquareException("Square is not on a cardinal direction");
  }

  /**
   * Add a Weapon object to the square, only if it's a spawn point
   * @param weapon, weapon that will be added
   * @exception IllegalStateException thrown if selected square is not a spawn point
   */
  public void addWeapon(Weapon weapon) {
    if (! spawnPoint) {
      throw new IllegalStateException("Square is not a spawnPoint");
    }
    weapons.add(weapon);
  }

  @Override
  public Square getSquare() {
    return this;
  }

  @Override
  public boolean equals (Object object) {
    return object instanceof Square
        && ((Square) object).posX == posX
        && ((Square) object).posY == posY;
  }

  @Override
  public int hashCode() {
    return posX + posY + color.ordinal();
  }

  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy()).create();
    return gson.toJson(this);
  }

  /**
   * Create Square object from json formatted String
   * @param json json input String, can't be null
   * @return Square
   * @exception  IllegalArgumentException thrown if argument json is null
   */
  public static Square deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    Square square = gson.fromJson(json, Square.class);
    square.neighbours = new HashMap<>();
    return square;
  }
}
