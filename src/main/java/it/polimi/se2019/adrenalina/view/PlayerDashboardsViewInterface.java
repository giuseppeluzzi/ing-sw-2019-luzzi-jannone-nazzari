package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.BuyableType;
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

  default List<EventType> getHandledEvents() throws RemoteException {
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

  void showEffectSelection(Weapon weapon, List<Effect> effects) throws RemoteException;

  void showPowerUpSelection(List<PowerUp> powerUps) throws RemoteException;

  void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons) throws RemoteException;
}
