package it.polimi.se2019.adrenalina.model;
import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class defining a single player
 */
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

  /**
   * Constructor
   * @param name String containing an user selected name, must be not null
   * @param color Color of the player chosen for a single game
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
   * Copy constructor
   * @param player Player object that has to be copied, can't be null
   * @param publicCopy boolean value indicating if the copy should be private or public.
   * If true a public copy will be made containg only public informations
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

  /**
   * Add damages received by a specific player
   * @param player Color of the player that inflicted damages
   * @exception IllegalStateException thrown if a player already received 12 damages
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
   * Add tags received by a specific player
   * @param player Color of the player that gave tags
   * @exception IllegalStateException thrown if a player already has 3 tags of the specified color
   */
  public void addTag(PlayerColor player) {
    if (tags.stream().filter(tag -> tag == player).count() > 3) {
      throw new IllegalStateException("Player already has 3 tags of this color");
    }
    tags.add(player);
  }

  public List<PowerUp> getPowerUps() {
    // TODO: powerUps is mutable
    return new ArrayList<>();
  }

  /**
   * Add a powerup to the list of available powerups
   * @param powerup chosen powerup
   * @exception IllegalStateException thrown if a player already has 3 powerups available
   */
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

  /**
   * Add a weapon to the list of available weapon
   * @param weapon chosen weapon
   * @exception IllegalStateException thrown if a player already has 3 weapon available
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

  /**
   * Create json serialization of a Player object
   * @return String
   */
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
