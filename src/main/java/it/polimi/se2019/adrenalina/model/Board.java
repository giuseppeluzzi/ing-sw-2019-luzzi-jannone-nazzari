package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.List;

public class Board extends Observable {
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
    grid = new Square[3][4];

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
    if (board == null) {
      throw new IllegalArgumentException("Argument board cannot be null");
    }
    this.publicCopy = publicCopy;
    publicCopyHasWeapons = board.hasWeapons();

    grid = new Square[3][4];
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 4; y++) {
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

    if (! publicCopy) {
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
   * Adds a square to the board.
   * @param x x coordinate.
   * @param y y coordinate.
   * @param square Square to add.
   * @throws IllegalArgumentException thrown if x or y are not within the size
   * of the board.
   */
  public void setSquare(int x, int y, Square square) {
    if (x < 0 ||  x > 2 || y < 0 || y > 3) {
      throw new IllegalArgumentException("Invalid square coordinates");
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
    if (x < 0 ||  x > 2 || y < 0 || y > 3) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    return grid[x][y];
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
   * @throws IllegalArgumentException thrown if weapon is not in the weapons
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
   * @throws IllegalArgumentException thrown if powerUp is not in the powerUps
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
   * @throws IllegalArgumentException thrown if there are no players of the
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

  public void removePlayer(PlayerColor color) {
    players.remove(getPlayerByColor(color));
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Create Board object from json formatted String
   * @param json json input String, can't be null
   * @return Board
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static Board deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Board.class);
  }
}
