package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerChatEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface, Runnable {
  private static final long serialVersionUID = -8473577041428305191L;
  private static final int RMI_PORT = 1099;
  private final ArrayList<BoardController> games;
  private final ArrayList<ClientInterface> clients;

  public Server() throws RemoteException {
    games = new ArrayList<>();
    clients = new ArrayList<>();
  }

  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    clients.add(client);
    client.showMessage("ciaooo");
    Log.info("Server", "New client connected! (" + client.getName() + " - Domination: " + client.isDomination() + ")");
  }

  public void createGame(BoardController boardController) throws RemoteException {
    //
  }

  public BoardController getPendingGame(boolean domination) throws RemoteException {
    return null;
  }

  @Override
  public void run() {
    while (true) {
      for (ClientInterface client: clients) {
        String name = "";
        try {
          client.ping();
          name = client.getName();
        } catch (RemoteException e) {
          Log.severe(name + " disconnected!");
        }
      }
      try {
        sleep(500);
      } catch (InterruptedException e) {
        Log.severe("Interrupted!");
      }
    }
  }
}
