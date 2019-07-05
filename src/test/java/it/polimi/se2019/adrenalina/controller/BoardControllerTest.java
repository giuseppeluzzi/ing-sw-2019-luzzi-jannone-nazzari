package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import java.rmi.RemoteException;
import java.util.List;

import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.view.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

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
    testMap = new GameMap(5, "test", "testMap", 3,4);
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
    boardController.getBoard().addPlayer(new Player("test", PlayerColor.GREEN, boardController.getBoard()));
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

  @Ignore //TODO
  @Test
  public void testAddPlayerSuspended() {
    Player player = spy(new Player("test", PlayerColor.GREEN, boardController.getBoard()));
    FakeClient client = spy(new FakeClient(player.getName(), false, false, player.getColor()));



    player.setClient(client);
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
    PlayerColorSelectionEvent event = new PlayerColorSelectionEvent(PlayerColor.GREEN, PlayerColor.GREY);
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

  private class FakeClient extends Client {
    private boolean received;
    private PlayerColor color;

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

    public void showMessage(MessageSeverity messageSeverity, String message) {
      received = true;
    }

    public void showGameMessage(String message) {
      received = true;
    }

    public PlayerColor getPlayerColor() {
      return color;
    }

  }
}


