package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
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
import java.util.Map.Entry;

public class Server extends UnicastRemoteObject implements ServerInterface {

  private static final long serialVersionUID = 1666613338633244401L;
  private static final int PING_INTERVAL = 500;
  public static final int MAX_NAME_LENGTH = 12;

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
    for (BoardController game : new ArrayList<>(games)) {
      for (ClientInterface client : game.getClients().keySet()) {
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
   * Adds a client to an existing board or to a new board, per game mode. If the player was already
   * playing and it's DISCONNECTED, the player will be added back to the previous board.
   *
   * @param client to be added
   * @throws RemoteException in case of network problems
   */
  @Override
  public void addClient(ClientInterface client) throws RemoteException {
    pingAll();
    Log.info("Server",
        "New client connected! (Name: " + client.getName() + " - Domination: " + client
            .isDomination() + ")");

    if (client.getName().length() > MAX_NAME_LENGTH) {
      client.disconnect("Nome troppo lungo (max. 12 caratteri), cambia nome e riprova!");
      Log.severe("Tried to add a player with a too long name (" + client.getName() + ")");
    }

    if (isPlayerConnected(client.getName())) {
      client.disconnect("Nome già in uso, cambia nome e riprova!");
      Log.severe("Tried to add a player with an already used name (" + client.getName() + ")");
      return;
    }

    Player player = null;
    BoardController game = null;
    BoardController previousGame = getGameByPlayerName(client.getName());

    if (previousGame != null) {
      try {
        player = previousGame.getBoard().getPlayerByName(client.getName());
        game = previousGame;
      } catch (InvalidPlayerException ignored) {
        //
      }
    }

    if (game == null) {
      game = getPendingGame(client.isDomination());

      if (game == null) {
        game = new BoardController(client.isDomination());
        games.add(game);
      }

      player = game.getPlayerController().createPlayer(client.getName(),
          PlayerColor.values()[game.getBoard().getPlayers().size()]);
    }

    game.addClient(client);
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
   * Finds a game in LOBBY with a free spot for a player and with a certain game mode selected.
   *
   * @param domination determines if the game should be in domination mode.
   * @return a board controller or null no free boards exist.
   */
  public BoardController getPendingGame(boolean domination) {
    for (BoardController game : games) {
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
   *
   * @param client client to be found
   * @return the board in which the client is playing
   * @throws InvalidPlayerException if the client doesn't exists or isn't playing in any board
   */
  @Override
  public BoardController getGameByClient(ClientInterface client) throws InvalidPlayerException {
    for (BoardController game : games) {
      if (game.containsClient(client)) {
        return game;
      }
    }
    throw new InvalidPlayerException("This client isn't in any board");
  }

  /**
   * Gets a board of a player
   *
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
   * Verifies if exists a connected player with a given name
   *
   * @param name name of the playe to be found
   * @return true if there is a connected player (playerStatus != DISCONNECTED) with the specified
   * name
   */
  public boolean isPlayerConnected(String name) {
    for (BoardController game : games) {
      for (Player activePlayer : game.getActivePlayers()) {
        if (activePlayer.getName().equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Gets the board where exists a player with the specified name
   *
   * @param name name of the playe to be found
   * @return the board in which the player is playing or null
   */
  public BoardController getGameByPlayerName(String name) {
    for (Entry<Player, BoardController> player : playing.entrySet()) {
      if (player.getKey().getName().equals(name)) {
        return player.getValue();
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

  /**
   * Called on client disconnection
   *
   * @param client disconnected client
   */
  public void clientDisconnect(ClientInterface client) {
    for (BoardController game : games) {
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
