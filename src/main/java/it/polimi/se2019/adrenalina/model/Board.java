package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Board extends Observable {
  private final Square[][] grid;
  private boolean finalFrenzy;
  private Player currentPlayer;
  private final List<Player> players;

  // TODO: every weapon of this board should be in weapons or usedWeapons
  private final List<Weapon> weapons;
  private final List<Weapon> usedWeapons;

  // TODO: every powerUp of this board should be in powerUps or usedPowerUps
  private final List<PowerUp> powerUps;
  private final List<PowerUp> usedPowerUps;

  private final List<Player> doubleKills;
  private final List<Kill> killShots;

  public Board() {
    grid = new Square[3][4];

    currentPlayer = null;
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

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player player) {
    currentPlayer = player;
  }

  public boolean isFinalFrenzy() {
    return finalFrenzy;
  }

  public void setFinalFrenzy(boolean finalFrenzy) {
    this.finalFrenzy = finalFrenzy;
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
}
