package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerDashboardsViewInterface extends RemoteObservable {
  void addPlayer(Player player) throws RemoteException;
  List<Player> getPlayers() throws RemoteException;
  void reset(Player player) throws RemoteException;
  void switchToFinalFrenzy(Player player) throws RemoteException;
  //void update(Event event) throws RemoteException;
}
