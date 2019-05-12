package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class defining a single player.
 */
public class Player extends Observable implements Target, Serializable {
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

  /**
   * Class constructor.
   * @param name User chosen name, must be not null.
   * @param color User chosen color.
   */
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

  /**
   * Copy constructor, creates an exact clone of a Player.
   * @param player the Player to be cloned, has to be not null.
   * @param publicCopy if true, a public copy of the Player will be created
   * instead of a clone. The public copy will not contain the player's private
   * information.
   */
  public Player(Player player, boolean publicCopy) {
    // TODO: find error with, see testCopyConstructor for reference
    if (player == null) {
      throw new IllegalArgumentException("Argument player can't be null");
    }
    this.publicCopy = publicCopy;
    name = player.name;
    color = player.color;

    damages = player.getDamages();
    tags = player.getTags();

    powerUps = new ArrayList<>();
    weapons = new ArrayList<>();

    setObservers(player.getObservers());

    if (! publicCopy) {
      for (PowerUp powerUp : player.powerUps) {
        powerUps.add(powerUp.copy());
      }
    }

    for (Weapon weapon : player.weapons) {
      if (!weapon.isLoaded() || !publicCopy) {
        weapons.add(weapon);
      }
    }

    ammo = new HashMap<>(player.ammo);

    if (player.square == null) {
      square = null;
    } else {
      square = new Square(player.square);
    }

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

  @Override
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

  public List<PlayerColor> getDamages() {
    return new ArrayList<>(damages);
  }

  /**
   * Adds a new damage to a player.
   * @param player color of the player that inflicted the damage.
   * @throws IllegalStateException thrown if a player has already received 12
   * damages, thus is dead.
   */
  public void addDamage(PlayerColor player) {
    if (damages.size() >= 12) {
      throw new IllegalStateException("Player is already dead");
    }
    damages.add(player);
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  /**
   * Adds a new tag to a player.
   * @param player color of the player that gave the tag.
   * @throws IllegalStateException thrown if a player already has 3 tags of the
   * specified color.
   */
  public void addTag(PlayerColor player) {
    if (tags.stream().filter(tag -> tag == player).count() >= 3) {
      throw new IllegalStateException("Player already has 3 tags of this color");
    }
    tags.add(player);
  }

  public List<PowerUp> getPowerUps() {
    // TODO: powerUps is mutable
    return new ArrayList<>(powerUps);
  }

  /**
   * Adds a powerUp to the list of powerUps of this player.
   * @param powerUp collected powerUp.
   * @throws IllegalStateException thrown if a player already has 3 powerUps.
   */
  public void addPowerUp(PowerUp powerUp) {
    if (powerUps.size() >= 3) {
      throw new IllegalStateException("Player already has 3 powerUps");
    }
    powerUps.add(powerUp);
    powerUpCount++;
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  /**
   * Adds a weapon to the list of weapons of this player.
   * @param weapon collected weapon.
   * @throws IllegalStateException thrown if a player already has 3 weapons.
   */
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

  public int getPowerUpCount() {
    return powerUpCount;
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Create Player object from json formatted String
   * @param json json input String
   * @return Player
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static Player deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Player.class);
  }
}
