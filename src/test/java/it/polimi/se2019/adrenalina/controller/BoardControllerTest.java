package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import java.rmi.RemoteException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardControllerTest {

  @Test
  public void testGetMapByPlayerNumber() {
    try {
      BoardController boardController = new BoardController(false);
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
      BoardController boardController = new BoardController(false);
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
      BoardController boardController = new BoardController(false);
      boardController.addPlayer(new Player("test", PlayerColor.GREEN, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.GREY, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.PURPLE, boardController.getBoard()));
      boardController.addPlayer(new Player("test", PlayerColor.BLUE, boardController.getBoard()));
      assertEquals(PlayerColor.YELLOW, boardController.getFreePlayerColor());
    } catch (RemoteException | FullBoardException | EndedGameException | PlayingBoardException | NullPointerException ignore) {
      //
    }
  }
}
