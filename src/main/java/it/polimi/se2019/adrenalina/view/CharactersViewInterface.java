package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.rmi.RemoteException;
import java.util.List;

public interface CharactersViewInterface extends RemoteObservable {
  List<Player> getPlayers() throws RemoteException;
  void addPlayer(Player player) throws RemoteException;
  void setSelected(Player player) throws RemoteException;
  void removeSelected() throws RemoteException;
  void update(PlayerDeathEvent event) throws RemoteException;
  void update(PlayerSpawnEvent event) throws RemoteException;
  void update(PlayerMoveEvent event) throws RemoteException;
}
