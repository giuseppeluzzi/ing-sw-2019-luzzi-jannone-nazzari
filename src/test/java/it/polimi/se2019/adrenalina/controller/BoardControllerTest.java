package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.List;
import org.junit.Test;

public class BoardControllerTest {

  @Test
  public void testGetMapByPlayerNumber() {
    try {
      BoardController boardController = new BoardController(false);
      boardController.addPlayer(new Player("P1", PlayerColor.GREEN));
      boardController.addPlayer(new Player("P2", PlayerColor.BLUE));
      boardController.addPlayer(new Player("P3", PlayerColor.YELLOW));

      List<GameMap> validMaps = boardController.getValidMaps(3);
      assertEquals(3, validMaps.size());
    } catch (Exception ignored) {
      //
    }
  }


}
