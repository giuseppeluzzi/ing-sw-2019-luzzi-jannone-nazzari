package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface {
  private static final long serialVersionUID = -8473577041428305191L;
  private final ArrayList<BoardController> games;
  private final ArrayList<ClientInterface> clients;

  public volatile boolean running = true;

  public Server() throws RemoteException {
    games = new ArrayList<>();
    clients = new ArrayList<>();

    new Thread(() -> {
      while (running) {
        for (ClientInterface client: clients) {
          try {
            client.ping();
          } catch (RemoteException e) {
            Log.severe("A client disconnected!");
            clients.remove(client);
            // TODO: remove client?
          }
        }
        try {
          sleep(500);
        } catch (InterruptedException e) {
          Log.severe("Server", "Pinging interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
        }
      }
    }).start();
  }

  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    clients.add(client);
    client.showMessage("ciaooo");
    Log.info("Server", "New client connected! (" + client.getName() + " - Domination: " + client.isDomination() + ")");
  }

  public void createGame(BoardController boardController) {
    //
  }

  public BoardController getPendingGame(boolean domination) {
    return null;
  }

  public void stop() {
    running = false;
  }

}
