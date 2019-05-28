package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public interface CharactersViewInterface extends RemoteObservable, Serializable {

  List<Player> getPlayers() throws RemoteException;

  void addPlayer(Player player) throws RemoteException;

  void setSelected(PlayerColor playerColor) throws RemoteException;

  void removeSelected() throws RemoteException;

  void showDeath(PlayerColor playerColor) throws RemoteException;

  void update(PlayerPositionUpdate event) throws RemoteException;

  void update(PlayerStatusUpdate event) throws RemoteException;

  void update(Event event) throws RemoteException;
}
