package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import org.junit.Test;

public class CheckRespawnTest {

  @Test
  public void testGetDeadPlayers() {
    Board board = new Board();
    Player player1 = new Player("test", PlayerColor.GREEN, board);
    Player player2 = new Player("test2", PlayerColor.GREY, board);
    board.addPlayer(player1);
    board.addPlayer(player2);
    player1.addDamages(PlayerColor.PURPLE, 12, false);
    assertEquals(1, CheckRespawn.getDeadPlayers(board).size());
  }

}