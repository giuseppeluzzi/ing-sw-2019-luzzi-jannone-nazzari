package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Constants;
import org.junit.Test;

public class EndGameTest {

  @Test
  public void testAssignPoints() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Player player2 = new Player("blue", PlayerColor.BLUE, board);
    board.addPlayer(player);
    board.addPlayer(player2);
    player2.addDamages(PlayerColor.PURPLE, Configuration.getInstance().getDeathDamages(), false);
    EndGame endGame = new EndGame();
    endGame.execute(board);
    assertEquals(0, player2.getDamages().size());
  }
}