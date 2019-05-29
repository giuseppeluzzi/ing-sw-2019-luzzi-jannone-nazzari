package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDeathUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class defining a single player.
 */
public class Player extends Observable implements Target, Serializable {

  // TODO: powerUps and weapon should have a remove*() method (with which parameter?)
  private static final long serialVersionUID = -3827252611045096143L;

  public static final int OVERKILL_DEATH = 12;
  public static final int NORMAL_DEATH = 11;

  private final transient Board board;

  private final String name;
  private PlayerColor color;
  private Square square;
  private PlayerStatus status;

  private int deaths;
  private boolean frenzy;

  private int powerUpCount;
  private int weaponCount;
  private int score;
  private int killScore; // TODO: this should be updated in Final Frenzy mode

  private List<PlayerColor> damages;
  private List<PlayerColor> tags;
  private List<PowerUp> powerUps;
  private List<Weapon> weapons;
  private HashMap<AmmoColor, Integer> ammo;

  private Weapon currentWeapon;
  private Buyable currentBuying;

  private transient ClientInterface client;

  private final boolean publicCopy;

  /**
   * Class constructor.
   * @param name User chosen name, must be not null
   * @param color User chosen color
   * @param board The game board
   */
  public Player(String name, PlayerColor color, Board board) {
    this.name = name;
    this.color = color;
    this.board = board;
    status = PlayerStatus.WAITING;
    frenzy = false;
    killScore = 8;

    damages = new ArrayList<>();
    tags = new ArrayList<>();
    powerUps = new ArrayList<>();
    weapons = new ArrayList<>();
    ammo = new HashMap<>();

    ammo.put(AmmoColor.RED, 0);
    ammo.put(AmmoColor.BLUE, 0);
    ammo.put(AmmoColor.YELLOW, 0);
    currentWeapon = null;
    publicCopy = false;
  }

  /**
   * Copy constructor, creates an exact clone of a Player.
   * @param player the Player to be cloned, has to be not null
   * @param publicCopy if true, a public copy of the Player will be created
   * instead of a clone. The public copy will not contain the player's private
   * information
   */
  public Player(Player player, boolean publicCopy) {
    // TODO: find error with, see testCopyConstructor for reference
    if (player == null) {
      throw new IllegalArgumentException("Argument player can't be null");
    }
    this.publicCopy = publicCopy;
    board = player.board;
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

    score = player.score;
    deaths = player.deaths;
    frenzy = player.frenzy;
    status = player.status;
    powerUpCount = player.powerUpCount;
    weaponCount = player.weaponCount;
    currentWeapon = null;
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
    if (this.square != null) {
      this.square.removePlayer(this);
    }
    this.square = square;
    square.addPlayer(this);
    try {
      notifyObservers(new PlayerPositionUpdate(color, square.getPosX(), square.getPosY()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public Board getBoard() {
    return board;
  }

  public void setColor(PlayerColor playerColor) {
    color = playerColor;
  }

  public PlayerColor getColor() {
    return color;
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

  public int getScore() {
    return score;
  }

  public void setScore(int points) {
    score = points;
    try {
      notifyObservers(new PlayerScoreUpdate(color, score));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public List<PlayerColor> getDamages() {
    return new ArrayList<>(damages);
  }

  public ClientInterface getClient() {
    return client;
  }

  public void setClient(ClientInterface client) {
    this.client = client;
  }

  @Override
  public Player getPlayer() {
    return this;
  }

  /**
   * Adds a new damage to a player including damages given by tags and, possibly, inflicts death.
   * @param killerColor color of the killer that inflicted the damage
   */
  @Override
  public void addDamages(PlayerColor killerColor, int num) {
    int maxDamages = OVERKILL_DEATH - damages.size();
    for (int i = 0; i < Math.min(num, maxDamages); i++) {
      damages.add(killerColor);
    }
    for (PlayerColor tag : new ArrayList<>(tags)) {
      if (tag == killerColor) {

        if (damages.size() < OVERKILL_DEATH) {
          damages.add(killerColor);
        }
        tags.remove(tag);
      }
    }
    if (damages.size() == OVERKILL_DEATH) {
      try {
        board.getPlayerByColor(damages.get(NORMAL_DEATH)).addTags(color, 1);
      } catch (InvalidPlayerException ignored) {
        //
      }
    }
    try {
      notifyObservers(new PlayerDamagesTagsUpdate(getDamages(), getTags(), color));
    } catch (RemoteException e) {
      Log.exception(e);
    }
    if (damages.size() >= NORMAL_DEATH) {
      try {
        notifyObservers(new PlayerDeathUpdate(color));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  public void updateDamages(List<PlayerColor> newDamages) {
    damages = new ArrayList<>(newDamages);
  }

  public void updateTags(List<PlayerColor> newTags) {
    tags = new ArrayList<>(newTags);
  }

  public boolean isDead() {
    return damages.size() >= NORMAL_DEATH;
  }

  /**
   * Builds an list of unique PlayerColors who damaged this player. The list is ordered based on
   * the points that each player should get as a reward.
   * @return the list of PlayerColors
   */
  private List<PlayerColor> getPlayerRankings() {
    List<PlayerColor> output = new ArrayList<>();
    List<PlayerColor> distinctPlayers = damages.stream().distinct().collect(Collectors.toList());
    EnumMap<PlayerColor, Integer> damageCount = new EnumMap<>(PlayerColor.class);
    for (PlayerColor player : distinctPlayers) {
      damageCount.put(player, Collections.frequency(damages, player));
    }
    while (! damageCount.isEmpty()) {
      int max = Collections.max(damageCount.values());
      List<PlayerColor> maxPlayers = new ArrayList<>();
      Set<Map.Entry<PlayerColor, Integer>> entrySet = damageCount.entrySet();
      for (Map.Entry<PlayerColor, Integer> entry : entrySet) {
        if (entry.getValue() == max) {
          maxPlayers.add(entry.getKey());
          damageCount.remove(entry.getKey());
        }
      }
      maxPlayers.sort(Comparator.comparing(damages::indexOf));
      output.addAll(maxPlayers);
    }
    return output;
  }

  public void respawn(AmmoColor spawnColor) {
    if (! isDead()) {
      throw new IllegalStateException("Player is not dead");
    }
    if (! board.isFinalFrenzyActive()) {
      try {
        board.getPlayerByColor(damages.get(0)).setScore(score + 1); // first blood
      } catch (InvalidPlayerException ignored) {
        //
      }
    }
    int awardedScore = killScore;
    for (PlayerColor playerColor : getPlayerRankings()) { // score for damages
      try {
        board.getPlayerByColor(playerColor).setScore(score + awardedScore);
      } catch (InvalidPlayerException ignored) {
        //
      }
      if (awardedScore > 1) {
        awardedScore -= 2;
      }
    }
    if (board.getDoubleKill().color == color) {
      score += 1;
    }
    if (! board.isDominationBoard()) {
      board.addKillShot(new Kill(damages.get(10), damages.get(NORMAL_DEATH) == damages.get(10)));
    } else if (damages.get(NORMAL_DEATH) == damages.get(10)) {
      // TODO: choose a spawnpoint track and put a token there
    }
    if (killScore > 1) {
      killScore -= 2;
    }
    damages.clear();
    square = board.getSpawnPointSquare(spawnColor);
    try {
      notifyObservers(new PlayerKillScoreUpdate(color, killScore));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  /**
   * Adds a new tag to a player if that player does not already have 3 tags from its attacker.
   * @param player color of the player that gave the tag
   */
  @Override
  public void addTags(PlayerColor player, int num) {
    for (int i = 0; i < num; i++) {
      if (tags.stream().filter(tag -> tag == player).count() >= 3) {
        return;
      }
      tags.add(player);
    }
    try {
      notifyObservers(new PlayerDamagesTagsUpdate(getDamages(), getTags(), color));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public PowerUp getPowerUp(PowerUpType powerUpType, AmmoColor powerUpColor) {
    for (PowerUp powerUp : powerUps) {
      if (powerUp.getType() == powerUpType && powerUp.getColor() == powerUpColor) {
        return powerUp;
      }
    }
    return null;
  }

  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  /**
   * Adds a powerUp to the list of powerUps of this player.
   * @param powerUp collected powerUp
   * @throws InvalidPowerUpException thrown if a player already has 3 powerUps
   */
  public void addPowerUp(PowerUp powerUp) throws InvalidPowerUpException {
    addPowerUp(powerUp, false);
  }

  /**
   * Adds a powerUp to the list of powerUps of this player.
   * @param powerUp collected powerUp
   * @param force should ignore the powerup limit
   * @throws InvalidPowerUpException thrown if a player already has 3 powerUps and force is false
   */
  public void addPowerUp(PowerUp powerUp, boolean force) throws InvalidPowerUpException  {
    if (powerUps.size() >= 3 && !force) {
      throw new InvalidPowerUpException("Player already has 3 powerUp");
    }
    powerUps.add(powerUp);
    powerUpCount++;

    try {
      notifyObservers(new OwnPowerUpUpdate(color, getPowerUps()));
      notifyObservers(new EnemyPowerUpUpdate(color, powerUpCount));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }


  /**
   * Removes a powerUp from a player.
   * @param powerUp the powerUp to remove
   * @throws InvalidPowerUpException thrown if the player does not own that powerUp
   */
  public void removePowerUp(PowerUp powerUp) throws InvalidPowerUpException {
    if (! powerUps.contains(powerUp)) {
      throw new InvalidPowerUpException("Player does not own this powerUp");
    }
    powerUps.remove(powerUp);
    powerUpCount--;
  }

  public void updatePowerUps(List<PowerUp> newPowerUps) {
    powerUps = new ArrayList<>(newPowerUps);
    powerUpCount = powerUps.size();
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
  }

  public List<Weapon> getUnloadedWeapons() {
    List<Weapon> returnWeapons = new ArrayList<>();
    for (Weapon weapon : getWeapons()) {
      if (! weapon.isLoaded()) {
        returnWeapons.add(weapon);
      }
    }
    return returnWeapons;
  }

  /**
   * Adds a weapon to the list of weapons of this player.
   * @param weapon collected weapon
   * @throws IllegalStateException thrown if a player already has 3 weapons
   */
  public void addWeapon(Weapon weapon) {
    if (weapons.size() >= 3) {
      throw new IllegalStateException("Player already has 3 weapons");
    }
    weapons.add(weapon);
    weaponCount++;
    try {
      notifyObservers(new OwnWeaponUpdate(color, getWeapons()));
      notifyObservers(new EnemyWeaponUpdate(color, weaponCount, getUnloadedWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void updateWeapons(List<Weapon> newWeapons) {
    weapons = new ArrayList<>(newWeapons);
    weaponCount = weapons.size();
  }

  /**
   * Check if Player has collected a specific weapon.
   * @param weapon Weapon checked
   * @return true if the player holds the weapon, false otherwise
   */
  public boolean hasWeapon(Weapon weapon) {
    for (Weapon toCheck : weapons) {
      if (weapon.equals(toCheck)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if Player has at least one loaded weapon
   * @return true if the player holds a loaded weapon, false otherwise
   */
  public boolean hasLoadedWeapons() {
    for (Weapon weapon: weapons) {
      if (weapon.isLoaded()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if Player can pay to reload weapon.
   * @param weapon Weapon reloading
   * @return true if possible, false otherwise
   */
  public boolean canReload(Weapon weapon) {
    EnumMap<AmmoColor, Integer> powerUpAmmo = getPowerUpAmmo();

    for (AmmoColor ammoColor : AmmoColor.getValidColor()) {

      if (ammoColor == weapon.getBaseCost()) {
        if (getAmmo(ammoColor) + powerUpAmmo.get(ammoColor) < weapon.getCost(ammoColor) + 1) {
          return false;
        }
      } else {
        if (getAmmo(ammoColor) + powerUpAmmo.get(ammoColor) < weapon.getCost(ammoColor)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Adds the quantity for an AmmoColor, ammo will be added only up to three unit per color.
   * @param ammoColor key
   * @param value how many ammo will be added
   */
  public void addAmmo(AmmoColor ammoColor, int value) {
    int currentAmmo = ammo.get(ammoColor);
    if (currentAmmo + value >= 3) {
      ammo.put(ammoColor, 3);
    } else {
      ammo.put(ammoColor, currentAmmo + value);
    }
    try {
      notifyObservers(new PlayerAmmoUpdate(color, getAmmo(AmmoColor.BLUE), getAmmo(AmmoColor.RED),
          getAmmo(AmmoColor.YELLOW)));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void updateAmmo(AmmoColor ammoColor, int value) {
    if (value > 3) {
      throw new IllegalStateException("Cannot have more than 3 ammoCards of this color");
    }
    ammo.put(ammoColor, value);
  }

  /**
   * Private method, creates an EnumMap associating each AmmoColor to an integer value
   * representing how many powerUps of that color the player possesses.
   * @return the EnumMap
   */
  private EnumMap<AmmoColor, Integer> getPowerUpAmmo() {
    EnumMap<AmmoColor, Integer> powerUpAmmo = new EnumMap<>(AmmoColor.class);

    for (PowerUp powerUp : getPowerUps()) {
      if (powerUpAmmo.containsKey(powerUp.getColor())) {
        powerUpAmmo.put(powerUp.getColor(), powerUpAmmo.get(powerUp.getColor()) + 1);
      } else {
        powerUpAmmo.put(powerUp.getColor(), 1);
      }
    }

    for (AmmoColor ammoColor : AmmoColor.getValidColor()) {

      if (!powerUpAmmo.containsKey(ammoColor)) {
        powerUpAmmo.put(ammoColor, 0);
      }
    }
    return powerUpAmmo;
  }

  /**
   * Check if player can collect a specific weapon.
   * @param weapon weapon to check
   * @return true if it's possible, false otherwise.
   */
  public boolean canCollectWeapon(Weapon weapon) {
    EnumMap<AmmoColor, Integer> powerUpAmmo = getPowerUpAmmo();

    for (AmmoColor ammoColor : AmmoColor.getValidColor()) {
      if (getAmmo(ammoColor) + powerUpAmmo.get(ammoColor) < weapon.getCost(ammoColor)) {
        return false;
      }
    }
    return true;
  }

  public HashMap<AmmoColor, Integer> getAmmos() {
    return new HashMap<>(ammo);
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

  public void setPowerUpCount(int powerUpCount) {
    this.powerUpCount = powerUpCount;
  }

  public int getWeaponCount() {
    return weaponCount;
  }

  public void setWeaponCount(int weaponCount) {
    this.weaponCount = weaponCount;
  }

  public Weapon getCurrentWeapon() {
    return currentWeapon;
  }

  public void setCurrentWeapon(Weapon currentWeapon) {
    this.currentWeapon = currentWeapon;
  }

  public void setKillScore(int score) {
    killScore = score;
  }

  public Buyable getCurrentBuying() {
    return currentBuying;
  }

  public void setCurrentBuying(Buyable currentBuying) {
    this.currentBuying = currentBuying;
  }

  /**
   * Gson serialization.
   * @return JSON string containing serialized object
   */
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Creates Player object from a JSON serialized object.
   * @param json JSON input String
   * @return Player object
   */
  public static Player deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Player.class);
  }
}
