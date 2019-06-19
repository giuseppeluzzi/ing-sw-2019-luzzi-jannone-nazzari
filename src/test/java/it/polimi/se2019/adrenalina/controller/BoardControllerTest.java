package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;
import org.junit.Test;

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
}
