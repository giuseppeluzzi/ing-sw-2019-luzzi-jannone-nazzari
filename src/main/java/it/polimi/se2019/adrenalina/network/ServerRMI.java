package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI extends UnicastRemoteObject implements ServerInterface  {
  private static final long serialVersionUID = -8473577041428305191L;
  private final transient Server server;

  public ServerRMI(Server server) throws RemoteException {
    this.server = server;
  }

  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    server.addClient(client);
  }

  @Override
  public void createGame(BoardController boardController) throws RemoteException {
    //
  }

  @Override
  public BoardController getPendingGame(boolean domination) throws RemoteException {
    return null;
  }
}
