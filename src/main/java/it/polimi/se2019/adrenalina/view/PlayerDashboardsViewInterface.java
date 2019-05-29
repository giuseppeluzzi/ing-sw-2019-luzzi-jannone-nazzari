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
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PlayerDashboardsViewInterface extends Observer, RemoteObservable, Serializable {

  default List<EventType> getHandledEvents() {
    List<EventType> registeredEvents = new ArrayList<>();

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

    return registeredEvents;
  }

  void switchToFinalFrenzy(PlayerColor playerColor) throws RemoteException;

  void showPaymentOption(BuyableType buyableType, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUp,
      Map<AmmoColor, Integer> budgetAmmo) throws RemoteException;

  void showTurnActionSelection(List<TurnAction> actions) throws RemoteException;

  void showWeaponSelection(List<Weapon> weapons) throws RemoteException;

  void showEffectSelection(Weapon weapon) throws RemoteException;

  void showPowerUpSelection(List<PowerUp> powerUps) throws RemoteException;
}
