package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class defining the game board
 */
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
   * Copy constructor
   * @param board Board object that has to be copied, can't be null
   * @param publicCopy boolean value indicating if the copy should be private or public.
   * If true a public copy will be made containg only public informations
   * @exception IllegalArgumentException thrown if board argument is null
   */
  public Board(Board board, boolean publicCopy) {
    if (board == null) {
      throw new IllegalArgumentException("Argument board can't be null");
    }
    this.publicCopy = publicCopy;
    publicCopyHasWeapons = board.hasWeapons();

    grid = new Square[3][4];
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 4; y++) {
        grid[x][y] = board.getSquare(x, y);
      }
    }

    players = board.getPlayers();

    if (publicCopy) {
      weapons = new ArrayList<>();
      usedWeapons = new ArrayList<>();
      powerUps = new ArrayList<>();
      usedPowerUps = new ArrayList<>();
    } else {
      weapons = board.getWeapons();
      usedWeapons = board.getUsedWeapons();
      powerUps = board.getPowerUps();
      usedPowerUps = board.getUsedPowerUps();
    }

    doubleKills = board.getDoubleKills();
    killShots = board.getKillShots();

    status = board.status;
    finalFrenzyActive = board.finalFrenzyActive;
    finalFrenzySelected = board.finalFrenzySelected;
    turnStartTime = board.turnStartTime;
    currentPlayer = board.currentPlayer;
  }

  /**
   * Insert given square in the board
   * @param x x coordinate
   * @param y y coordinate
   * @param square inserted square
   * @exception IllegalArgumentException thrown if x or y are not within the size of the grid
   */
  public void setSquare(int x, int y, Square square) {
    if (x < 0 ||  x > 2 || y < 0 || y > 3) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    grid[x][y] = square;
  }

  /**
   * Get specific square
   * @param x x coordinate
   * @param y y coordinate
   * @return square found at the specified location
   * @exception IllegalArgumentException thrown if x or y are not within the size of the grid
   */
  public Square getSquare(int x, int y) {
    if (x < 0 ||  x > 2 || y < 0 || y > 3) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    return new Square(grid[x][y]);
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
    List<Player> output = new ArrayList<>();
    for (Player player : players) {
      output.add(player);
    }
    return output;
  }

  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
  }

  /**
   * Use a weapon and moves it from weapons list to usedWeapons list
   * @param weapon weapon to be used
   * @exception IllegalArgumentException thrown if weapon argument is not in weapons list
   */
  public void useWeapon(Weapon weapon) {
    if (! weapons.contains(weapon)) {
      throw new IllegalArgumentException("Weapon not present");
    }
    weapons.remove(weapon);
    usedWeapons.add(weapon);
  }

  public List<Weapon> getWeapons() {
    List<Weapon> output = new ArrayList<>();
    for (Weapon weapon : weapons) {
      output.add(new Weapon(weapon));
    }
    return output;
  }

  public List<Weapon> getUsedWeapons() {
    List<Weapon> output = new ArrayList<>();
    for (Weapon weapon : usedWeapons) {
      output.add(new Weapon(weapon));
    }
    return output;
  }

  public void addPowerUp(PowerUp powerup) {
    powerUps.add(powerup);
  }

  /**
   * Use a powerup and moves it from powerUps list to usedPowerUps list
   * @param powerup powerup to be used
   * @exception IllegalArgumentException thrown if powerup argument is not in powerup list
   */
  public void usePowerUp(PowerUp powerup) {
    if (! powerUps.contains(powerup)) {
      throw new IllegalArgumentException("PowerUp not present");
    }
    powerUps.remove(powerup);
    usedPowerUps.add(powerup);
  }

  public List<PowerUp> getPowerUps() {
    // TODO: powerUps is mutable
    return new ArrayList<>();
  }

  public List<PowerUp> getUsedPowerUps() {
    // TODO: usedPowerUps is mutable
    return new ArrayList<>();
  }

  public void addDoubleKill(Player player) {
    doubleKills.add(player);
  }

  public List<Player> getDoubleKills() {
    List<Player> output = new ArrayList<>();
    for (Player player : doubleKills) {
      output.add(new Player(player, false));
    }
    return output;
  }

  public void addKillShot(Kill kill) {
    killShots.add(kill);
  }

  public List<Kill> getKillShots() {
    List<Kill> output = new ArrayList<>();
    for (Kill kill : killShots) {
      output.add(new Kill(kill));
    }
    return output;
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
   * Return a Player object of a specified color
   * @param color player color
   * @return Player
   * @exception IllegalArgumentException thrown if there's no player of the specified color
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

  /**
   * Create json serialization of a Board object
   * @return String
   */
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Create Board object from json formatted String
   * @param json json input String
   * @return Board
   */
  public static Board deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Board.class);
  }
}
