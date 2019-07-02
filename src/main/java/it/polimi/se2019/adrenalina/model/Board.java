package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.event.modelview.BoardAddPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasAmmoCardsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardRemovePlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSetSquareUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerMasterUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class describes a game board.
 */
public class Board extends Observable implements Serializable {

  private static final long serialVersionUID = 6249423437530616554L;
  private final Square[][] grid;
  private BoardStatus status;
  private long turnStartTime;
  private int mapId = 1;
  private int turnCounter = 1;
  private int skulls = 8;
  private String lastGameMessage;

  private PlayerColor currentPlayer;
  private final List<Player> players;

  private final List<Weapon> weapons;
  private final List<Weapon> takenWeapons;

  private final List<PowerUp> powerUps;
  private final List<PowerUp> takenPowerUps;

  private final List<AmmoCard> ammoCards;
  private final List<AmmoCard> takenAmmoCards;

  private int turnKillShots;
  private List<Kill> killShots;

  private boolean finalFrenzySelected;
  private PlayerColor finalFrenzyActivator;

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
    takenPowerUps = new ArrayList<>();
    ammoCards = new ArrayList<>();
    takenAmmoCards = new ArrayList<>();
    killShots = new ArrayList<>();
  }

  /**
   * Given an AmmoColor returns reference to the same colored SpawnPoint, if existing
   *
   * @param spawnPointColor chosen color
   * @return SpawnPoint of spawnPointColor color, null if it doesn't exist
   */
  public Square getSpawnPointSquare(AmmoColor spawnPointColor) {
    if (spawnPointColor == AmmoColor.ANY) {
      throw new IllegalArgumentException("spawnPointColor can't be AmmoColor.ANY");
    }

    SquareColor squareColor = null;

    for (SquareColor color : SquareColor.values()) {
      if (color.getEquivalentAmmoColor() == spawnPointColor) {
        squareColor = color;
        break;
      }
    }

    for (Square square : getSquares()) {
      if (square.isSpawnPoint() &&
          square.getColor() == squareColor) {
        return square;
      }
    }

    Log.critical("Spawnpoint for " + spawnPointColor + " doesn't exists");
    return null;
  }

  /**
   * Adds a square to the board. Besides adding it to the grid, it sets {@code east}, {@code west},
   * {@code north} and {@code south} attributes and it also changes its neighbour squares
   * accordingly.
   *
   * @param square Square to add, must not be null
   * @throws IllegalArgumentException thrown if square is null
   */
  public void setSquare(Square square) {
    if (square == null) {
      throw new IllegalArgumentException("Square parameter must not be null");
    }
    setNorthNeighbour(square, this, true);
    setEastNeighbour(square, this, true);
    setSouthNeighbour(square, this, true);
    setWestNeighbour(square, this, true);

    grid[square.getPosX()][square.getPosY()] = square;

    try {
      notifyObservers(new BoardSetSquareUpdate(square));
    } catch (RemoteException e) {
      Log.exception(e);
    }

    try {
      for (Player player : getPlayers()) {
        ClientInterface client = player.getClient();
        if (client != null) {
          square.addObserver(client.getBoardView());
          square.addObserver(client.getPlayerDashboardsView());
          square.addObserver(client.getCharactersView());
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Set northern neighbour of a square if it exists.
   *
   * @param square whose neighbour will be set
   */
  private static void setNorthNeighbour(Square square, Board board, boolean reciprocal) {
    if (square.getPosY() > 0 && square.getEdge(Direction.NORTH) != BorderType.WALL) {
      Square neighbour = board.getSquare(square.getPosX(), square.getPosY() - 1);
      if (neighbour != null) {
        if (reciprocal) {
          neighbour.setNeighbour(Direction.SOUTH, square);
        }
        square.setNeighbour(Direction.NORTH, neighbour);
      }
    }
  }

  /**
   * Set southern neighbour of a square if it exists.
   *
   * @param square whose neighbour will be set
   */
  private static void setSouthNeighbour(Square square, Board board, boolean reciprocal) {
    if (square.getPosY() < 2 && square.getEdge(Direction.SOUTH) != BorderType.WALL) {
      Square neighbour = board.getSquare(square.getPosX(), square.getPosY() + 1);
      if (neighbour != null) {
        if (reciprocal) {
          neighbour.setNeighbour(Direction.NORTH, square);
        }
        square.setNeighbour(Direction.SOUTH, neighbour);
      }
    }
  }

  /**
   * Set eastern neighbour of a square if it exists.
   *
   * @param square whose neighbour will be set
   */
  private static void setEastNeighbour(Square square, Board board, boolean reciprocal) {
    if (square.getPosX() < 3 && square.getEdge(Direction.EAST) != BorderType.WALL) {
      Square neighbour = board.getSquare(square.getPosX() + 1, square.getPosY());
      if (neighbour != null) {
        if (reciprocal) {
          neighbour.setNeighbour(Direction.WEST, square);
        }
        square.setNeighbour(Direction.EAST, neighbour);
      }
    }
  }

  /**
   * Set western neighbour of a square if it exists.
   *
   * @param square whose neighbour will be set
   */
  private static void setWestNeighbour(Square square, Board board, boolean reciprocal) {
    if (square.getPosX() > 0 && square.getEdge(Direction.WEST) != BorderType.WALL) {
      Square neighbour = board.getSquare(square.getPosX() - 1, square.getPosY());
      if (neighbour != null) {
        if (reciprocal) {
          neighbour.setNeighbour(Direction.EAST, square);
        }
        square.setNeighbour(Direction.WEST, neighbour);
      }
    }
  }

  /**
   * Retrieves a square from the board given its coordinates.
   *
   * @param x x coordinate
   * @param y y coordinate
   * @return the requested Square
   * @throws IllegalArgumentException thrown if x or y are not within the size of the board
   */
  public Square getSquare(int x, int y) {
    if (!Square.isXValid(x) || !Square.isYValid(y)) {
      throw new IllegalArgumentException("Invalid square coordinates");
    }
    return grid[x][y];
  }

  /**
   * Retrieves a list of squares in the current map.
   *
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
   * Returns the id of the selected map.
   *
   * @return id of the map
   */
  public int getMapId() {
    return mapId;
  }

  /**
   * Sets the id of the current map.
   *
   * @param mapId the id of the map
   */
  public void setMapId(int mapId) {
    this.mapId = mapId;
    try {
      notifyObservers(new MapSelectionEvent(mapId));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns the player who is currently playing its turn.
   *
   * @return the Player currently playing
   */
  public PlayerColor getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Sets the player who is currently playing its turn.
   *
   * @param player the Player to set
   */
  public void setCurrentPlayer(PlayerColor player) {
    currentPlayer = player;
    try {
      notifyObservers(new CurrentPlayerUpdate(player));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns whether the current game mode is "Final Frenzy".
   *
   * @return true if the current game mode is "Final Frenzy", false otherwise
   */
  public boolean isFinalFrenzyActive() {
    return status == BoardStatus.FINAL_FRENZY;
  }

  /**
   * Returns whether the current game is set up to use "Final Frenzy" mode at the end.
   *
   * @return true if the game is set up to use "Final Frenzy" mode at the end, false otherwise
   */
  public boolean isFinalFrenzySelected() {
    return finalFrenzySelected;
  }

  /**
   * Sets up the current game to use "Final Frenzy" mode at the end.
   *
   * @param finalFrenzySelected true if the game shoud use "Final Frenzy" mode at the end, false
   * otherwise
   */
  public void setFinalFrenzySelected(boolean finalFrenzySelected) {
    this.finalFrenzySelected = finalFrenzySelected;
  }

  /**
   * Returns the player color of whom activated the "Final Frenzy" mode
   *
   * @return player color
   */
  public PlayerColor getFinalFrenzyActivator() {
    return finalFrenzyActivator;
  }

  /**
   * Sets the activator of the "Final Frenzy" mode
   *
   * @param playerColor the color of the activator of the "Final Frenzy" mode
   */
  public void setFinalFrenzyActivator(PlayerColor playerColor) {
    finalFrenzyActivator = playerColor;
  }

  public String getLastGameMessage() {
    return lastGameMessage;
  }

  public void setLastGameMessage(String lastGameMessage) {
    this.lastGameMessage = lastGameMessage;
  }

  /**
   * Notifies the initial state to every observer
   */
  public void addObserver(Observer observer, boolean notify) {
    addObserver(observer);
    if (notify) {
      try {
        for (Player player : getPlayers()) {
          observer.update(new BoardAddPlayerUpdate(player.getName(), player.getColor()));
          observer.update(new PlayerMasterUpdate(player.getColor(), player.isMaster()));
        }
        observer.update(new BoardSkullsUpdate(skulls));
        observer.update(new MapSelectionEvent(mapId));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  /**
   * Adds a player to the board.
   *
   * @param player the player to add
   */
  public void addPlayer(Player player) {
    players.add(player);
    try {
      notifyObservers(new BoardAddPlayerUpdate(player.getName(), player.getColor()));
      notifyObservers(new PlayerMasterUpdate(player.getColor(), player.isMaster()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a List of Players currently in the Board.
   *
   * @return a List of Players currently in the Board.
   */
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  /**
   * Gets a list of online player of this board
   *
   * @return a list of Player
   */
  public List<Player> getActivePlayers() {
    return players.stream()
        .filter(x -> x.getStatus() != PlayerStatus.DISCONNECTED
            && x.getStatus() != PlayerStatus.SUSPENDED)
        .collect(Collectors.toList());
  }

  /**
   * Gets a list of players that are not disconnected, or suspended or waiting
   * @return a list of Player
   */
  public List<Player> getPlayingPlayers() {
    return players.stream()
        .filter(x -> x.getStatus() != PlayerStatus.DISCONNECTED
            && x.getStatus() != PlayerStatus.SUSPENDED && x.getStatus() != PlayerStatus.WAITING)
        .collect(Collectors.toList());
  }

  /**
   * Gets a list of spawned players
   * @return a list of Player
   */
  public List<Player> getShootablePlayers() {
    return players.stream()
        .filter(x ->  x.getStatus() != PlayerStatus.WAITING)
        .collect(Collectors.toList());
  }

  /**
   * Function that tells if any player has died
   * @return true if it exists, false otherwise
   */
  public boolean existsOverKilledPlayer() {
    for (Player player : players) {
      if (player.getDamages().size() == ServerConfig.getInstance().getDeathDamages() + 1) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a Weapon to the board.
   *
   * @param weapon the Weapon to add
   */
  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
    //Collections.shuffle(weapons);

    try {
      notifyObservers(new BoardHasWeaponsUpdate(true));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Marks a Weapon as used by moving it from the weapons list to the takenWeapons list.
   *
   * @param weapon Weapon to be marked as used
   * @throws IllegalArgumentException thrown if weapon is not in the Board
   */
  public void takeWeapon(Weapon weapon) {
    if (!weapons.contains(weapon)) {
      throw new IllegalArgumentException("Weapon not present");
    }
    weapons.remove(weapon);
    takenWeapons.add(weapon);

    try {
      notifyObservers(new BoardHasWeaponsUpdate(weapons.isEmpty()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a List of Weapons in the Board.
   *
   * @return a List of Weapons in the Board
   */
  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  /**
   * Returns a List of taken Weapons.
   *
   * @return a List of taken Weapons.
   */
  public List<Weapon> getTakenWeapons() {
    return new ArrayList<>(takenWeapons);
  }

  /**
   * Adds a powerUp to the Board.
   *
   * @param powerup the powerUp to add to the Board
   */
  public void addPowerUp(PowerUp powerup) {
    powerUps.add(powerup);
    Collections.shuffle(powerUps);
  }

  /**
   * Marks a powerUp as used by moving it from the powerUps list to the usedPowerUps list.
   *
   * @param powerUp the powerUp to be marked as used
   * @throws IllegalArgumentException thrown if powerUp is not in the Board
   */
  public void drawPowerUp(PowerUp powerUp) {
    if (!powerUps.contains(powerUp)) {
      throw new IllegalArgumentException("PowerUp not present");
    }
    powerUps.remove(powerUp);
    takenPowerUps.add(powerUp);
  }

  /**
   * Marks a powerUp as not used by moving it from the takenPowerUps list to the powerUps list.
   *
   * @param powerUp the powerUp to be marked as unused
   * @throws IllegalArgumentException thrown if powerUp is not in the Board
   */
  public void undrawPowerUp(PowerUp powerUp) {
    if (!takenPowerUps.contains(powerUp)) {
      throw new IllegalArgumentException("PowerUp not present");
    }
    takenPowerUps.remove(powerUp);
    powerUps.add(powerUp);
    Collections.shuffle(powerUps);
  }


  /**
   * Returns a List of powerUps in the Board.
   *
   * @return a List of powerUps in the Board
   */
  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  /**
   * Returns a List of drawn powerUps in the Board.
   *
   * @return a List of drawn powerUps in the Board
   */
  public List<PowerUp> getTakenPowerUps() {
    return new ArrayList<>(takenPowerUps);
  }

  /**
   * Returns a PowerUp whose name and color is the same as the specified one.
   *
   * @param powerUpType type of the requested powerup
   * @param powerUpColor color of the requested powerup
   * @return PowerUp with name equals to "name" and powerUpColor equals "powerUpColor", null if
   * PowerUp does not exist
   */
  public PowerUp getPowerUpByNameAndColor(PowerUpType powerUpType, AmmoColor powerUpColor) {
    List<PowerUp> allPowerUps = getPowerUps();
    allPowerUps.addAll(takenPowerUps);

    for (PowerUp powerUp : allPowerUps) {
      if (powerUp.getType() == powerUpType && powerUp.getColor() == powerUpColor) {
        return powerUp;
      }
    }
    return null;
  }

  /**
   * Adds a ammoCard to the Board.
   *
   * @param ammoCard the ammoCard to add to the Board
   */
  public void addAmmoCard(AmmoCard ammoCard) {
    ammoCards.add(ammoCard);
    Collections.shuffle(ammoCards);
  }

  /**
   * Marks a ammoCard as used by moving it from the ammoCards list to the usedAmmoCards list.
   *
   * @param ammoCard the ammoCard to be marked as used
   * @throws IllegalArgumentException thrown if ammoCard is not in the Board
   */
  public void drawAmmoCard(AmmoCard ammoCard) {
    if (!ammoCards.contains(ammoCard)) {
      throw new IllegalArgumentException("AmmoCard not present");
    }
    ammoCards.remove(ammoCard);
    takenAmmoCards.add(ammoCard);

    try {
      notifyObservers(new BoardHasAmmoCardsUpdate(ammoCards.isEmpty()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Marks a ammoCard as not used by moving it from the takenAmmoCards list to the ammoCards list.
   *
   * @param ammoCard the ammoCard to be marked as unused
   * @throws IllegalArgumentException thrown if ammoCard is not in the Board
   */
  public void undrawAmmoCard(AmmoCard ammoCard) {
    if (!takenAmmoCards.contains(ammoCard)) {
      throw new IllegalArgumentException("AmmoCard not present");
    }
    takenAmmoCards.remove(ammoCard);
    ammoCards.add(ammoCard);
    Collections.shuffle(ammoCards);
  }


  /**
   * Returns a List of ammoCards in the Board.
   *
   * @return a List of ammoCards in the Board
   */
  public List<AmmoCard> getAmmoCards() {
    return new ArrayList<>(ammoCards);
  }

  /**
   * Returns a List of drawn ammoCards in the Board.
   *
   * @return a List of drawn ammoCards in the Board
   */
  public List<AmmoCard> getTakenAmmoCards() {
    return new ArrayList<>(takenAmmoCards);
  }

  /**
   * Gets the remaining skulls
   *
   * @return number of skulls
   */
  public int getSkulls() {
    return skulls;
  }

  /**
   * Sets the remaining skull of the killshot track
   *
   * @param skulls number of skulls
   */
  public void setSkulls(int skulls) {
    this.skulls = skulls;
    try {
      notifyObservers(new BoardSkullsUpdate(skulls));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Adds a killShot to the killshot track.
   *
   * @param kill the killShot to add
   */
  public void addKillShot(Kill kill) {
    killShots.add(kill);
    try {
      notifyObservers(new BoardKillShotsUpdate(getKillShots()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a list of killShot in the killshot track.
   *
   * @return a list of killShot in the killshot track
   */
  public List<Kill> getKillShots() {
    return new ArrayList<>(killShots);
  }

  /**
   * Updates the list of killShots in the board.
   *
   * @param newKillShots the new list of killShots that will replace the current one
   */
  public void updateKillShots(List<Kill> newKillShots) {
    killShots = new ArrayList<>(newKillShots);
  }

  /**
   * Returns the timestamp at which the current turn started.
   *
   * @return the timestamp at which the current turn started
   */
  public long getTurnStartTime() {
    return turnStartTime;
  }

  /**
   * Sets the timestamp at which the current turn started.
   *
   * @param turnStartTime the timestamp to set
   */
  public void setTurnStartTime(long turnStartTime) {
    this.turnStartTime = turnStartTime;
  }

  /**
   * Returns whether the Board is a DominationBoard. Always true in the children class {@code
   * DominationBoard}, always false here.
   *
   * @return true, since this is a normal Board and not a DominationBoard
   */
  public boolean isDominationBoard() {
    return false;
  }

  /**
   * Gets the turn counter
   *
   * @return the turn counter
   */
  public int getTurnCounter() {
    return turnCounter;
  }

  /**
   * Increment the turn counter
   *
   * @return the new value of the turn counter
   */
  public int incrementTurnCounter() {
    turnCounter++;
    return turnCounter;
  }

  /**
   * Returns the current status of the Board.
   *
   * @return the current Status of the Board
   */
  public BoardStatus getStatus() {
    return status;
  }

  /**
   * Sets the current Status of the Board.
   *
   * @param status the Status to set
   */
  public void setStatus(BoardStatus status) {
    this.status = status;
    try {
      notifyObservers(new BoardStatusUpdate(status));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns whether the board has any Weapons left in the stack.
   *
   * @return true if the board has any weapons left in the stack, false otherwise
   */
  public boolean hasWeapons() {
    return !weapons.isEmpty();
  }

  /**
   * Returns whether the board has any ammoCards left in the stack.
   *
   * @return true if the board has any ammoCards left in the stack, false otherwise
   */
  public boolean hasAmmoCards() {
    return !ammoCards.isEmpty();
  }

  /**
   * Returns unused PlayerColor for this board
   *
   * @return a set of free PlayerColor
   */
  public Set<PlayerColor> getFreePlayerColors() {
    EnumSet<PlayerColor> freeColors = EnumSet.allOf(PlayerColor.class);
    for (Player player : players) {
      if (freeColors.contains(player.getColor())) {
        freeColors.remove(player.getColor());
      } else {
        player.setColor(null);
      }
    }
    return freeColors;
  }

  /**
   * Retrieves a player by its color.
   *
   * @throws InvalidPlayerException thrown if there are no players of the specified color
   */
  public Player getPlayerByColor(PlayerColor color) throws InvalidPlayerException {
    for (Player player : players) {
      if (player.getColor() == color) {
        return player;
      }
    }
    throw new InvalidPlayerException("No such player");
  }

  /**
   * Retrieves a player by its name.
   *
   * @throws InvalidPlayerException thrown if there are no players with the specified name
   */
  public Player getPlayerByName(String playerName) throws InvalidPlayerException {
    for (Player player : players) {
      if (player.getName().equals(playerName)) {
        return player;
      }
    }
    throw new InvalidPlayerException("No such player");
  }

  /**
   * Removes the player of a given color.
   *
   * @param color the color of the player to remove
   * @throws InvalidPlayerException thrown if there are no players of the specified color
   */
  public void removePlayer(PlayerColor color) throws InvalidPlayerException {
    players.remove(getPlayerByColor(color));
    try {
      notifyObservers(new BoardRemovePlayerUpdate(color));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns a Weapon whose name is the same as the specified one.
   *
   * @param name name of the weapon requested
   * @return Weapon with name equals to "name", null if weapon does not exist
   */
  public Weapon getWeaponByName(String name) {
    List<Weapon> allWeapons = getWeapons();
    allWeapons.addAll(takenWeapons);
    for (Weapon weapon : allWeapons) {
      if (weapon.getName().equals(name)) {
        return weapon;
      }
    }
    return null;
  }

  /**
   * Increments the number of players killed this turn
   */
  public void incrementTurnKillShots() {
    turnKillShots++;
  }

  /**
   * Resets the number of players killed this turn
   */
  public void resetTurnKillShots() {
    turnKillShots = 0;
  }

  /**
   * Returns the number of players killed this turn
   * @return number of player
   */
  public int getTurnKillShots() {
    return turnKillShots;
  }

  /**
   * Gson serialization.
   *
   * @return JSON string containing serialized object
   */
  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy())
        .create();
    return gson.toJson(this);
  }

  /**
   * Creates Board object from a JSON serialized object. This method also sets each Square's
   * neighbours and Players to the correct state.
   *
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
      setNorthNeighbour(square, board, false);
      setEastNeighbour(square, board, false);
      setSouthNeighbour(square, board, false);
      setWestNeighbour(square, board, false);
    }
    return board;
  }
}
