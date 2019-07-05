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
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import java.rmi.RemoteException;
import java.util.List;
import javafx.print.PageLayout;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardControllerTest {

  private BoardController boardController;
  private GameMap testMap;

  @Before
  public void setBoardController() {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
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
    boardController.getBoard().addPlayer(new Player("P1", PlayerColor.GREEN, null));
    boardController.getBoard().addPlayer(new Player("P2", PlayerColor.GREY, null));
    boardController.getBoard().addPlayer(new Player("P3", PlayerColor.BLUE, null));
    boardController.getBoard().addPlayer(new Player("P4", PlayerColor.YELLOW, null));
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
      boardController.addPlayer(new Player("P1", PlayerColor.GREEN, null));
      boardController.addPlayer(new Player("P2", PlayerColor.BLUE, null));
      boardController.addPlayer(new Player("P3", PlayerColor.YELLOW, null));
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
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    try {
      boardController.addPlayer(player);
    } catch (FullBoardException | PlayingBoardException | EndedGameException ignore) {
      //
    }

    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    boardController.getBoard().setStatus(BoardStatus.MATCH);
    try {
      boardController.removePlayer(player);
    } catch (InvalidPlayerException ignore) {
      //
    }

    assertEquals(PlayerStatus.DISCONNECTED, player.getStatus());
  }

  @Test
  public void testAddPlayerSuspended() {
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
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
}
