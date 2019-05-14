package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board extends Observable implements Serializable {
  private final Square[][] grid;
  private BoardStatus status;
  private boolean finalFrenzyActive;
  private boolean finalFrenzySelected;
  private long turnStartTime;
  private PlayerColor currentPlayer;
  private final List<Player> players;

  private final List<Weapon> weapons;
  private final List<Weapon> usedWeapons;

  private final List<PowerUp> powerUps;
  private final List<PowerUp> usedPowerUps;

  private final List<Player> doubleKills;
  private final List<Kill> killShots;

  private final boolean publicCopy;
  private final boolean publicCopyHasWeapons;

  public Board() {
    grid = new Square[4][3];

    currentPlayer = null;
    status = BoardStatus.LOBBY;
    players = new ArrayList<>();

    weapons = new ArrayList<>();
    usedWeapons = new ArrayList<>();
    powerUps = new ArrayList<>();
    usedPowerUps = new ArrayList<>();
    doubleKills = new ArrayList<>();
    killShots = new ArrayList<>();
    publicCopy = false;
    publicCopyHasWeapons = false;
  }

  /**
   * Copy constructor, creates an exact copy of a Board.
   * @param board the Board to be cloned, has to be not null.
   * @param publicCopy if true, a public copy of the Board will be created
   * instead of a clone. The public copy will not contain players' private
   * information.
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
    usedWeapons = new ArrayList<>();
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
    for (Weapon weapon : board.usedWeapons) {
      usedWeapons.add(new Weapon(weapon));
    }
    for (PowerUp powerUp : board.powerUps) {
      powerUps.add(powerUp.copy());
    }
    for (PowerUp powerUp : board.usedPowerUps) {
      usedPowerUps.add(powerUp.copy());
    }
  }

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
   * @param x x coordinate.
   * @param y y coordinate.
   * @param square Square to add, must not be null.
   * @throws IllegalArgumentException thrown if x or y are not within the size
   * of the board.
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
   * @param x x coordinate.
   * @param y y coordinate.
   * @return the requested Square.
   * @throws IllegalArgumentException thrown if x or y are not within the size
   * of the board.
   */
  public Square getSquare(int x, int y) {
    if (x < 0 ||  x > 3 || y < 0 || y > 2) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    return grid[x][y];
  }

  /**
   * Retrieves a list of square of the current map
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

  public PlayerColor getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(PlayerColor player) {
    currentPlayer = player;
  }

  public boolean isFinalFrenzyActive() {
    return finalFrenzyActive;
  }

  public void setFinalFrenzyActive(boolean finalFrenzyActive) {
    this.finalFrenzyActive = finalFrenzyActive;
  }

  public boolean isFinalFrenzySelected() {
    return finalFrenzySelected;
  }

  public void setFinalFrenzySelected(boolean finalFrenzySelected) {
    this.finalFrenzySelected = finalFrenzySelected;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
  }

  /**
   * Marks a weapon as used by moving it from the weapons list to the
   * usedWeapons list.
   * @param weapon weapon to be marked as used.
   * @throws  IllegalArgumentException thrown if weapon is not in the weapons
   * list.
   */
  public void useWeapon(Weapon weapon) {
    if (! weapons.contains(weapon)) {
      throw new IllegalArgumentException("Weapon not present");
    }
    weapons.remove(weapon);
    usedWeapons.add(weapon);
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  public List<Weapon> getUsedWeapons() {
    return new ArrayList<>(usedWeapons);
  }

  public void addPowerUp(PowerUp powerup) {
    powerUps.add(powerup);
  }

  /**
   * Marks a powerUp as used by moving it from the powerUps list to the
   * usedPowerUps list.
   * @param powerUp the powerUp to be marked as used.
   * @exception  IllegalArgumentException thrown if powerUp is not in the powerUps
   * list.
   */
  public void usePowerUp(PowerUp powerUp) {
    if (! powerUps.contains(powerUp)) {
      throw new IllegalArgumentException("PowerUp not present");
    }
    powerUps.remove(powerUp);
    usedPowerUps.add(powerUp);
  }

  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  public List<PowerUp> getUsedPowerUps() {
    return new ArrayList<>(usedPowerUps);
  }

  public void addDoubleKill(Player player) {
    doubleKills.add(player);
  }

  public List<Player> getDoubleKills() {
    return new ArrayList<>(doubleKills);
  }

  public void addKillShot(Kill kill) {
    killShots.add(kill);
  }

  public List<Kill> getKillShots() {
    return new ArrayList<>(killShots);
  }

  public long getTurnStartTime() {
    return turnStartTime;
  }

  public void setTurnStartTime(long turnStartTime) {
    this.turnStartTime = turnStartTime;
  }

  public boolean isDominationBoard() {
    return false;
  }

  public BoardStatus getStatus() {
    return status;
  }

  public void setStatus(BoardStatus status) {
    this.status = status;
  }

  public boolean hasWeapons() {
    if (publicCopy) {
      return publicCopyHasWeapons;
    }
    return !weapons.isEmpty();
  }

  /**
   * Retrieves a player by its color.
   * @throws  IllegalArgumentException thrown if there are no players of the
   * specified color.
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
   * Retrieves a list of player in a specified Square.
   * @param square the square to consider, which must not be null.
   * @return the list of players.
   */
  public List<Player> getPlayersInSquare(Square square) {
    if (square == null) {
      throw new IllegalArgumentException("Argument square cannot be null");
    }
    List<Player> output = new ArrayList<>();
    for (Player player : players) {
      if (player.getSquare() == square) {
        output.add(player);
      }
    }
    return output;
  }

  public void removePlayer(PlayerColor color) {
    players.remove(getPlayerByColor(color));
  }

  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy()).create();
    return gson.toJson(this);
  }

  /**
   * Create Board object from json formatted String
   * @param json json input String, can't be null
   * @return Board
   */
  public static Board deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    Board board = gson.fromJson(json, Board.class);
    for (Square square : board.getSquares()) {
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
