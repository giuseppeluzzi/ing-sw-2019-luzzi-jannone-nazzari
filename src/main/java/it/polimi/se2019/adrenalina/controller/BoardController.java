package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardController extends UnicastRemoteObject implements Runnable, Observer {

  private static final long serialVersionUID = 5651066204312828750L;

  private final transient Board board;

  private final TurnController turnController;
  private final AttackController attackController;
  private final PlayerController playerController;

  private final transient HashMap<ClientInterface, String> clientsName;

  private final HashMap<ClientInterface, BoardViewInterface> boardViews;
  private final HashMap<ClientInterface, CharactersViewInterface> charactersViews;
  private final HashMap<ClientInterface, PlayerDashboardsViewInterface> playerDashboardViews;

  private final List<GameMap> maps;
  private int selectedMap;

  private final transient Timer timer;
  private final Random random;

  private final Set<EventType> registeredEvents = new HashSet<>();

  public BoardController(boolean domination) throws RemoteException {

    if (domination) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }

    clientsName = new HashMap<>();
    boardViews = new HashMap<>();
    charactersViews = new HashMap<>();
    playerDashboardViews = new HashMap<>();
    maps = new ArrayList<>();
    timer = new Timer();
    random = new Random();

    registeredEvents.add(EventType.FINAL_FRENZY_TOGGLE_EVENT);
    registeredEvents.add(EventType.MAP_SELECTION_EVENT);
    registeredEvents.add(EventType.PLAYER_COLOR_SELECTION_EVENT);

    turnController = new TurnController(this);
    attackController = new AttackController(this);
    playerController = new PlayerController(this);

    loadWeapons();
    loadPowerUps();
    loadMaps();
    loadAmmoCards();
  }

  private void loadPowerUps() {
    board.addPowerUp(new Teleporter(AmmoColor.RED));
    board.addPowerUp(new TagbackGrenade(AmmoColor.RED));
    board.addPowerUp(new Newton(AmmoColor.RED));
    board.addPowerUp(new TargetingScope(AmmoColor.RED));

    board.addPowerUp(new Teleporter(AmmoColor.BLUE));
    board.addPowerUp(new TagbackGrenade(AmmoColor.BLUE));
    board.addPowerUp(new Newton(AmmoColor.BLUE));
    board.addPowerUp(new TargetingScope(AmmoColor.BLUE));

    board.addPowerUp(new Teleporter(AmmoColor.YELLOW));
    board.addPowerUp(new TagbackGrenade(AmmoColor.YELLOW));
    board.addPowerUp(new Newton(AmmoColor.YELLOW));
    board.addPowerUp(new TargetingScope(AmmoColor.YELLOW));
  }

  public Board getBoard() {
    return board;
  }

  public TurnController getTurnController() {
    return turnController;
  }

  public AttackController getAttackController() {
    return attackController;
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  /**
   * Loads maps from json
   */
  private void loadMaps() {
    try (Stream<Path> weaponStream = Files.walk(
        Paths.get(BoardController.class.getResource("/maps").toURI()))) {
      weaponStream.filter(x -> x.toFile().isFile()).filter(Files::isReadable)
          .forEach(this::loadMap);
    } catch (URISyntaxException | IOException e) {
      Log.critical("No maps found");
    }
    if (maps.isEmpty()) {
      Log.critical("No maps found");
    }
  }

  /**
   * Returns a set of valid maps for a given number of players
   * @param players number of players
   * @return a set of GameMap
   */
  public List<GameMap> getValidMaps(int players) {
    List<GameMap> validMaps = new ArrayList<>();
    for (GameMap map : new ArrayList<>(maps)) {
      if (map.getMinPlayers() == 0 || map.getMaxPlayers() == 0 ||
          players >= map.getMinPlayers() && players <= map.getMaxPlayers()) {
        validMaps.add(map);
      }
    }
    return validMaps;
  }

  /**
   * Loads a single map from a file
   * @param mapPath Path of a map
   */
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

  /**
   * Load weapons from json
   */
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

  private void loadAmmoCards() {
    try (Stream<Path> ammoCardStream = Files.walk(
        Paths.get(BoardController.class.getResource("/img/ammo").toURI()))) {
      ammoCardStream.filter(x -> x.toFile().isFile()).filter(Files::isReadable)
          .forEach(filePath -> {
            String name = filePath.getFileName().toString()
                .replace("ammo_", "")
                .replace(".png", "");
            if (! "back".equalsIgnoreCase(name)) {
              int red = 3 - name.replace("R", "").length();
              int blue = 3 - name.replace("B", "").length();
              int yellow = 3 - name.replace("Y", "").length();
              int powerUp = 3 - name.replace("P", "").length();
              board.addAmmoCard(new AmmoCard(red, blue, yellow, powerUp));
            }
          });
    } catch (URISyntaxException | IOException e) {
      Log.critical("No ammo card found");
    }
    if (board.getAmmoCards().isEmpty()) {
      Log.critical("No ammo card found");
    }
  }

  /**
   * Adds a new player to a board in LOBBY status or a returning player (who had previously
   * disconnected) to a board where a game is in progress.
   *
   * @param player the player to be added.
   * @throws FullBoardException thrown if the board already has 5 players.
   * @throws PlayingBoardException thrown if the status of the board is not LOBBY (a game is already
   * in progress or ended) and the player does not belong to that game.
   * @throws EndedGameException thrown if this board hosts a game which has already ended.
   */
  public void addPlayer(Player player) throws FullBoardException,
      PlayingBoardException, EndedGameException {
    if (board.getStatus() == BoardStatus.END) {
      throw new EndedGameException();
    } else if (board.getStatus() == BoardStatus.LOBBY) {
      if (board.getPlayers().size() >= 5) {
        throw new FullBoardException();
      }

      setViews(player);

      board.addPlayer(player);
      player.setStatus(PlayerStatus.WAITING);

      if (board.getPlayers().size() >= 2) {
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

  /**
   * Starts a timer both server-side and on each client
   */
  private void startJoinTimer() {
    timer.start(Configuration.getInstance().getJoinTimeout(), this::chooseMap);

    getActivePlayers().stream().forEach(p -> {
      try {
        boardViews.get(p.getClient()).startTimer(Configuration.getInstance().getJoinTimeout());
      } catch (RemoteException e) {
        Log.exception(e);
      }
    });
  }

  /**
   * Verifies is a map is selected otherwise one is selected taking into account the number of
   * players
   */
  private void chooseMap() {
    if (selectedMap == 0) {
      List<GameMap> validMaps = getValidMaps(board.getPlayers().size());
      selectedMap = validMaps.get(random.nextInt(validMaps.size())).getId();
    }

    for (GameMap map : new ArrayList<>(maps)) {
      if (map.getId() == selectedMap) {
        prepareMap(map);

      }
      maps.remove(map);
    }
    Log.info("Selected map #" + selectedMap);
    run();
  }

  private void prepareMap(GameMap map) {
    for (Square square : map.getSquares()) {
      Square realSquare = new Square(square.getPosX(),
          square.getPosY(),
          square.getColor(),
          square.getEdge(Direction.NORTH),
          square.getEdge(Direction.EAST),
          square.getEdge(Direction.SOUTH),
          square.getEdge(Direction.WEST),
          board);
      if (square.isSpawnPoint()) {
        realSquare.setSpawnPoint(true);
      }
      board.setSquare(realSquare);
    }

    placeAmmoCard();
  }

  private void placeAmmoCard() {
    for (Square square : board.getSquares()) {
      if (! square.isSpawnPoint()) {
        AmmoCard ammoCard = board.getAmmoCards().get(0);
        board.drawAmmoCard(ammoCard);
        square.setAmmoCard(ammoCard);
      }
    }
  }


  /**
   * Removes a player from a board in LOBBY status or sets the player's status to DISCONNECTED if
   * the game on that board is already in progress.
   *
   * @param player the player to be removed.
   */
  public void removePlayer(Player player) throws InvalidPlayerException {
    clientsName.remove(player.getClient());
    if (board.getStatus() == BoardStatus.LOBBY) {
      board.removePlayer(player.getColor());
    } else {
      player.setStatus(PlayerStatus.DISCONNECTED);
      player.setClient(null);
    }
  }

  /**
   * Gets a list of online player of this board
   *
   * @return a list of Player
   */
  public List<Player> getActivePlayers() {
    return board.getPlayers().stream()
        .filter(x -> x.getStatus() != PlayerStatus.DISCONNECTED)
        .collect(Collectors.toList());
  }

  /**
   * Sets needed views (BoardView, CharactersView and PlayerDashboardsView) on a Player
   * @param player a Player
   */
  private void setViews(Player player) {
    try {
      ClientInterface client = player.getClient();
      BoardViewInterface boardView = client.getBoardView();
      CharactersViewInterface charactersView = client.getCharactersView();
      PlayerDashboardsViewInterface playerDashboardsView = client.getPlayerDashboardsView();

      boardViews.put(client, boardView);
      charactersViews.put(client, charactersView);
      playerDashboardViews.put(client, playerDashboardsView);

      boardView.addObserver(this);
      boardView.addObserver(playerController);
      boardView.addObserver(attackController);
      charactersView.addObserver(this);
      charactersView.addObserver(playerController);
      charactersView.addObserver(attackController);
      playerDashboardsView.addObserver(this);
      playerDashboardsView.addObserver(playerController);
      playerDashboardsView.addObserver(attackController);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public Map<ClientInterface, String> getClients() {
    return new HashMap<>(clientsName);
  }

  public void addClient(ClientInterface client) {
    try {
      clientsName.put(client, client.getName());
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public boolean containsClient(ClientInterface client) {
    return clientsName.containsKey(client);
  }

  public Player getPlayerByClient(ClientInterface client) throws InvalidPlayerException {
    return board.getPlayerByName(clientsName.get(client));
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
    board.setStatus(BoardStatus.MATCH);
    board.setFinalFrenzyActive(false);
    board.setCurrentPlayer(null);

    Deque<PlayerColor> freeColors = new ArrayDeque<>(board.getFreePlayerColors());
    for (Player player: board.getPlayers()) {
      if (player.getColor() == null) {
        player.setColor(freeColors.pop());
      }
    }

    turnController.prepare();
    turnController.executeGameActionQueue();
  }

  public void update(FinalFrenzyToggleEvent event) {
    board.setFinalFrenzySelected(event.isEnabled());
  }

  public void update(MapSelectionEvent event) {
    if (event.getMap() >= 1 && event.getMap() <= 4) {
      selectedMap = event.getMap();
    }
  }

  public void update(PlayerColorSelectionEvent event) {
    if (! board.getFreePlayerColors().contains(event.getNewPlayerColor())) {
      return ;
    }

    Player player;

    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    player.setColor(event.getNewPlayerColor());

    try {
      player.getClient().setPlayerColor(event.getNewPlayerColor());
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("BoardController", "Event received: " + event.getEventType());
      try {
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        //
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BoardController &&
        clientsName.equals(obj);
  }

  @Override
  public int hashCode() {
    return clientsName.hashCode();
  }
}
