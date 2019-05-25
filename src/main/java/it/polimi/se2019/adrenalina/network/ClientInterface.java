package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
  String getName() throws RemoteException;
  PlayerColor getPlayerColor() throws RemoteException;
  void setPlayerColor(PlayerColor color) throws RemoteException;
  boolean isDomination() throws RemoteException;
  void setDomination(boolean domination) throws RemoteException;

  void showMessage(MessageSeverity severity, String title, String message) throws RemoteException;
  void ping() throws IOException;
  Long getLastPing() throws IOException;

  void disconnect() throws RemoteException;

  BoardViewInterface getBoardView() throws RemoteException;
  CharactersViewInterface getCharactersView() throws RemoteException;
  PlayerDashboardsViewInterface getPlayerDashboardsView() throws RemoteException;
}
