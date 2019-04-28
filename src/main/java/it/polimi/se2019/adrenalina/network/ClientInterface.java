package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
  String getName() throws RemoteException;
  boolean isDomination() throws RemoteException;
  void showMessage(String text) throws RemoteException;
  void ping() throws IOException;
  void disconnect() throws RemoteException;
  BoardViewInterface getBoardView() throws RemoteException;
  CharactersViewInterface getCharactersView() throws RemoteException;
  PlayerDashboardsViewInterface getPlayerDashboardsView() throws RemoteException;
}
