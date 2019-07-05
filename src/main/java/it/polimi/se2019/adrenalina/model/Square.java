package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A board square.
 */
public class Square extends Observable implements Target {

  private static final long serialVersionUID = -4261049664345567097L;
  private final int posX;
  private final int posY;
  private final SquareColor color;
  @NotExpose
  private HashMap<Direction, Square> neighbours = new HashMap<>();
  @NotExpose
  private List<Player> players = new ArrayList<>();

  private boolean spawnPoint;
  private AmmoCard ammoCard;
  private final transient Board board;

  private final HashMap<Direction, BorderType> borders;
  @NotExpose
  private List<Weapon> weapons;

  public Square(int posX, int posY, SquareColor color, BorderType[] borders, Board board) {

    this.board = board;
    this.posX = posX;
    this.posY = posY;
    this.color = color;

    spawnPoint = false;
    ammoCard = null;

    this.borders = new HashMap<>();
    this.borders.put(Direction.NORTH, borders[0]);
    this.borders.put(Direction.EAST, borders[1]);
    this.borders.put(Direction.SOUTH, borders[2]);
    this.borders.put(Direction.WEST, borders[3]);

    neighbours = new HashMap<>();
    weapons = new ArrayList<>();
    players = new ArrayList<>();
  }

  /**
   * Copy constructor, creates an exact copy of square.
   * @param square, Square object that has to be copied, can't be null
   * @throws IllegalArgumentException thrown if square argument is null
   */
  public Square(Square square) {
    if (square == null) {
      throw new IllegalArgumentException("Argument square can't be null");
    }
    board = square.board;
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
    players = new ArrayList<>(square.players);
  }

  @Override
  public boolean isPlayer() {
    return false;
  }

  public Board getBoard() {
    return board;
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
    try {
      if (ammoCard == null) {
        notifyObservers(new SquareAmmoCardUpdate(posX, posY, 0,0,0,0));
      } else {
        notifyObservers(new SquareAmmoCardUpdate(posX, posY, ammoCard.getAmmo(AmmoColor.BLUE),
            ammoCard.getAmmo(AmmoColor.RED), ammoCard.getAmmo(AmmoColor.YELLOW),
            ammoCard.getPowerUp()));
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void setWeapons(List<Weapon> weapons) {
    this.weapons = new ArrayList<>(weapons);
    try {
      notifyObservers(new SquareWeaponUpdate(posX, posY, getWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
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

  /**
   * Add a square as neighbour in specified direction.
   * @param direction direction in which neighbour is placed
   * @param square neighbour square
   */
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
   * Get the player currently on this square.
   * @return the player currently on this square if any, null otherwise
   * @throws InvalidSquareException thrown if there are more than one player on this square
   */
  @Override
  public Player getPlayer() throws InvalidSquareException {
    if (getPlayers().size() == 1) {
      return getPlayers().get(0);
    } else if (getPlayers().isEmpty()) {
      return null;
    } else {
      throw new InvalidSquareException("In this square there are more than one player");
    }
  }

  public void resetPlayers() {
    players = new ArrayList<>();
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  /**
   * Add damages to the square in domination mode.
   * @param player the player who scored the damages
   * @param num the number of damages
   */
  @Override
  public void addDamages(PlayerColor player, int num, boolean powerup) {
    if (spawnPoint) {
      if (color == SquareColor.BLUE) {
        ((DominationBoard) board).addBlueDamage(player);
      } else if (color == SquareColor.RED) {
        ((DominationBoard) board).addRedDamage(player);
      } else if (color == SquareColor.YELLOW) {
        ((DominationBoard) board).addYellowDamage(player);
      } else {
        throw new IllegalStateException("Invalid square color");
      }
    }
  }

  @Override
  public void addTags(PlayerColor player, int num) {
    // Do nothing: squares don't care about tags
  }

  /**
   * Verify if a square is visible.
   * @param square a square to be checked
   * @return visibility condition
   */
  public boolean isVisible(Square square) {

    for (Direction direction : Direction.values()) {
      if (getNeighbour(direction) != null && getNeighbour(direction).getColor() == square.getColor()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Calculate the distance from a specific square.
   * @param square a specific square
   * @return distance from the square
   */
  public int getDistance(Square square) {
    Map<Square, Integer> nodes = bfs();
    return nodes.get(square);
  }

  /**
   * Creates an Hashmap where all the squares of the board are coupled with their distance from
   * the current square.
   * @return Specified Hashmap
   */
  private Map<Square, Integer> bfs() {
    ArrayDeque<Square> unvisitedNodes = new ArrayDeque<>();
    List<Square> visitedNodes = new ArrayList<>();
    Map<Square, Integer> nodes = new HashMap<>();
    unvisitedNodes.addLast(this);
    nodes.put(this, 0);

    while (!unvisitedNodes.isEmpty()) {

      Square current = unvisitedNodes.removeFirst();
      int currentValue = nodes.get(current);

      for (Direction direction : Direction.values()) {
        Square next = current.getNeighbour(direction);
        updateNodes(next, visitedNodes, nodes, unvisitedNodes, currentValue);
      }
      visitedNodes.add(current);
    }
    return nodes;
  }

  private void updateNodes(Square next, List<Square> visitedNodes,
      Map<Square, Integer> nodes, Deque<Square> unvisitedNodes, int currentValue) {
    if (next != null) {
      if (!visitedNodes.contains(next)) {
        unvisitedNodes.addLast(next);
      }

      if (nodes.containsKey(next)) {
        if (nodes.get(next) > currentValue + 1) {
          nodes.put(next, currentValue + 1);
        }
      } else {
        nodes.put(next, currentValue + 1);
      }
    }
  }

  /**
   * Calculate the cardinal direction between two squares.
   * @param square another square
   * @return direction between the squares, null if the input is the same square as this
   */
  public Direction getCardinalDirection(Square square) throws InvalidSquareException {
    int diffX = square.posX - posX;
    int diffY = square.posY - posY;

    if (diffX == 0 && diffY == 0) {
      return null;
    }

    if (diffX == 0) {
      if (diffY < 0) {
        return Direction.NORTH;
      } else {
        return Direction.SOUTH;
      }
    }
    if (diffY == 0) {
      if (diffX > 0) {
        return Direction.EAST;
      } else {
        return Direction.WEST;
      }
    }
    throw new InvalidSquareException("Square is not on a cardinal direction");
  }

  /**
   * Add a Weapon object to the square, only if it's a spawn point.
   * @param weapon, weapon that will be added
   * @throws IllegalStateException thrown if selected square is not a spawn point
   */
  public void addWeapon(Weapon weapon) {
    if (!spawnPoint) {
      throw new IllegalStateException("Square is not a spawnPoint");
    }
    weapons.add(weapon);
    try {
      notifyObservers(new SquareWeaponUpdate(posX, posY, getWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void removeWeapon(Weapon weapon) {
    Log.debug("removeWeapon for: " + weapon.getName());
    weapons.remove(weapon);
    try {
      notifyObservers(new SquareWeaponUpdate(posX, posY, getWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public Square getSquare() {
    return this;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Square
        && ((Square) object).posX == posX
        && ((Square) object).posY == posY;
  }

  @Override
  public int hashCode() {
    return posX + posY + color.ordinal();
  }

  /**
   * Check if x coordinate is within valid range.
   * @param x coordinate
   * @return true if valid, false otherwise
   */
  public static boolean isXValid(int x) {
    return x >= 0 && x < 4;
  }

  /**
   * Check if y coordinate is within valid range.
   * @param y coordinate
   * @return true if valid, false otherwise
   */
  public static boolean isYValid(int y) {
    return y >= 0 && y < 3;
  }

  public List<Square> getSquaresInRange(int min, int max, boolean fetch) {
    List<Square> output = new ArrayList<>();
    for (Square square : board.getSquares()) {
      if (getDistance(square) >= min && getDistance(square) <= max) {
        if (fetch) {
          if (square.spawnPoint || square.ammoCard != null) {
            output.add(square);
          }
        } else {
          output.add(square);
        }
      }
    }
    return output;
  }

  /**
   * Method that moves a target from it's square to the specified one, since a square can't be
   * moved it does nothing.
   * @param square where the target will be moved
   */
  @Override
  public void setSquare(Square square) {
    //do nothing since a square can't be moved
  }

  @Override
  public String getName() {
    return String.format("Punto di generazione (x: %d y: %d)", posX, posY);
  }
  /**
   * Interface method that returns the equivalent ansi color of the target
   * @return ansi color
   */
  @Override
  public ANSIColor getAnsiColor() {
    return color.getAnsiColor();
  }

  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy())
        .create();
    return gson.toJson(this);
  }

  /**
   * Create Square object from json formatted String
   * @param json json input String, can't be null
   * @return Square
   * @throws IllegalArgumentException thrown if argument json is null
   */
  public static Square deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    Square square = gson.fromJson(json, Square.class);
    square.neighbours = new HashMap<>();
    square.players = new ArrayList<>();
    square.weapons = new ArrayList<>();
    return square;
  }
}
