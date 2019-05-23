package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements ServerInterface {

  private static final long serialVersionUID = 1666613338633244401L;
  private static final int PING_INTERVAL = 500;

  private final ArrayList<BoardController> games;
  private final HashMap<Player, BoardController> playing;
  private volatile boolean running = true;

  public Server() throws RemoteException {
    games = new ArrayList<>();
    playing = new HashMap<>();

    new Thread(() -> {
      while (running) {
        pingAll();
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
   * Pings every client in every game
   */
  private void pingAll() {
    for (BoardController game: new ArrayList<>(games)) {
      for (ClientInterface client: game.getClients().keySet()) {
        try {
          client.ping();
          Long lastPing = client.getLastPing();
        } catch (IOException e) {
          clientDisconnect(client);
        }
      }
    }
  }

  /**
   * Adds a client to an existing board or to a new board, per game mode.
   * @param client client to be added.
   * @throws RemoteException in case of network problems with RMI.
   */
  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    pingAll();
    Log.info("Server", "New client connected! (Name: " + client.getName() + " - Domination: " + client.isDomination() + ")");

    BoardController game = getPendingGame(client.isDomination());
    if (game == null) {
      game = new BoardController(client.isDomination());
      games.add(game);
    }
    game.addClient(client);

    Player player = game.getPlayerController().createPlayer(client.getName(),
        PlayerColor.values()[game.getBoard().getPlayers().size()]);
    player.setClient(client);
    client.setPlayerColor(player.getColor());

    try {
      game.addPlayer(player);
    } catch (FullBoardException e) {
      Log.severe("Tried to add a player to a full board");
    } catch (PlayingBoardException e) {
      Log.severe("Tried to add a new player to a playing board");
    } catch (EndedGameException e) {
      Log.severe("Tried to add a player to an ended game");
    }

    playing.put(player, game);
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
   * Gets a board of a client
   * @param client client to be found
   * @return the board in which the client is playing
   * @throws InvalidPlayerException if the client doesn't exists or isn't playing in any board
   */
  @Override
  public BoardController getGameByClient(ClientInterface client) throws InvalidPlayerException {
    for (BoardController game: games) {
      if (game.containsClient(client)) {
        return game;
      }
    }
    throw new InvalidPlayerException("This client isn't in any board");
  }

  /**
   * Gets a board of a player
   * @param client a client to be found
   * @return the board in which the player is playing
   * @throws InvalidPlayerException if the player doesn't exists or isn't playing in any board
   */
  @Override
  public BoardController getGameByPlayer(Player player) throws InvalidPlayerException {
    if (playing.containsKey(player)) {
      throw new InvalidPlayerException("This client isn't in any board");
    }
    return playing.get(player);
  }

  /**
   * Stops the server pinging.
   */
  private void stopPinging() {
    running = false;
  }

  /**
   * Called on client disconnection
   * @param client disconnected client
   */
  public void clientDisconnect(ClientInterface client) {
    for (BoardController game: games) {
      if (game.containsClient(client)) {
        try {
          Player player = game.getPlayerByClient(client);
          Log.info("server", "A client disconnected! (Name: " + player.getName() + ")");
          game.removePlayer(player);
          playing.remove(player);
          break;
        } catch (InvalidPlayerException ignored) {
          //
        }
      }
    }
  }
}
