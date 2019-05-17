package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidWeaponException;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a game board.
 */
public class Board extends Observable implements Serializable {

  private static final long serialVersionUID = 6249423437530616554L;
  private final Square[][] grid;
  private BoardStatus status;
  private boolean finalFrenzyActive;
  private boolean finalFrenzySelected;
  private long turnStartTime;
  private PlayerColor currentPlayer;
  private final List<Player> players;

  private final List<Weapon> weapons;
  private final List<Weapon> takenWeapons;

  private final List<PowerUp> powerUps;
  private final List<PowerUp> usedPowerUps;

  private final List<Player> doubleKills;
  private final List<Kill> killShots;

  private final boolean publicCopy;
  private final boolean publicCopyHasWeapons;

  /**
   * Class constructor. Creates an empty Board to which objects have to be added.
   */
  public Board() {
    grid = new Square[4][3];

    currentPlayer = null;
    status = BoardStatus.LOBBY;
    players = new ArrayList<>();

    weapons = new ArrayList<>();
    takenWeapons = new ArrayList<>();
    powerUps = new ArrayList<>();
    usedPowerUps = new ArrayList<>();
    doubleKills = new ArrayList<>();
    killShots = new ArrayList<>();
    publicCopy = false;
    publicCopyHasWeapons = false;
  }

  /**
   * Copy constructor, creates an exact copy of a Board.
   * @param board the Board to be cloned, has to be not null
   * @param publicCopy if true, a public copy of the Board will be created
   * instead of a clone. The public copy will not contain players' private
   * information
   */
  public Board(Board board, boolean publicCopy) {
    // TODO: copy observers
    if (board == null) {
      throw new IllegalArgumentException("Argument board cannot be null");
    }
    this.publicCopy = publicCopy;
    publicCopyHasWeapons = board.hasWeapons();

    grid = new Square[4][3];
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        grid[x][y] = new Square(board.grid[x][y]);
      }
    }

    players = new ArrayList<>();
    for (Player player : board.players) {
      players.add(new Player(player, publicCopy));
    }

    weapons = new ArrayList<>();
    takenWeapons = new ArrayList<>();
    powerUps = new ArrayList<>();
    usedPowerUps = new ArrayList<>();

    setObservers(board.getObservers());

    if (! publicCopy) {
      copyPrivateAttributes(board);
    }

    doubleKills = new ArrayList<>();
    for (Player player : board.doubleKills) {
      doubleKills.add(new Player(player, publicCopy));
    }

    killShots = new ArrayList<>();
    for (Kill kill : board.killShots) {
      killShots.add(new Kill(kill));
    }

    status = board.status;
    finalFrenzyActive = board.finalFrenzyActive;
    finalFrenzySelected = board.finalFrenzySelected;
    turnStartTime = board.turnStartTime;
    currentPlayer = board.currentPlayer;
  }

  /**
   * Copies private attributes as required by the copy constructor.
   * @param board the Board to be cloned.
   */
  private void copyPrivateAttributes(Board board) {
    for (Weapon weapon : board.weapons) {
      weapons.add(new Weapon(weapon));
    }
    for (Weapon weapon : board.takenWeapons) {
      takenWeapons.add(new Weapon(weapon));
    }
    for (PowerUp powerUp : board.powerUps) {
      powerUps.add(powerUp.copy());
    }
    for (PowerUp powerUp : board.usedPowerUps) {
      usedPowerUps.add(powerUp.copy());
    }
  }

  // TODO: documentation
  public void clearMap() {
    if (status != BoardStatus.LOBBY) {
      throw new IllegalStateException("Can't clear the map during a game");
    }
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        // TODO
      }
    }
  }

  /**
   * Adds a square to the board. Besides adding it to the grid, it sets {@code east}, {@code west},
   * {@code north} and {@code south} attributes and it also changes its neighbour squares
   * accordingly.
   * @param x x coordinate
   * @param y y coordinate
   * @param square Square to add, must not be null
   * @throws IllegalArgumentException thrown if x or y are not within the size
   * of the board
   */
  public void setSquare(int x, int y, Square square) {
    if (x < 0 ||  x > 3 || y < 0 || y > 2) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    if (square == null) {
      throw new IllegalArgumentException("Square parameter must not be null");
    }
    Square neighbour;
    if (x > 0 && square.getEdge(Direction.WEST) != BorderType.WALL) {
      neighbour = getSquare(x - 1, y);
      if (neighbour != null) {
       neighbour.setNeighbour(Direction.EAST, square);
       square.setNeighbour(Direction.WEST, neighbour);
      }
    }
    if (x < 3 && square.getEdge(Direction.EAST) != BorderType.WALL) {
      neighbour = getSquare(x + 1, y);
      if (neighbour != null) {
        neighbour.setNeighbour(Direction.WEST, square);
        square.setNeighbour(Direction.EAST, neighbour);
      }
    }
    if (y > 0 && square.getEdge(Direction.NORTH) != BorderType.WALL) {
      neighbour = getSquare(x, y - 1);
      if (neighbour != null) {
        neighbour.setNeighbour(Direction.SOUTH, square);
        square.setNeighbour(Direction.NORTH, neighbour);
      }
    }
    if (y < 2 && square.getEdge(Direction.SOUTH) != BorderType.WALL) {
      neighbour = getSquare(x, y + 1);
      if (neighbour != null) {
        neighbour.setNeighbour(Direction.NORTH, square);
        square.setNeighbour(Direction.SOUTH, neighbour);
      }
    }
    grid[x][y] = square;
  }

  /**
   * Retrieves a square from the board given its coordinates.
   * @param x x coordinate
   * @param y y coordinate
   * @return the requested Square
   * @throws IllegalArgumentException thrown if x or y are not within the size
   * of the board
   */
  public Square getSquare(int x, int y) {
    if (x < 0 ||  x > 3 || y < 0 || y > 2) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    return grid[x][y];
  }

  /**
   * Retrieves a list of squares in the current map.
   * @return a list of Square
   */
  public List<Square> getSquares() {
    List<Square> squares = new ArrayList<>();
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        if (grid[x][y] != null) {
          squares.add(grid[x][y]);
        }
      }
    }
    return squares;
  }

  /**
   * Returns the player who is currently playing its turn.
   * @return the Player currently playing
   */
  public PlayerColor getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Sets the player who is currently playing its turn.
   * @param player the Player to set
   */
  public void setCurrentPlayer(PlayerColor player) {
    currentPlayer = player;
  }

  /**
   * Returns whether the current game mode is "Final Frenzy".
   * @return true if the current game mode is "Final Frenzy", false otherwise
   */
  public boolean isFinalFrenzyActive() {
    return finalFrenzyActive;
  }

  /**
   * Sets or unsets the current game mode to "Final Frenzy".
   * @param finalFrenzyActive true if the game mode has to be set to "Final Frenzy", false otherwhise
   */
  public void setFinalFrenzyActive(boolean finalFrenzyActive) {
    this.finalFrenzyActive = finalFrenzyActive;
  }

  /**
   * Returns whether the current game is set up to use "Final Frenzy" mode at the end.
   * @return true if the game is set up to use "Final Frenzy" mode at the end, false otherwise
   */
  public boolean isFinalFrenzySelected() {
    return finalFrenzySelected;
  }

  /**
   * Sets up the current game to use "Final Frenzy" mode at the end.
   * @param finalFrenzySelected true if the game shoud use "Final Frenzy" mode at the end, false
   * otherwise
   */
  public void setFinalFrenzySelected(boolean finalFrenzySelected) {
    this.finalFrenzySelected = finalFrenzySelected;
  }

  /**
   * Adds a player to the board.
   * @param player the player to add
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * Returns a List of Players currently in the Board.
   * @return a List of Players currently in the Board.
   */
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  /**
   * Adds a Weapon to the board.
   * @param weapon the Weapon to add
   */
  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
  }

  /**
   * Marks a Weapon as used by moving it from the weapons list to the
   * takenWeapons list.
   * @param weapon Weapon to be marked as used
   * @throws  InvalidWeaponException thrown if weapon is not in the Board
   */
  public void takeWeapon(Weapon weapon) throws InvalidWeaponException {
    if (! weapons.contains(weapon)) {
      throw new InvalidWeaponException("Weapon not present");
    }
    weapons.remove(weapon);
    takenWeapons.add(weapon);
  }

  /**
   * Returns a List of Weapons in the Board.
   * @return a List of Weapons in the Board
   */
  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  /**
   * Returns a List of taken Weapons.
   * @return a List of taken Weapons.
   */
  public List<Weapon> getTakenWeapons() {
    return new ArrayList<>(takenWeapons);
  }


  /**
   * Adds a powerUp to the Board.
   * @param powerup the powerUp to add to the Board
   */
  public void addPowerUp(PowerUp powerup) {
    powerUps.add(powerup);
  }

  /**
   * Marks a powerUp as used by moving it from the powerUps list to the
   * usedPowerUps list.
   * @param powerUp the powerUp to be marked as used
   * @exception  IllegalArgumentException thrown if powerUp is not in the Board
   */
  public void usePowerUp(PowerUp powerUp) {
    if (! powerUps.contains(powerUp)) {
      throw new IllegalArgumentException("PowerUp not present");
    }
    powerUps.remove(powerUp);
    usedPowerUps.add(powerUp);
  }

  /**
   * Returns a List of powerUps in the Board.
   * @return a List of powerUps in the Board
   */
  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  /**
   * Returns a List of used powerUps in the Board.
   * @return a List of used powerUps in the Board
   */
  public List<PowerUp> getUsedPowerUps() {
    return new ArrayList<>(usedPowerUps);
  }

  /**
   * Adds a doubleKill to the Board.
   * @param player the Player who scored the doubleKill
   */
  public void addDoubleKill(Player player) {
    doubleKills.add(player);
  }

  /**
   * Returns a list of doubleKills in the Board.
   * @return a list of doubleKills in the Board
   */
  public List<Player> getDoubleKills() {
    return new ArrayList<>(doubleKills);
  }

  /**
   * Adds a killShot to the killshot track.
   * @param kill the killShot to add
   */
  public void addKillShot(Kill kill) {
    killShots.add(kill);
  }

  /**
   * Returns a list of doubleKills in the killshot track.
   * @return a list of doubleKills in the killshot track
   */
  public List<Kill> getKillShots() {
    return new ArrayList<>(killShots);
  }

  /**
   * Returns the timestamp at which the current turn started.
   * @return the timestamp at which the current turn started
   */
  public long getTurnStartTime() {
    return turnStartTime;
  }

  /**
   * Sets the timestamp at which the current turn started.
   * @param turnStartTime the timestamp to set
   */
  public void setTurnStartTime(long turnStartTime) {
    this.turnStartTime = turnStartTime;
  }

  /**
   * Returns whether the Board is a DominationBoard. Always true in the children class
   * {@code DominationBoard}, always false here.
   * @return true, since this is a normal Board and not a DominationBoard
   */
  public boolean isDominationBoard() {
    return false;
  }

  /**
   * Returns the current status of the Board.
   * @return the current Status of the Board
   */
  public BoardStatus getStatus() {
    return status;
  }

  /**
   * Sets the current Status of the Board.
   * @param status the Status to set
   */
  public void setStatus(BoardStatus status) {
    this.status = status;
  }

  /**
   * Returns whether the board has any Weapons left in the stack. This method works even if this
   * object is the publicCopy of a board (and thus has no Weapons saved into it), thanks to the
   * {@code publicCopyHasWeapons} attribute.
   * @return true if the board has any weapons left in the stack, false otherwise
   */
  public boolean hasWeapons() {
    if (publicCopy) {
      return publicCopyHasWeapons;
    }
    return !weapons.isEmpty();
  }

  /**
   * Retrieves a player by its color.
   * @throws IllegalArgumentException thrown if there are no players of the specified color
   */
  public Player getPlayerByColor(PlayerColor color) {
    for (Player player : players) {
      if (player.getColor() == color) {
        return player;
      }
    }
    throw new IllegalArgumentException("No such player");
  }

  /**
   * Removes the player of a given color.
   * @param color the color of the player to remove
   */
  public void removePlayer(PlayerColor color) {
    players.remove(getPlayerByColor(color));
  }

  /**
   * Return a Weapon whose name is the same as the specified one.
   * @param name Name of the weapon requested
   * @return Weapon whith name equals to "name", null if weapon does not exist
   */
  public Weapon getWeaponByName(String name) {
    List<Weapon> takenWeapons = getTakenWeapons();
    for (Weapon weapon : takenWeapons) {
      if (weapon.getName().equals(name)) {
        return weapon;
      }
    }
    return null;
  }

  /**
   * Gson serialization.
   * @return JSON string containing serialized object
   */
  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy()).create();
    return gson.toJson(this);
  }

  /**
   * Creates Board object from a JSON serialized object. This method also sets each Square's
   * neighbours and Players to the correct state.
   * @param json JSON input String
   * @return Board object
   */
  public static Board deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    Board board = gson.fromJson(json, Board.class);
    for (Square square : board.getSquares()) {
      square.resetPlayers();
      for (Player player : board.players) {
        if (player.getSquare() != null) {
          player.getSquare().addPlayer(player);
        }
      }
      square.resetNeighbours();
      if (square.getPosX() > 0 && square.getEdge(Direction.WEST) != BorderType.WALL && board.getSquare(square.getPosX() - 1, square.getPosY()) != null) {
        square.setNeighbour(Direction.WEST, board.getSquare(square.getPosX() - 1, square.getPosY()));
      }
      if (square.getPosX() < 3 && square.getEdge(Direction.EAST) != BorderType.WALL && board.getSquare(square.getPosX() + 1, square.getPosY()) != null) {
        square.setNeighbour(Direction.EAST, board.getSquare(square.getPosX() + 1, square.getPosY()));
      }
      if (square.getPosY() > 0 && square.getEdge(Direction.NORTH) != BorderType.WALL && board.getSquare(square.getPosX(), square.getPosY() - 1) != null) {
        square.setNeighbour(Direction.NORTH, board.getSquare(square.getPosX(), square.getPosY() - 1));
      }
      if (square.getPosY() < 2 && square.getEdge(Direction.SOUTH) != BorderType.WALL && board.getSquare(square.getPosX(), square.getPosY() + 1) != null) {
        square.setNeighbour(Direction.SOUTH, board.getSquare(square.getPosX(), square.getPosY() + 1));
      }
    }
    return board;
  }
}
