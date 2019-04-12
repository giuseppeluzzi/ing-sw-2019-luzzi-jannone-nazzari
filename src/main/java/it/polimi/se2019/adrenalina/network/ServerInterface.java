package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
  void addClient(ClientInterface client) throws RemoteException;
  void createGame(BoardController boardController) throws RemoteException;
  BoardController getPendingGame(boolean domination) throws RemoteException;
}
