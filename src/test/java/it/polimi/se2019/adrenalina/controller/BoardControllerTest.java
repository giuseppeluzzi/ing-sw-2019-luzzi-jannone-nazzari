package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

public class BoardControllerTest {

  private BoardController boardController;

  @Before
  public void setBoardController() {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
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
    //player.setClient(new FakeTestClient("test", false, true));
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


  private class FakeTestClient extends Client {

    private static final long serialVersionUID = 3058306177850233433L;

    protected FakeTestClient(String playerName, boolean domination, boolean tui) {
      super(playerName, domination, tui);
    }


  }
}
