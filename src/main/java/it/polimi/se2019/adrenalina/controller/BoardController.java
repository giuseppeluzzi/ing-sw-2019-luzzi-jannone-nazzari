package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.controller.event.MapSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardController extends UnicastRemoteObject implements Runnable, Observer {
  private static final long serialVersionUID = 5651066204312828750L;

  private final transient Board board;
  private final AttackController attackController;
  private final PlayerController playerController;

  private final HashMap<Player, ClientInterface> clients;

  private final HashMap<ClientInterface, BoardViewInterface> boardViews;
  private final HashMap<ClientInterface, CharactersViewInterface> charactersViews;
  private final HashMap<ClientInterface, PlayerDashboardsViewInterface> playerDashboardViews;

  private final List<GameMap> maps;
  private int selectedMap;

  private final transient Timer timer;
  private final Random random;

  public BoardController(boolean domination) throws RemoteException {

    if (domination) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }

    clients = new HashMap<>();
    boardViews = new HashMap<>();
    charactersViews = new HashMap<>();
    playerDashboardViews = new HashMap<>();
    maps = new ArrayList<>();
    timer = new Timer();
    random = new Random();


    attackController = new AttackController(this);
    playerController = new PlayerController(this);

    loadWeapons();
    loadMaps();
  }

  private void loadMaps() {
    try (Stream<Path> weaponStream = Files.walk(
        Paths.get(BoardController.class.getResource("/maps").toURI()))) {
      weaponStream.filter(x -> x.toFile().isFile()).filter(Files::isReadable).forEach(this::loadMap);
    } catch (URISyntaxException | IOException e) {
      Log.critical("No maps found");
    }
    if (maps.isEmpty()) {
      Log.critical("No maps found");
    }
  }

  private void loadMap(Path mapPath) {
    Gson gson = new Gson();
    try {
      String json = new String(Files.readAllBytes(mapPath), StandardCharsets.UTF_8);
      GameMap gameMap = gson.fromJson(json, GameMap.class);
      maps.add(gameMap);
    } catch (IOException e) {
      Log.critical("Map not found");
    }
  }

  private void loadWeapons() {
    try (Stream<Path> weaponStream = Files.walk(
        Paths.get(BoardController.class.getResource("/weapons").toURI()))) {
      weaponStream.filter(x -> x.toFile().isFile()).filter(Files::isReadable)
          .forEach(filePath -> {
            String json = null;
            try {
              json = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            } catch (IOException e) {
              Log.severe(filePath.getFileName() + " is an invalid weapon!");
            }
            board.addWeapon(Weapon.deserialize(json));
          });
    } catch (URISyntaxException | IOException e) {
      Log.critical("No weapons found");
    }
    if (board.getWeapons().isEmpty()) {
      Log.critical("No weapons found");
    }
  }

  public Board getBoard() {
    return board;
  }

  public AttackController getAttackController() {
    return attackController;
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  /**
   * Adds a new player to a board in LOBBY status or a returning
   * player (who had previously disconnected) to a board where a game
   * is in progress.
   * @param player the player to be added.
   * @throws FullBoardException thrown if the board already has 5 players.
   * @throws PlayingBoardException thrown if the status of the board is not
   * LOBBY (a game is already in progress or ended) and the player does not
   * belong to that game.
   * @throws EndedGameException thrown if this board hosts a game which has
   * already ended.
   */
  public void addPlayer(Player player) throws FullBoardException,
      PlayingBoardException, EndedGameException {
    if (board.getStatus() == BoardStatus.END) {
      throw new EndedGameException();
    } else if (board.getStatus() == BoardStatus.LOBBY) {
      if (board.getPlayers().size() > 5) {
        throw new FullBoardException();
      }

      setViews(player);

      board.addPlayer(player);
      player.setStatus(PlayerStatus.WAITING);

      if (board.getPlayers().size() >= 3) {
        startJoinTimer();
      }

    } else {
      if (board.getPlayers().contains(player)) {
        player.setStatus(PlayerStatus.PLAYING);
      } else {
        throw new PlayingBoardException("Board isn't in LOBBY status");
      }
    }
  }

  private void startJoinTimer() {
    timer.start(Configuration.getInstance().getJoinTimeout(), this::startGame);

    getActivePlayers().stream().forEach(p -> {
      try {
        boardViews.get(getPlayerClient(p)).startTimer(Configuration.getInstance().getJoinTimeout());
      } catch (InvalidPlayerException ignored) {
        //
      } catch (RemoteException e) {
        Log.exception(e);
      }
    });
  }

  public List<GameMap> getValidMaps(int players) {
    List<GameMap> validMaps = new ArrayList<>();
    for (GameMap map: new ArrayList<>(maps)) {
      if (map.getMinPlayers() == 0 || map.getMaxPlayers() == 0 ||
          players >= map.getMinPlayers() && players <= map.getMaxPlayers()) {
        validMaps.add(map);
      }
    }
    return validMaps;
  }

  private void startGame() {
    if (selectedMap == 0) {
      List<GameMap> validMaps = getValidMaps(board.getPlayers().size());
      selectedMap = validMaps.get(random.nextInt(maps.size())).getId();
    }

    for (GameMap map: new ArrayList<>(maps)) {
      if (map.getId() != selectedMap) {
        maps.remove(map);
      }
    }
    Log.info("Selected map #" + selectedMap);
  }


  private void setViews(Player player) {
    try {
      ClientInterface client = clients.get(player);
      BoardViewInterface boardView = client.getBoardView();
      CharactersViewInterface charactersView = client.getCharactersView();
      PlayerDashboardsViewInterface playerDashboardsView = client.getPlayerDashboardsView();

      boardViews.put(client, boardView);
      charactersViews.put(client, charactersView);
      playerDashboardViews.put(client, playerDashboardsView);

      boardView.addObserver(this);
      charactersView.addObserver(playerController);
      playerDashboardsView.addObserver(playerController);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Removes a player from a board in LOBBY status or sets the player's
   * status to DISCONNECTED if the game on that board is already in progress.
   * @param player the player to be removed.
   */
  void removePlayer(Player player) {
    if (board.getStatus() == BoardStatus.LOBBY) {
      board.removePlayer(player.getColor());
    } else {
      player.setStatus(PlayerStatus.DISCONNECTED);
      removePlayerClient(player);
    }
  }

  /**
   * Gets a list of online player of this board
   * @return a list of Player
   */
  public List<Player> getActivePlayers() {
    return board.getPlayers().stream()
        .filter(x -> x.getStatus() != PlayerStatus.DISCONNECTED)
        .collect(Collectors.toList());
  }

  public ClientInterface getPlayerClient(Player player) throws InvalidPlayerException {
    if (!clients.containsKey(player)) {
      throw new InvalidPlayerException("This player doesn't exists");
    }
    return clients.get(player);
  }

  public Map<Player, ClientInterface> getClients() {
    return new HashMap<>(clients);
  }

  public boolean containsClient(ClientInterface client) {
    return clients.containsValue(client);
  }

  public Player getPlayerByClient(ClientInterface client) throws InvalidPlayerException {
    for (Entry<Player, ClientInterface> c: clients.entrySet()) {
      if (c.getValue().equals(client)) {
        return c.getKey();
      }
    }
    throw new InvalidPlayerException("This player doesn't exists");
  }

  public void setPlayerClient(Player player, ClientInterface client) {
    clients.put(player, client);
  }

  public void removePlayerClient(Player player) {
    clients.remove(player);
  }

  public void setClientBoardView(ClientInterface client, BoardViewInterface view) {
    boardViews.put(client, view);
  }

  public void setClientCharacterView(ClientInterface client, CharactersViewInterface view) {
    charactersViews.put(client, view);
  }

  public void setClientDashboardView(ClientInterface client, PlayerDashboardsViewInterface view) {
    playerDashboardViews.put(client, view);
  }

  @Override
  public void run() {
    // TODO: game manager
  }

  @Override
  public void update(Event event) {
    throw new UnsupportedOperationException();
  }

  public void update(FinalFrenzyToggleEvent event) {
    board.setFinalFrenzySelected(event.isEnabled());
  }

  public void update(MapSelectionEvent event) {
    if (event.getMap() >= 1 && event.getMap() <= 4) {
      selectedMap = event.getMap();
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BoardController &&
        clients.equals(obj);
  }

  @Override
  public int hashCode() {
    return clients.hashCode();
  }
}
