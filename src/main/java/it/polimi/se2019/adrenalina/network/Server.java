package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server implements ServerInterface {
  private static final int RMI_PORT = 1099;
  private final ArrayList<BoardController> games;
  private final ArrayList<ClientInterface> clients;

  public Server() {
    games = new ArrayList<>();
    clients = new ArrayList<>();
  }

  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    clients.add(client);
    Log.info("Server", "New client connected! (" + client.getName() + " - Domination: " + client.isDomination() + ")");
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
