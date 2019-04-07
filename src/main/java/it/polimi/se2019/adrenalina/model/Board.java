package it.polimi.se2019.adrenalina.model;

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

  // TODO: every weapon of this board should be in weapons or usedWeapons
  private final List<Weapon> weapons;
  private final List<Weapon> usedWeapons;

  // TODO: every powerUp of this board should be in powerUps or usedPowerUps
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

  public Board(Board board, boolean publicCopy) {
    // TODO: copy all attributes, if publicCopy powerUps, usedPowerUps, weapons and usedWeapons must be set empty
    this.publicCopy = publicCopy;
    publicCopyHasWeapons = board.hasWeapons();

    grid = new Square[3][4];
    players = new ArrayList<>();

    weapons = new ArrayList<>();
    usedWeapons = new ArrayList<>();
    powerUps = new ArrayList<>();
    usedPowerUps = new ArrayList<>();
    doubleKills = new ArrayList<>();
    killShots = new ArrayList<>();
  }

  public void setSquare(int x, int y, Square square) {
    // TODO: limit 0 <= x <= 2, 0 <= y <= 3
    grid[x][y] = square;
  }

  public Square getSquare(int x, int y) {
    // TODO: check 0 <= x <= 2, 0 <= y <= 3; otherwise throw exception
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
    // TODO: players is mutable
    return new ArrayList<>();
  }

  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
  }

  public void useWeapon(Weapon weapon) {
    // TODO: check if exists otherwise throw exception
    weapons.remove(weapon);
    usedWeapons.add(weapon);
  }

  public List<Weapon> getWeapons() {
    // TODO: weapons is mutable
    return new ArrayList<>();
  }

  public List<Weapon> getUsedWeapons() {
    // TODO: usedWeapons is mutable
    return new ArrayList<>();
  }

  public void addPowerUp(PowerUp powerup) {
    powerUps.add(powerup);
  }

  public void usePowerUp(PowerUp powerup) {
    // TODO: check if exists otherwise throw exception
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
    // TODO: doubleKills is mutable
    return new ArrayList<>();
  }

  public void addKillShot(Kill kill) {
    killShots.add(kill);
  }

  public List<Kill> getKillShots() {
    // TODO: killShotTrack is mutable
    return new ArrayList<>();
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

  public Player getPlayerByColor(PlayerColor color) {
    // TODO: return correct player
    return null;
  }
}
