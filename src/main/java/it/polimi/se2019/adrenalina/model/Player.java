package it.polimi.se2019.adrenalina.model;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Observable;

public class Player extends Observable implements Target, Cloneable {
  private final String name;
  private final PlayerColor color;
  private Square square;
  private int points;
  private int deaths;
  private boolean frenzy;
  private PlayerStatus status;

  private final List<Player> damages;
  private final List<Player> tags;
  private final List<PowerUp> powerUps;
  private final List<Weapon> weapons;
  private final EnumMap<AmmoColor, Integer> ammo;

  public Player(String name, PlayerColor color) {
    this.name = name;
    this.color = color;
    status = PlayerStatus.WAITING;
    frenzy = false;

    damages = new ArrayList<>();
    tags = new ArrayList<>();
    powerUps = new ArrayList<>();
    weapons = new ArrayList<>();
    ammo = new EnumMap<>(AmmoColor.class);

    ammo.put(AmmoColor.RED, 0);
    ammo.put(AmmoColor.BLUE, 0);
    ammo.put(AmmoColor.YELLOW, 0);
  }

  @Override
  public boolean isPlayer() {
    return true;
  }

  public String getName() {
    return name;
  }

  public Square getSquare() {
    return square;
  }

  public void setSquare(Square square) {
    this.square = square;
  }

  public PlayerColor getColor() {
    return color;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public int getDeaths() {
    return deaths;
  }

  public void setDeaths(int deaths) {
    this.deaths = deaths;
  }

  public PlayerStatus getStatus() {
    return status;
  }

  public void setStatus(PlayerStatus status) {
    this.status = status;
  }

  public List<Player> getDamages() {
    // TODO: damages is mutable
    return new ArrayList<>();
  }

  public void addDamage(Player player) {
    // TODO: check if dead
    damages.add(player);
  }

  public List<Player> getTags() {
    // TODO: tags is mutable
    return new ArrayList<>();
  }

  public void addTag(Player player) {
    // TODO: limit to 3 per offender
    tags.add(player);
  }

  public List<PowerUp> getPowerUps() {
    // TODO: powerUps is mutable
    return new ArrayList<>();
  }

  public void addPowerUp(PowerUp powerup) {
    // TODO: limit to 3
    powerUps.add(powerup);
  }

  public List<Weapon> getWeapons() {
    // TODO: weapons is mutable
    return new ArrayList<>();
  }

  public void addWeapon(Weapon weapon) {
    // TODO: limit to 3
    weapons.add(weapon);
  }

  public int getAmmo(AmmoColor ammoColor) {
    return ammo.get(ammoColor);
  }

  public boolean isFrenzy() {
    return frenzy;
  }

  public void setFrenzy(boolean frenzy) {
    this.frenzy = frenzy;
  }

  @Override
  public Player clone() {
    //TODO: player is mutable
    return this;
  }
}
