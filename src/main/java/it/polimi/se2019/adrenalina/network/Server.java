package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.model.BoardStatus;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PlayerColor;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface {
  private static final int PING_INTERVAL = 500;

  private static final long serialVersionUID = 1666613338633244401L;
  private final ArrayList<BoardController> games;
  private final ArrayList<ClientInterface> clients;

  private volatile boolean running = true;

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
          sleep(PING_INTERVAL);
        } catch (InterruptedException e) {
          Log.severe("Server", "Pinging interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
        }
      }
    }).start();
  }

  /**
   * Adds a client to an existing board or to a new board, per game mode.
   * @param client client to be added.
   * @throws RemoteException in case of network problems with RMI.
   */
  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    clients.add(client);
    Log.info("Server", "New client connected! (Name: " + client.getName() + " - Domination: " + client.isDomination() + ")");

    BoardController game = getPendingGame(client.isDomination());
    if (game == null) {
      game = new BoardController(client.isDomination());
      games.add(game);
    }
    Player player = game.getPlayerController().createPlayer(client.getName(),
        PlayerColor.values()[game.getBoard().getPlayers().size()]);
  }

  /**
   * Finds a game in LOBBY with a free spot for a player and with a certain
   * game mode selected.
   * @param domination determines if the game should be in domination mode.
   * @return a board controller or null no free boards exist.
   */
  public BoardController getPendingGame(boolean domination) {
    for (BoardController game: games) {
      if (game.getBoard().getStatus() == BoardStatus.LOBBY &&
          game.getBoard().isDominationBoard() == domination &&
          game.getBoard().getPlayers().size() < 5) {
        return game;
      }
    }
    return null;
  }

  /**
   * Stops the server pinging.
   */
  private void stopPinging() {
    running = false;
  }

}
