package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
  void addClient(ClientInterface client)
      throws RemoteException, InvalidPlayerException;
  BoardController getGameByClient(ClientInterface client) throws RemoteException, InvalidPlayerException;
  BoardController getGameByPlayer(Player player) throws RemoteException, InvalidPlayerException;

  void ping(ClientInterface client) throws RemoteException;
}
