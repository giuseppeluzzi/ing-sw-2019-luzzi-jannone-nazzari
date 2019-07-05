package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.event.modelview.*;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Class defining a player.
 */
public class Player extends Observable implements Target {

  private static final long serialVersionUID = -3827252611045096143L;

  private final transient Board board;

  private final String name;
  private PlayerColor color;
  private PlayerStatus status;
  private boolean master;
  @NotExpose
  private Square square;

  private int deaths;
  private boolean frenzy;

  private int powerUpCount;
  private int weaponCount;
  private int score;
  private int timeoutCount;
  private int killScore;

  private List<PlayerColor> damages;
  private List<PlayerColor> tags;
  private final HashMap<AmmoColor, Integer> ammo;
  @NotExpose
  private List<Weapon> weapons = new ArrayList<>();
  @NotExpose
  private List<PowerUp> powerUps = new ArrayList<>();

  @NotExpose
  private transient ExecutableObject currentExecutable;
  @NotExpose
  private transient ExecutableObject oldExecutable;
  @NotExpose
  private Buyable currentBuying;

  private transient ClientInterface client;

  private final boolean publicCopy;

  /**
   * Class constructor.
   *
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
    currentExecutable = null;
    publicCopy = false;
  }

  /**
   * Copy constructor, creates an exact clone of a Player.
   *
   * @param player the Player to be cloned, has to be not null
   * @param publicCopy if true, a public copy of the Player will be created instead of a clone. The
   * public copy will not contain the player's private information
   */
  public Player(Player player, boolean publicCopy) {
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

    if (!publicCopy) {
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
    currentExecutable = null;
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

  @Override
  public void setSquare(Square square) {
    if (this.square != null && this.square.getPosX() == square.getPosX() && this.square.getPosY() == square.getPosY()) {
      return;
    }
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
    if (color != null) {
      try {
        notifyObservers(new PlayerColorSelectionEvent(color, playerColor));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
    color = playerColor;
  }

  public PlayerColor getColor() {
    return color;
  }

  /**
   * Specifies whether this player is the one who created the game.
   *
   * @return true if this player is the one who created the game, false otherwise.
   */
  public boolean isMaster() {
    return master;
  }

  public void setMaster(boolean master) {
    if (master && board != null) {
      for (Player toPlayer : board.getPlayers()) {
        if (toPlayer.color != color) {
          toPlayer.setMaster(false);
        }
      }
    }
    this.master = master;
    try {
      notifyObservers(new PlayerMasterUpdate(color, master));
    } catch (RemoteException e) {
      Log.exception(e);
    }
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
    try {
      notifyObservers(new PlayerStatusUpdate(color, status));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public int getScore() {
    return score;
  }

  public int getKillScore() {
    return killScore;
  }

  public void setScore(int points) {
    score = points;
    try {
      notifyObservers(new PlayerScoreUpdate(color, score));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Returns the number of times a turn has timed out for this player. The value is reset when the
   * player is suspended.
   *
   * @return the number of times a turn has timed out for this player
   */
  public int getTimeoutCount() {
    return timeoutCount;
  }

  public void incrementTimeoutCount() {
    timeoutCount++;
  }

  public void resetTimeoutCount() {
    timeoutCount = 0;
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
   * @param killerColor color of the player that inflicted the damage
   */
  @Override
  public void addDamages(PlayerColor killerColor, int num, boolean powerup) {
    int oldDamagesSize = damages.size();
    int maxDamages = ServerConfig.getInstance().getDeathDamages() + 1 - damages.size();

    for (int i = 0; i < Math.min(num, maxDamages); i++) {
      damages.add(killerColor);
    }

    addDamagesFromTags(killerColor, powerup, num);

    try {
      notifyObservers(new PlayerDamagesTagsUpdate(getDamages(), getTags(), color, killerColor));
    } catch (RemoteException e) {
      Log.exception(e);
    }

    if (oldDamagesSize <= ServerConfig.getInstance().getDeathDamages() &&
            damages.size() >= ServerConfig.getInstance().getDeathDamages() + 1) {

      // An overkill was just scored

      try {
        board.getPlayerByColor(killerColor).addTags(color, 1);
      } catch (InvalidPlayerException ignored) {
        //
      }

    }

    if (oldDamagesSize < ServerConfig.getInstance().getDeathDamages() &&
            damages.size() >= ServerConfig.getInstance().getDeathDamages()) {

      // Player just died

      handlePlayerDeath(killerColor);

    }
  }

  /**
   * Handles player death
   * @param killerColor the color of the killer
   */
  private void handlePlayerDeath(PlayerColor killerColor) {
    board.incrementTurnKillShots();
    try {
      notifyObservers(new PlayerDeathUpdate(color, killerColor));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Converts any existing tags of a killer into damages
   * if the damages weren't scored using a powerUp
   * @param killerColor the color of the player who scored the damages
   * @param powerUp whether the damages were added by a powerUp
   * @param num number of damages
   */
  private void addDamagesFromTags(PlayerColor killerColor, boolean powerUp, int num) {
    if (! powerUp && num > 0) {
      for (PlayerColor tag : new ArrayList<>(tags)) {
        if (tag == killerColor) {

          if (damages.size() < ServerConfig.getInstance().getDeathDamages() + 1) {
            damages.add(killerColor);
          }
          tags.remove(tag);
        }
      }
    }
  }

  /**
   * Handles the case when the last skull is taken.
   */
  private void handleLastSkull(PlayerColor finalFrenzyActivator) {
    board.setSkulls(0);
    // Attivazione frenesia finale
    if (board.isFinalFrenzySelected() && !board.isFinalFrenzyActive()) {
      board.setStatus(BoardStatus.FINAL_FRENZY_ENABLED);
      board.setFinalFrenzyActivator(finalFrenzyActivator);
    }
  }

  public void updateDamages(List<PlayerColor> newDamages) {
    damages = new ArrayList<>(newDamages);
  }

  public void updateTags(List<PlayerColor> newTags) {
    tags = new ArrayList<>(newTags);
  }

  public boolean isDead() {
    return damages.size() >= ServerConfig.getInstance().getDeathDamages();
  }

  /**
   * Builds an list of unique PlayerColors who damaged this player. The list is ordered based on the
   * points that each player should get as a reward.
   *
   * @return the list of PlayerColors
   */
  private List<PlayerColor> getPlayerRankings() {
    List<PlayerColor> output = new ArrayList<>();
    List<PlayerColor> distinctPlayers = damages.stream().distinct().collect(Collectors.toList());
    Map<PlayerColor, Integer> damageCount = new EnumMap<>(PlayerColor.class);
    for (PlayerColor player : distinctPlayers) {
      damageCount.put(player, Collections.frequency(damages, player));
    }
    while (!damageCount.isEmpty()) {
      int max = Collections.max(damageCount.values());
      List<PlayerColor> maxPlayers = new ArrayList<>();
      Set<Entry<PlayerColor, Integer>> entrySet = damageCount.entrySet();
      for (Entry<PlayerColor, Integer> entry : entrySet) {
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

  /**
   * Handles "first blood" points assignment.
   */
  private void assignFirstBlood() {
    PlayerColor firstDamage = damages.get(0);
    if (firstDamage != color) {
      try {
        board.getPlayerByColor(firstDamage).setScore(board.getPlayerByColor(firstDamage).score + 1);
      } catch (InvalidPlayerException ignored) {
        //
      }
    }
  }

  /**
   * Handles damages points assignemnt.
   */
  private void assignDamagesPoints() {
    int awardedScore;
    if (killScore > 0) {
      awardedScore = killScore;
    } else {
      awardedScore = 1;
    }
    for (PlayerColor playerColor : getPlayerRankings()) { // score for damages
      if (playerColor != color) {
        try {
          board.getPlayerByColor(playerColor).setScore(board.getPlayerByColor(playerColor).score + awardedScore);
        } catch (InvalidPlayerException ignored) {
          //
        }
      }
      if (awardedScore > 2) {
        awardedScore -= 2;
      } else {
        awardedScore = 1;
      }
    }
  }

  /**
   * Handles score assignment after the death of a player and clears its status.
   */
  public void assignPoints() {
    if (!isDead()) {
      throw new IllegalStateException("Player is not dead");
    }

    if (board.getSkulls() > 1) {
      board.setSkulls(board.getSkulls() - 1);
    } else if (board.getSkulls() == 1) {
      handleLastSkull(damages.get(damages.size()-1));
    }

    board.addKillShot(new Kill(damages.get(ServerConfig.getInstance().getDeathDamages() - 1),
        damages.size() >= ServerConfig.getInstance().getDeathDamages() + 1 && ! board.isDominationBoard()));

    if (!board.isFinalFrenzyActive()) {
      assignFirstBlood();
    }
    assignDamagesPoints();

    decrementKillScore();
    damages.clear();
    try {
      notifyObservers(new PlayerDamagesTagsUpdate(getDamages(), getTags(), color, null));
      notifyObservers(new PlayerKillScoreUpdate(color, killScore));
      notifyObservers(new PlayerScoreUpdate(color, score));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  /**
   * Adds a new tag to a player if that player does not already have 3 tags from its attacker.
   *
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
      notifyObservers(new PlayerDamagesTagsUpdate(getDamages(), getTags(), color, player));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Interface method that returns the equivalent ansi color of the target
   * @return ansi color
   */
  @Override
  public ANSIColor getAnsiColor() {
    return color.getAnsiColor();
  }

  /**
   * Return Weapon whose name matches specified one.
   *
   * @param weaponName name of the weapon
   * @return Weapon if present, null otherwise
   */
  public Weapon getWeaponByName(String weaponName) {
    for (Weapon weapon : weapons) {
      if (weaponName.equals(weapon.getName())) {
        return weapon;
      }
    }
    return null;
  }

  /**
   * Return a PowerUp that matches type and color if present.
   *
   * @param powerUpType Type of chosen powerup
   * @param powerUpColor Color of chosen powerup
   * @return PowerUp chosen.
   */
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
   *
   * @param powerUp collected powerUp
   * @throws InvalidPowerUpException thrown if a player already has 3 powerUps
   */
  public void addPowerUp(PowerUp powerUp) throws InvalidPowerUpException {
    addPowerUp(powerUp, false);
  }

  /**
   * Adds a powerUp to the list of powerUps of this player.
   *
   * @param powerUp collected powerUp
   * @param force should ignore the powerup limit
   * @throws InvalidPowerUpException thrown if a player already has 3 powerUps and force is false
   */
  public void addPowerUp(PowerUp powerUp, boolean force) throws InvalidPowerUpException {
    if (powerUps.size() >= 3 && !force) {
      throw new InvalidPowerUpException("Player already has 3 powerUp");
    }
    powerUp.setTargetHistory(0, this);
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
   *
   * @param powerUp the powerUp to remove
   * @throws InvalidPowerUpException thrown if the player does not own that powerUp
   */
  public void removePowerUp(PowerUp powerUp) throws InvalidPowerUpException {
    if (!powerUps.contains(powerUp)) {
      throw new InvalidPowerUpException("Player does not own this powerUp");
    }
    powerUp.reset();
    powerUps.remove(powerUp);
    powerUpCount--;
    try {
      notifyObservers(new OwnPowerUpUpdate(color, getPowerUps()));
      notifyObservers(new EnemyPowerUpUpdate(color, powerUpCount));
    } catch (RemoteException e) {
      Log.exception(e);
    }
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
      if (!weapon.isLoaded()) {
        returnWeapons.add(weapon);
      }
    }
    return returnWeapons;
  }

  /**
   * Adds a weapon to the list of weapons of this player.
   *
   * @param weapon collected weapon
   * @throws IllegalStateException thrown if a player already has 3 weapons
   */
  public void addWeapon(Weapon weapon) {
    if (weapons.size() >= 3) {
      throw new IllegalStateException("Player already has 3 weapons");
    }
    weapon.setTargetHistory(0, this);
    weapon.setLoaded(true);
    weapons.add(weapon);
    weaponCount++;
    if (board != null) {
      for (Player player : board.getPlayers()) {
        if (player.client != null) {
          try {
            weapon.addObserver(player.client.getBoardView());
            weapon.addObserver(player.client.getPlayerDashboardsView());
            weapon.addObserver(player.client.getCharactersView());
          } catch (RemoteException e) {
            Log.exception(e);
          }
        }
      }
    }
    try {
      notifyObservers(new OwnWeaponUpdate(color, getWeapons()));
      notifyObservers(new EnemyWeaponUpdate(color, weaponCount, getUnloadedWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Removes specified weapon if owned by the player.
   *
   * @param weapon removed weapon
   * @throws IllegalArgumentException throw if the player doesn't own the weapon
   */
  public void removeWeapon(Weapon weapon) {
    if (!weapons.contains(weapon)) {
      throw new IllegalArgumentException("Player does not have this weapon");
    }
    weapons.remove(weapon);
    weaponCount--;
    if (client != null) {
      try {
        notifyObservers(new OwnWeaponUpdate(color, getWeapons()));
        notifyObservers(new EnemyWeaponUpdate(color, weaponCount, getUnloadedWeapons()));
        weapon.removeObserver(client.getBoardView());
        weapon.removeObserver(client.getPlayerDashboardsView());
        weapon.removeObserver(client.getCharactersView());
        weapon.reset();
        weapon.setLoaded(true);
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  public void updateWeapons(List<Weapon> newWeapons) {
    weapons = new ArrayList<>(newWeapons);
    weaponCount = weapons.size();
  }

  /**
   * Check if Player has collected a specific weapon.
   *
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
   *
   * @return true if the player holds a loaded weapon, false otherwise
   */
  public boolean hasLoadedWeapons() {
    for (Weapon weapon : weapons) {
      if (weapon.isLoaded()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if Player can pay to reload weapon.
   *
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
   *
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
   * Creates an EnumMap associating each AmmoColor to an integer value representing how many
   * powerUps of that color the player has.
   *
   * @return the described EnumMap
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
   *
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

  public Map<AmmoColor, Integer> getAmmos() {
    return new HashMap<>(ammo);
  }

  public int getAmmo(AmmoColor ammoColor) {
    return ammo.get(ammoColor);
  }

  public boolean isFrenzy() {
    return frenzy;
  }

  public void enableFrenzy() {
    frenzy = true;
    try {
      notifyObservers(new PlayerFrenzyUpdate(color, frenzy));
    } catch (RemoteException e) {
      Log.exception(e);
    }
    if (damages.isEmpty()) {
      killScore = 2;
      try {
        notifyObservers(new PlayerKillScoreUpdate(color, killScore));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
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

  public ExecutableObject getCurrentExecutable() {
    return currentExecutable;
  }

  public void setCurrentExecutable(ExecutableObject currentExecutable) {
    this.currentExecutable = currentExecutable;
  }

  public void setKillScore(int score) {
    killScore = score;
  }

  /**
   * Decrements a player's kill score. The {@code killScore} attribute can run below 1
   * in order to keep track of how many skulls to show on the player's dashboard.
   * When evaluated, any kill score below 1 will count as 1.
   */
  private void decrementKillScore() {
    if (killScore <= 2) {
      killScore -= 1;
    } else {
      killScore -= 2;
    }
  }

  public Buyable getCurrentBuying() {
    return currentBuying;
  }

  public void setCurrentBuying(Buyable currentBuying) {
    this.currentBuying = currentBuying;
  }


  public ExecutableObject getOldExecutable() {
    return oldExecutable;
  }

  public void setOldExecutable(ExecutableObject oldExecutable) {
    this.oldExecutable = oldExecutable;
  }

  /**
   * Given a list of powerUps and a map of AmmoColor,Integer returns a list of spendables
   * containing all the powerUps and ammos
   * @param powerUps powerups
   * @param budgetAmmo ammos
   * @return List of spendables
   */
  public static List<Spendable> setSpendable(List<PowerUp> powerUps,
      Map<AmmoColor, Integer> budgetAmmo) {
    List<Spendable> spendableList = new ArrayList<>();
    int index = 0;
    int redAmmo = budgetAmmo.get(AmmoColor.RED);
    int blueAmmo = budgetAmmo.get(AmmoColor.BLUE);
    int yellowAmmo = budgetAmmo.get(AmmoColor.YELLOW);

    for (int i = 0; i < blueAmmo; i++) {
      spendableList.add(index, AmmoColor.BLUE);
      index++;
    }
    for (int i = 0; i < redAmmo; i++) {
      spendableList.add(index, AmmoColor.RED);
      index++;
    }
    for (int i = 0; i < yellowAmmo; i++) {
      spendableList.add(index, AmmoColor.YELLOW);
      index++;
    }
    for (PowerUp powerUp : powerUps) {
      spendableList.add(index, powerUp);
      index++;
    }
    return spendableList;
  }

  /**
   * Gson serialization.
   *
   * @return JSON string containing serialized object
   */
  public String serialize() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy());
    Gson gson = gsonBuilder.create();
    return gson.toJson(this);
  }

  /**
   * Creates Player object from a JSON serialized object.
   *
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
