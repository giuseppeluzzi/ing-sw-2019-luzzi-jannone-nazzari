package it.polimi.se2019.adrenalina.controller;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BoardControllerTest {

  private BoardController boardController;
  private TurnController turnController;
  private GameMap testMap;
  private Player player1;
  private Player player2;
  private Player player3;
  private Player player4;


  @Before
  public void setBoardController() {
    try {
      boardController = new BoardController(false);
      boardController.setAttackController(new AttackController(boardController));
      turnController = spy(new TurnController(boardController));
      doNothing().when(turnController).executeGameActionQueue();
      boardController.setTurnController(turnController);
    } catch (RemoteException ignore) {
      //
    }
    player1 = spy(new Player("P1", PlayerColor.GREEN, boardController.getBoard()));
    doNothing().when(player1).addObserver((Observer) notNull());
    player2 = spy(new Player("P2", PlayerColor.GREY, boardController.getBoard()));
    doNothing().when(player2).addObserver((Observer) notNull());
    player3 = new Player("P3", PlayerColor.BLUE, boardController.getBoard());
    player4 = new Player("P4", PlayerColor.YELLOW, boardController.getBoard());
    testMap = new GameMap(5, "test", "testMap", 3, 4);
  }

  @Test(expected = EndedGameException.class)
  public void testAddPlayerEndGame() throws EndedGameException {
    boardController.getBoard().setStatus(BoardStatus.END);
    try {
      boardController.addPlayer(new Player("test", PlayerColor.GREEN, null));
    } catch (FullBoardException | PlayingBoardException e) {
      fail("Wrong exception");
    }
  }

  @Test(expected = FullBoardException.class)
  public void testAddPlayerFullBoard() throws FullBoardException {
    boardController.getBoard().addPlayer(player1);
    boardController.getBoard().addPlayer(player2);
    boardController.getBoard().addPlayer(player3);
    boardController.getBoard().addPlayer(player4);
    boardController.getBoard().addPlayer(new Player("P5", PlayerColor.PURPLE, null));
    try {
      boardController.addPlayer(new Player("P6", PlayerColor.GREEN, null));
    } catch (PlayingBoardException | EndedGameException ignore) {
      fail("Wrong exception");
    }


  }

  @Test
  public void testGetMapByPlayerNumber() {
    try {
      boardController.addPlayer(player1);
      boardController.addPlayer(player2);
      boardController.addPlayer(player3);
      List<GameMap> validMaps = boardController.getValidMaps(3);
      assertEquals(3, validMaps.size());
    } catch (Exception ignored) {
      //
    }
  }

  @Test
  public void testLoadMaps() {
    try {
      List<GameMap> validMaps = boardController.getValidMaps(3);
      int spawnPoints = 0;
      GameMap map = validMaps.get(0);
      assertEquals(testMap.getMinPlayers(), map.getMinPlayers());
      assertEquals(testMap.getMaxPlayers(), map.getMaxPlayers());
      assertNotEquals(testMap.getDescription(), map.getDescription());
      assertNotEquals(testMap.getName(), map.getName());
      assertNotEquals(map.getId(), testMap.getId());
      boardController.createSquares(map);
      for (Square square : boardController.getBoard().getSquares()) {
        if (square.isSpawnPoint()) {
          spawnPoints++;
        }
      }
      assertEquals(3, spawnPoints);
    } catch (Exception ignored) {
      //
    }
  }

  @Test
  public void testGetFreePlayerColor() {
    try {
      boardController.addPlayer(new Player("test", PlayerColor.GREEN, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.GREY, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.PURPLE, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.BLUE, boardController.getBoard()));
      assertEquals(PlayerColor.YELLOW, boardController.getFreePlayerColor());
    } catch (FullBoardException | EndedGameException | PlayingBoardException ignore) {
      //
    }
  }

  @Test
  public void testHandleDisconnectCurrentPlayer() {
    boardController.getBoard()
        .addPlayer(new Player("test", PlayerColor.GREEN, boardController.getBoard()));
    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    boardController.handleDisconnect(PlayerColor.GREEN);
    assertEquals(0, boardController.getTurnController().getEndGameReason());
  }

  @Test
  public void testRemovePlayer() {
    try {
      boardController.addPlayer(player1);
    } catch (FullBoardException | PlayingBoardException | EndedGameException ignore) {
      //
    }

    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    boardController.getBoard().setStatus(BoardStatus.MATCH);
    try {
      boardController.removePlayer(player1);
    } catch (InvalidPlayerException ignore) {
      //
    }

    assertEquals(PlayerStatus.DISCONNECTED, player1.getStatus());
  }


  @Test(expected = PlayingBoardException.class)
  public void testAddPlayerMatchException() throws PlayingBoardException {
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    boardController.getBoard().setStatus(BoardStatus.MATCH);
    try {
      boardController.addPlayer(player);
    } catch (FullBoardException | EndedGameException ignore) {
      //
    }
  }


  @Test
  public void testFinalFrenzyToggleEvent() {
    FinalFrenzyToggleEvent event = new FinalFrenzyToggleEvent(true);
    boardController.update(event);
    assertTrue(boardController.getBoard().isFinalFrenzySelected());
  }

  @Test
  public void testBoardSkullEvent() {
    BoardSkullsUpdate event = new BoardSkullsUpdate(3);
    boardController.update(event);
    assertEquals(3, boardController.getBoard().getSkulls());
  }

  @Test
  public void testMapSelectionEvent() {
    MapSelectionEvent event = new MapSelectionEvent(2);
    boardController.update(event);
    assertEquals(2, boardController.getBoard().getMapId());
  }

  @Test
  public void testPlayerColorSelectionEvent() {
    player1.setClient(new FakeClient(player1.getName(), false, false, player1.getColor()));
    player2.setClient(new FakeClient(player2.getName(), false, false, player2.getColor()));
    boardController.addClient(player1.getClient());
    boardController.addClient(player2.getClient());
    PlayerColorSelectionEvent event = new PlayerColorSelectionEvent(PlayerColor.GREEN,
        PlayerColor.GREY);
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    boardController.getBoard().addPlayer(player);
    boardController.update(event);
    assertEquals(PlayerColor.GREY, player.getColor());
  }

  @Test
  public void testGenericEvent() {
    Event event = new MapSelectionEvent(2);
    boardController.update(event);
    assertEquals(2, boardController.getBoard().getMapId());
  }

  @Test
  public void testAddPlayer() {
    player1.setClient(new FakeClient(player1.getName(), false, false, player1.getColor()));
    player2.setClient(new FakeClient(player2.getName(), false, false, player2.getColor()));
    try {
      boardController.addPlayer(player1);
      boardController.addPlayer(player2);
    } catch (Exception ignore) {
      //
    }

    boardController.addClient(player1.getClient());
    boardController.addClient(player2.getClient());

    boardController.notifyPlayerJoin(player2);
    assertTrue(((FakeClient) player2.getClient()).isReceived());
    boardController.notifyPlayerQuit(player2);
    assertTrue(boardController.containsClient(player1.getClient()));
    try {
      assertEquals(player1, boardController.getPlayerByClient(player1.getClient()));
    } catch (InvalidPlayerException ignore) {
      //
    }
  }

  @Test
  public void testRun() {
    doNothing().when(turnController).prepare();
    try {
      boardController.addPlayer(player3);
    } catch (Exception ignore) {
      //
    }
    player3.setColor(null);
    boardController.run();
    assertEquals(BoardStatus.MATCH, boardController.getBoard().getStatus());
    assertNotNull(player3.getColor());
  }
  
  @Test
  public void testAddPlayerSuspended() throws RemoteException {
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    FakeClient fakeClient = spy(new FakeClient("test", false, false, PlayerColor.GREEN));

    fakeClient.setCharactersView(new FakeCharacterViewInterface());
    fakeClient.setBoardView(new FakeBoardViewInterface());
    fakeClient.setPlayerDashboardsView(new FakePlayerDashboardViewInterface());
    player.setSquare(new Square(2, 2, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, null));
    player.setClient(fakeClient);


    boardController.getBoard().addPlayer(player);
    player.setStatus(PlayerStatus.SUSPENDED);
    boardController.getBoard().setStatus(BoardStatus.MATCH);
    try {
      boardController.addPlayer(player);
    } catch (FullBoardException | PlayingBoardException | EndedGameException ignore) {
      //
    }

    assertEquals(PlayerStatus.PLAYING, player.getStatus());
  }

  private class FakeClient extends Client {

    private boolean received;
    private final PlayerColor color;

    protected FakeClient(String playerName, boolean domination, boolean tui, PlayerColor color) {
      super(playerName, domination, tui);
      received = false;
      this.color = color;
    }

    public boolean isReceived() {
      return received;
    }

    public void setReceived(boolean received) {
      this.received = received;
    }

    @Override
    public void showMessage(MessageSeverity messageSeverity, String message) {
      received = true;
    }

    @Override
    public void showGameMessage(String message) {
      received = true;
    }

    @Override
    public PlayerColor getPlayerColor() {
      return color;
    }

    @Override
    public BoardViewInterface getBoardView() {
      return new FakeBoardViewInterface();
    }

    @Override
    public CharactersViewInterface getCharactersView() {
      return new FakeCharacterViewInterface();
    }

    @Override
    public PlayerDashboardsViewInterface getPlayerDashboardsView() {
      return new FakePlayerDashboardViewInterface();
    }
  }


  private class FakeBoardViewInterface implements BoardViewInterface {

    private static final long serialVersionUID = -4852438859227845373L;

    @Override
    public Board getBoard() throws RemoteException {
      return null;
    }

    @Override
    public void sendEvent(Event event) throws RemoteException {

    }

    @Override
    public void setBoard(Board board) throws RemoteException {

    }

    @Override
    public void startTimer(int time) throws RemoteException {

    }

    @Override
    public void hideTimer() throws RemoteException {

    }

    @Override
    public Timer getTimer() throws RemoteException {
      return null;
    }

    @Override
    public void endLoading(boolean masterPlayer) throws RemoteException {

    }

    @Override
    public void showBoard() throws RemoteException {

    }

    @Override
    public void showTargetSelect(TargetType type, List<Target> targets, boolean skippable)
        throws RemoteException {

    }

    @Override
    public void showDirectionSelect() throws RemoteException {

    }

    @Override
    public void showSquareSelect(List<Target> targets) throws RemoteException {

    }

    @Override
    public void showBuyableWeapons(List<Weapon> weapons) throws RemoteException {

    }

    @Override
    public void showSpawnPointTrackSelection(Map<AmmoColor, Integer> damages)
        throws RemoteException {

    }

    @Override
    public void showFinalRanks() throws RemoteException {

    }

    @Override
    public void showDisconnectWarning() throws RemoteException {

    }

    @Override
    public void cancelInput() throws RemoteException {

    }

    @Override
    public void update(Event event) throws RemoteException {

    }

    @Override
    public void addObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void removeObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void setObservers(List<Observer> observers) throws RemoteException {

    }

    @Override
    public List<Observer> getObservers() throws RemoteException {
      return null;
    }
  }

  private class FakeCharacterViewInterface implements CharactersViewInterface {


    private static final long serialVersionUID = 4106826435258930136L;

    @Override
    public void showDeath(PlayerColor playerColor) throws RemoteException {

    }

    @Override
    public void update(Event event) throws RemoteException {

    }

    @Override
    public void addObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void removeObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void setObservers(List<Observer> observers) throws RemoteException {

    }

    @Override
    public List<Observer> getObservers() throws RemoteException {
      return null;
    }
  }
  private class FakePlayerDashboardViewInterface implements PlayerDashboardsViewInterface {

    private static final long serialVersionUID = 6757020193197735775L;

    @Override
    public void switchToFinalFrenzy(PlayerColor playerColor) throws RemoteException {

    }

    @Override
    public void showPaymentOption(BuyableType buyableType, String prompt,
        Map<AmmoColor, Integer> buyableCost, List<PowerUp> budgetPowerUp,
        Map<AmmoColor, Integer> budgetAmmo) throws RemoteException {

    }

    @Override
    public void showTurnActionSelection(List<TurnAction> actions) throws RemoteException {

    }

    @Override
    public void showWeaponSelection(List<Weapon> weapons) throws RemoteException {

    }

    @Override
    public void showEffectSelection(Weapon weapon, List<Effect> effects) throws RemoteException {

    }

    @Override
    public void showPowerUpSelection(String targetName, List<PowerUp> powerUps, boolean discard)
        throws RemoteException {

    }

    @Override
    public void showSwapWeaponSelection(List<Weapon> ownWeapons, List<Weapon> squareWeapons)
        throws RemoteException {

    }

    @Override
    public void showReloadWeaponSelection(List<Weapon> unloadedWeapons) throws RemoteException {

    }

    @Override
    public void showUnsuspendPrompt() throws RemoteException {

    }

    @Override
    public void update(Event event) throws RemoteException {

    }

    @Override
    public void addObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void removeObserver(Observer observer) throws RemoteException {

    }

    @Override
    public void setObservers(List<Observer> observers) throws RemoteException {

    }

    @Override
    public List<Observer> getObservers() throws RemoteException {
      return null;
    }
  }
  private class FakeObserver implements Observer {

    @Override
    public void update(Event event) {
      // do nothing
    }
  }
}


