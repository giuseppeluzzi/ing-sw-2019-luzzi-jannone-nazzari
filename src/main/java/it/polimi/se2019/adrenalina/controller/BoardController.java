package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.utils.*;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Controller in charge of handling the game board.
 */
public class BoardController extends UnicastRemoteObject implements Runnable, Observer {

  private static final long serialVersionUID = 5651066204312828750L;

  private final transient Board board;

  private TurnController turnController;
  private AttackController attackController;
  private PlayerController playerController;

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
    registeredEvents.add(EventType.BOARD_SKULLS_UPDATE);

    loadWeapons();
    loadPowerUps();
    loadMaps();
    loadAmmoCards();
  }

  private void loadPowerUps() {
    for (AmmoColor color : AmmoColor.getValidColor()) {
      for (int i = 0; i < 4; i++) {
        board.addPowerUp(new Teleporter(color));
        board.addPowerUp(new TagbackGrenade(color));
        board.addPowerUp(new Newton(color));
        board.addPowerUp(new TargetingScope(color));
      }
    }
  }

  public Board getBoard() {
    return board;
  }

  public Player getPlayer(PlayerColor color) {
    Player player;
    try {
      player = board.getPlayerByColor(color);
    } catch (InvalidPlayerException ignored) {
      return null;
    }
    return player;
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

  public void setTurnController(TurnController turnController) {
    this.turnController = turnController;
  }

  public void setAttackController(AttackController attackController) {
    this.attackController = attackController;
  }

  public void setPlayerController(PlayerController playerController) {
    this.playerController = playerController;
  }

  /**
   * Loads maps from json.
   */
  private void loadMaps() {
    Gson gson = new Gson();
    for (String mapName : ServerConfig.getInstance().getMapFiles()) {
      try {
        String json = IOUtils.readResourceFile("maps/" + mapName);
        GameMap gameMap = gson.fromJson(json, GameMap.class);
        maps.add(gameMap);
      } catch (IOException e) {
        Log.critical(mapName + " not found");
      }
    }

    if (maps.isEmpty()) {
      Log.critical("No maps found");
    }
  }

  /**
   * Returns a set of valid maps for a given number of players.
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
   * Load weapons from json.
   */
  private void loadWeapons() {
    for (String weaponName : ServerConfig.getInstance().getWeaponFiles()) {
      try {
        String json = IOUtils.readResourceFile("weapons/" + weaponName);
        board.addWeapon(Weapon.deserialize(json));
      } catch (IOException e) {
        Log.critical(weaponName + " not found");
      }
    }

    if (board.getWeapons().isEmpty()) {
      Log.critical("No weapons found");
    }
  }

  /**
   * Load ammo cards from json.
   */
  private void loadAmmoCards() {
    Gson gson = new Gson();
    try {
      String json = IOUtils.readResourceFile("ammocards.json");
      AmmoCard[] ammoCards = gson.fromJson(json, AmmoCard[].class);
      for (AmmoCard ammoCard : ammoCards) {
        board.addAmmoCard(
            new AmmoCard(ammoCard.getAmmo(AmmoColor.RED), ammoCard.getAmmo(AmmoColor.BLUE),
                ammoCard.getAmmo(AmmoColor.YELLOW),
                ammoCard.getPowerUp()));
      }
    } catch (IOException e) {
      Log.critical("No ammo cards found");
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
      player.setMaster(board.getPlayers().isEmpty());
      board.addPlayer(player);

      try {
        addPlayerObservers(player);
        if (player.getClient() != null) {
          board.addObserver(player.getClient().getBoardView(), true);
          board.addObserver(player.getClient().getPlayerDashboardsView());
          board.addObserver(player.getClient().getCharactersView());
        }
      } catch (RemoteException e) {
        Log.exception(e);
      }

      player.setStatus(PlayerStatus.WAITING);
      notifyPlayerJoin(player);

      if (board.getPlayers().size() >= ServerConfig.getInstance().getMinNumPlayers()) {
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

  private void addPlayerObservers(Player player) throws RemoteException {
    if (player.getClient() != null) {
      player.addObserver(player.getClient().getBoardView());
      player.addObserver(player.getClient().getPlayerDashboardsView());
      player.addObserver(player.getClient().getCharactersView());

      for (Player toPlayer : board.getPlayers()) {
        if (toPlayer.getColor() != player.getColor() && toPlayer.getClient() != null) {
          player.addObserver(toPlayer.getClient().getBoardView());
          player.addObserver(toPlayer.getClient().getPlayerDashboardsView());
          player.addObserver(toPlayer.getClient().getCharactersView());

          toPlayer.addObserver(player.getClient().getBoardView());
          toPlayer.addObserver(player.getClient().getPlayerDashboardsView());
          toPlayer.addObserver(player.getClient().getCharactersView());
        }
      }
    }
  }

  /**
   * Notifies clients when a player joins a game.
   * @param player the player who just joined the game
   */
  public void notifyPlayerJoin(Player player) {
    for (ClientInterface client : clientsName.keySet()) {
      try {
        if (player.getColor() != client.getPlayerColor()) {
          client.showMessage(MessageSeverity.INFO,
              String.format(
                  "%s%s%s si è unito alla partita! (%d/5)",
                  player.getColor().getAnsiColor(),
                  player.getName(),
                  ANSIColor.RESET,
                  clientsName.size())
          );
        }
        if (player.getClient() != null) {
          player.getClient().showMessage(MessageSeverity.INFO,
              String.format(
                  "%s%s%s partecipa alla partita!",
                  client.getPlayerColor().getAnsiColor(),
                  client.getName(),
                  ANSIColor.RESET)
          );
        }
      } catch (RemoteException ignored) {
        //
      }
    }
  }

  /**
   * Notifies clients when a player quits a game.
   *
   * @param player the player who just quitted the game
   */
  public void notifyPlayerQuit(Player player) {
    for (ClientInterface client : clientsName.keySet()) {
      try {
        if (player.getColor() != client.getPlayerColor()) {
          client.showMessage(MessageSeverity.INFO,
              String.format(
                  "%s%s%s ha abbandonato la partita!",
                  player.getColor().getAnsiColor(),
                  player.getName(),
                  ANSIColor.RESET)
          );
        }
      } catch (RemoteException ignored) {
        //
      }
    }
  }

  /**
   * Handle player disconnection.
   *
   * @param player the player who just disconnected.
   */
  public void handleDisconnect(PlayerColor player) {
    if (player == board.getCurrentPlayer()) {
      turnController.clearActionsQueue();
      turnController.endTurn();
    }
  }

  /**
   * Finds a free PlayerColor for this game board.
   * @return a PlayerColor or null
   */
  public PlayerColor getFreePlayerColor() {
    for (PlayerColor color : PlayerColor.values()) {
      try {
        board.getPlayerByColor(color);
      } catch (InvalidPlayerException e) {
        return color;
      }
    }
    return null;
  }

  /**
   * Starts a timer both server-side and on each client.
   */
  private void startJoinTimer() {
    timer.start(ServerConfig.getInstance().getJoinTimeout(), this::chooseMap);

    board.getPlayers().stream().forEach(p -> {
      if (p.getClient() != null) {
        try {
          boardViews.get(p.getClient()).startTimer(ServerConfig.getInstance().getJoinTimeout());
        } catch (RemoteException e) {
          Log.exception(e);
        }
      }
    });
  }

  /**
   * Verifies if a map is selected otherwise one is chosen depending on the number of
   * players.
   */
  private void chooseMap() {
    if (selectedMap == 0) {
      List<GameMap> validMaps = getValidMaps(board.getPlayers().size());
      selectedMap = validMaps.get(random.nextInt(validMaps.size())).getId();
    }

    for (GameMap map : new ArrayList<>(maps)) {
      if (map.getId() == selectedMap) {
        createSquares(map);
        placeAmmoCard();
        break;
      }
    }
    maps.clear();
    Log.info("Selected map #" + selectedMap);
    board.setMapId(selectedMap);

    run();
  }

  /**
   * Create every square for a map from the template in the GameMap.
   * @param gameMap the map template
   */
  public void createSquares(GameMap gameMap) {
    for (Square square : gameMap.getSquares()) {
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
  }

  /**
   * Places ammoCards on squares.
   */
  private void placeAmmoCard() {
    for (Square square : board.getSquares()) {
      if (!square.isSpawnPoint()) {
        AmmoCard ammoCard = board.getAmmoCards().get(0);
        board.drawAmmoCard(ammoCard);
        square.setAmmoCard(ammoCard);
      }
    }
  }

  /**
   * Removes a player from a board in LOBBY status or sets the player's status to DISCONNECTED if
   * the game on that board is already in progress.
   * @param player the player to be removed
   */
  public void removePlayer(Player player) throws InvalidPlayerException {
    clientsName.remove(player.getClient());
    if (board.getStatus() == BoardStatus.LOBBY) {
      board.removePlayer(player.getColor());
      if (! board.getPlayers().isEmpty() && player.isMaster()) {
        board.getPlayers().get(0).setMaster(true);
      }
      if (board.getPlayers().size() >= 2) {
        startJoinTimer();
      } else {
        timer.stop();
      }
    } else {
      player.setStatus(PlayerStatus.DISCONNECTED);
      player.setClient(null);
    }
  }

  /**
   * Sets needed views (BoardView, CharactersView and PlayerDashboardsView) on a Player.
   *
   * @param player a Player
   */
  private void setViews(Player player) {
    try {
      ClientInterface client = player.getClient();
      if (client != null) {
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
      }
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

  /**
   * Starts a game.
   */
  @Override
  public void run() {
    board.setCurrentPlayer(null);

    Deque<PlayerColor> freeColors = new ArrayDeque<>(board.getFreePlayerColors());
    for (Player player : board.getPlayers()) {
      if (player.getColor() == null) {
        player.setColor(freeColors.pop());
      }
    }

    turnController.prepare();
    board.setStatus(BoardStatus.MATCH);
    turnController.executeGameActionQueue();
  }

  /**
   * Handles the toggling of final frenzy.
   *
   * @param event the received event
   */
  public void update(FinalFrenzyToggleEvent event) {
    if (timer.getRemainingSeconds() > 0) {
      startJoinTimer();
    }
    board.setFinalFrenzySelected(event.isEnabled());
  }

  /**
   * Handles the selection of the number of skulls to play with.
   *
   * @param event the received event
   */
  public void update(BoardSkullsUpdate event) {
    if (timer.getRemainingSeconds() > 0) {
      startJoinTimer();
    }
    board.setSkulls(event.getSkulls());
  }

  /**
   * Handles the selection of the map that will be used.
   *
   * @param event the received event
   */
  public void update(MapSelectionEvent event) {
    if (timer.getRemainingSeconds() > 0) {
      startJoinTimer();
    }
    if (event.getMap() >= 1 && event.getMap() <= 4) {
      selectedMap = event.getMap();
      board.setMapId(selectedMap);
    }
  }

  /**
   * Handles the selection of a player's color.
   *
   * @param event the received event
   */
  public void update(PlayerColorSelectionEvent event) {
    if (timer.getRemainingSeconds() > 0) {
      startJoinTimer();
    }

    if (!board.getFreePlayerColors().contains(event.getNewPlayerColor())) {
      return;
    }

    Player player;

    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    player.setColor(event.getNewPlayerColor());

    try {
      if (player.getClient() != null) {
        player.getClient().setPlayerColor(event.getNewPlayerColor());
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }

    for (ClientInterface client : getClients().keySet()) {
      try {
        if (client.getPlayerColor() != event.getNewPlayerColor()) {
          client.showGameMessage(String.format("%s%s%s ha cambiato personaggio in %s%s%s",
                  event.getNewPlayerColor().getAnsiColor(),
                  player.getName(),
                  ANSIColor.RESET,
                  event.getNewPlayerColor().getAnsiColor(),
                  event.getNewPlayerColor().getCharacterName(),
                  ANSIColor.RESET));
        }
      } catch (RemoteException e) {
        // ignore
      }
    }
  }

  /**
   * Handles generic events.
   *
   * @param event the received event.
   */
  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("FController", "Event received: " + event.getEventType());
      try {
        Observer.invokeEventHandler(this, event);
      } catch (RemoteException e) {
        Log.exception(e);
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
