package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class PlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface, Observer {

  private static final long serialVersionUID = -6150690431150041388L;
  private final Set<EventType> registeredEvents = new HashSet<>();
  private final BoardView boardView;
  private final List<Player> players;

  protected PlayerDashboardsView(BoardView boardView) {
    players = new ArrayList<>();
    this.boardView = boardView;
  }

  public Player getPlayerByColor(PlayerColor playerColor) {
    for (Player player : getPlayers()) {
      if (player.getColor() == playerColor) {
        return player;
      }
    }
    registeredEvents.add(EventType.PLAYER_DAMAGES_TAGS_UPDATE);
    registeredEvents.add(EventType.PLAYER_SCORE_UPDATE);
    registeredEvents.add(EventType.PLAYER_KILL_SCORE_UPDATE);
    registeredEvents.add(EventType.PLAYER_STATUS_UPDATE);
    registeredEvents.add(EventType.PLAYER_AMMO_UPDATE);
    registeredEvents.add(EventType.PLAYER_WEAPON_UPDATE);
    registeredEvents.add(EventType.ENEMY_WEAPON_UPDATE);
    registeredEvents.add(EventType.ENEMY_POWER_UP_UPDATE);
    registeredEvents.add(EventType.OWN_POWER_UP_UPDATE);
    registeredEvents.add(EventType.CURRENT_PLAYER_UPDATE);
    return null;
  }

  @Override
  public abstract void showPaymentOption(BuyableType buyableType,
      Map<AmmoColor, Integer> buyableCost, List<PowerUp> budgetPowerUp,
      Map<AmmoColor, Integer> budgetAmmo);

  @Override
  public abstract void showTurnActionSelection(List<TurnAction> actions);

  @Override
  public abstract void showWeaponSelection(List<Weapon> weapons);

  @Override
  public abstract void showEffectSelection(Weapon weapon);

  @Override
  public abstract void showPowerUpSelection(List<PowerUp> powerUps);

  @Override
  public void addPlayer(Player player) {
    players.add(player);
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public void update(PlayerDamagesTagsUpdate event) throws RemoteException {
    Player player = getPlayerByColor(event.getPlayerColor());
    player.updateDamages(event.getDamages());
    player.updateTags(event.getTags());
  }

  @Override
  public void update(PlayerScoreUpdate event) throws RemoteException {
    getPlayerByColor(event.getPlayerColor()).setScore(event.getScore());
  }

  @Override
  public void update(PlayerKillScoreUpdate event) throws RemoteException {
    getPlayerByColor(event.getPlayerColor()).setKillScore(event.getKillScore());
  }

  @Override
  public void update(PlayerStatusUpdate event) throws RemoteException {
    getPlayerByColor(event.getPlayerColor()).setStatus(event.getPlayerStatus());
  }

  @Override
  public void update(PlayerAmmoUpdate event) throws RemoteException {
    Player player = getPlayerByColor(event.getPlayerColor());
    player.updateAmmo(AmmoColor.BLUE, event.getBlue());
    player.updateAmmo(AmmoColor.RED, event.getRed());
    player.updateAmmo(AmmoColor.YELLOW, event.getYellow());

  }

  @Override
  public void update(OwnWeaponUpdate event) throws RemoteException {
    getPlayerByColor(event.getPlayerColor()).updateWeapons(event.getWeapons());
  }

  @Override
  public void update(EnemyWeaponUpdate event) throws RemoteException {
    Player player = getPlayerByColor(event.getPlayerColor());
    player.updateWeapons(event.getUnloadedWeapons());
    player.setWeaponCount(event.getNumWeapons());
  }

  @Override
  public void update(EnemyPowerUpUpdate event) throws RemoteException {
    getPlayerByColor(event.getPlayerColor()).setPowerUpCount(event.getPowerUpsNum());
  }

  @Override
  public void update(OwnPowerUpUpdate event) throws RemoteException {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<PowerUpType, Map<AmmoColor, Integer>> entrySet : event.getPowerUps()
        .entrySet()) {
      if (entrySet.getKey() == PowerUpType.NEWTON) {
        powerUps.addAll(addNewtons(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TELEPORTER) {
        powerUps.addAll(addTeleporters(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TAGBACK_GRANADE) {
        powerUps.addAll(addTagbackGranades(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TARGETING_SCOPE) {
        powerUps.addAll(addTargetingScopes(entrySet.getValue()));
      }
    }
    getPlayerByColor(event.getPlayerColor()).updatePowerUps(powerUps);
  }

  private List<PowerUp> addNewtons(Map<AmmoColor, Integer> entrySet) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (AmmoColor color : AmmoColor.values()) {
      for (int i = 0; i < entrySet.get(color); i++) {
        powerUps.add(new Newton(color));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTargetingScopes(Map<AmmoColor, Integer> entrySet) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (AmmoColor color : AmmoColor.values()) {
      for (int i = 0; i < entrySet.get(color); i++) {
        powerUps.add(new TargetingScope(color));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTagbackGranades(Map<AmmoColor, Integer> entrySet) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (AmmoColor color : AmmoColor.values()) {
      for (int i = 0; i < entrySet.get(color); i++) {
        powerUps.add(new TagbackGrenade(color));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTeleporters(Map<AmmoColor, Integer> entrySet) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (AmmoColor color : AmmoColor.values()) {
      for (int i = 0; i < entrySet.get(color); i++) {
        powerUps.add(new Teleporter(color));
      }
    }
    return powerUps;
  }


  @Override
  public void update(CurrentPlayerUpdate event) throws RemoteException {
    boardView.getBoard().setCurrentPlayer(event.getCurrentPlayerColor());
  }

  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("PlayerDashboardsView", "Event received: " + event.getEventType());
      try {
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        //
      }
    }
  }
}
