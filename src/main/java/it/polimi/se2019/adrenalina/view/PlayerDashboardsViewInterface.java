package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerDashboardsViewInterface extends RemoteObservable, Serializable {
  void addPlayer(Player player) throws RemoteException;
  List<Player> getPlayers() throws RemoteException;
  void reset(Player player) throws RemoteException;
  void switchToFinalFrenzy(Player player) throws RemoteException;
  void showPaymentOption(Buyable item) throws RemoteException;
  void showTurnActionSelection(List<TurnAction> actions) throws RemoteException;
  void showWeaponSelect(List<Weapon> weapons);

  void update(Event event) throws RemoteException;
}
