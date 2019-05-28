package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerDashboardsViewInterface extends RemoteObservable, Serializable {

  void addPlayer(Player player) throws RemoteException;

  List<Player> getPlayers() throws RemoteException;

  void switchToFinalFrenzy(PlayerColor playerColor) throws RemoteException;

  void showPaymentOption(Buyable item) throws RemoteException;

  void showTurnActionSelection(List<TurnAction> actions) throws RemoteException;

  void showWeaponSelection(List<Weapon> weapons) throws RemoteException;

  void showEffectSelection(Weapon weapon) throws RemoteException;

  void showPowerUpSelection(List<PowerUp> powerUps) throws RemoteException;

  void update(PlayerDamagesTagsUpdate event) throws RemoteException;

  void update(PlayerScoreUpdate event) throws RemoteException;

  void update(PlayerKillScoreUpdate event) throws RemoteException;

  void update(PlayerStatusUpdate event) throws RemoteException;

  void update(PlayerAmmoUpdate event) throws RemoteException;

  void update(OwnWeaponUpdate event) throws RemoteException;

  void update(EnemyWeaponUpdate event) throws RemoteException;

  void update(EnemyPowerUpUpdate event) throws RemoteException;

  void update(OwnPowerUpUpdate event) throws RemoteException;

  void update(CurrentPlayerUpdate event) throws RemoteException;

  void update(Event event) throws RemoteException;
}
