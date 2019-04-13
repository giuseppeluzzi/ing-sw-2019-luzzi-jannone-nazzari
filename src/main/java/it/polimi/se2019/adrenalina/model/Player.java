package it.polimi.se2019.adrenalina.model;
import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class Player extends Observable implements Target {
  // TODO: powerUps and weapon should have a remove*() method (whith which parameter?)
  private final String name;
  private final PlayerColor color;
  private Square square;
  private int points;
  private int deaths;
  private boolean frenzy;
  private PlayerStatus status;
  private int powerUpCount;

  private final List<PlayerColor> damages;
  private final List<PlayerColor> tags;
  private final List<PowerUp> powerUps;
  private final List<Weapon> weapons;
  private final HashMap<AmmoColor, Integer> ammo;

  private final boolean publicCopy;

  public Player(String name, PlayerColor color) {
    this.name = name;
    this.color = color;
    status = PlayerStatus.WAITING;
    frenzy = false;

    damages = new ArrayList<>();
    tags = new ArrayList<>();
    powerUps = new ArrayList<>();
    weapons = new ArrayList<>();
    ammo = new HashMap<>();

    ammo.put(AmmoColor.RED, 0);
    ammo.put(AmmoColor.BLUE, 0);
    ammo.put(AmmoColor.YELLOW, 0);
    publicCopy = false;
  }

  public Player(Player player, boolean publicCopy) {
    this.publicCopy = publicCopy;
    name = player.name;
    color = player.color;

    damages = player.getDamages();
    tags = player.getTags();

    if (publicCopy) {
      powerUps = new ArrayList<>();
      weapons = new ArrayList<>();
      for (Weapon weapon : player.weapons) {
        if (!weapon.isLoaded()) {
          weapons.add(weapon);
        }
      }
    } else {
      powerUps = player.getPowerUps();
      weapons = player.getWeapons();
    }

    ammo = new HashMap<>();
    for (AmmoColor ammoColor : AmmoColor.values()) {
      ammo.put(ammoColor, player.getAmmo(ammoColor));
    }
    square = player.getSquare();
    points = player.points;
    deaths = player.deaths;
    frenzy = player.frenzy;
    status = player.status;
    powerUpCount = player.powerUpCount;
  }

  @Override
  public boolean isPlayer() {
    return true;
  }

  public String getName() {
    return name;
  }

  public Square getSquare() {
    return new Square(square);
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

  public List<PlayerColor> getDamages() {
    return new ArrayList<>(damages);
  }

  public void addDamage(PlayerColor player) {
    if (damages.size() >= 12) {
      throw new IllegalStateException("Player is already dead");
    }
    damages.add(player);
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  public void addTag(PlayerColor player) {
    if (tags.stream().filter(tag -> tag == player).count() < 3) {
      tags.add(player);
    }
  }

  public List<PowerUp> getPowerUps() {
    // TODO: powerUps is mutable
    return new ArrayList<>();
  }

  public void addPowerUp(PowerUp powerup) {
    if (powerUps.size() >= 3) {
      throw new IllegalStateException("Player already has 3 powerUps");
    }
    powerUps.add(powerup);
    powerUpCount++;
  }

  public List<Weapon> getWeapons() {
    List<Weapon> output = new ArrayList<>();
    for (Weapon weapon : weapons) {
      output.add(new Weapon(weapon));
    }
    return output;
  }

  public void addWeapon(Weapon weapon) {
    if (weapons.size() >= 3) {
      throw new IllegalStateException("Player already has 3 weapons");
    }
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

  public boolean isPublicCopy() {
    return publicCopy;
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public Player deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Player.class);
  }
  public int getPowerUpCount() {
    return powerUpCount;
  }
}
