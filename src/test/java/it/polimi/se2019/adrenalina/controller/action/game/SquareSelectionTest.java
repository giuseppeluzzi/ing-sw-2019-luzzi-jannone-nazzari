package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import org.junit.Test;

public class SquareSelectionTest {

  @Test
  public void testGetTargets() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Square square = new Square(0,0, SquareColor.RED, BorderType.WALL,BorderType.WALL,BorderType.WALL,BorderType.WALL,board);
    board.setSquare(square);
    square.setAmmoCard(new AmmoCard(2,1,0,0));
    player.setSquare(square);
    SquareSelection squareSelection = new SquareSelection(player, 0, true);
    if (squareSelection.isFetch()) {
      assertEquals(1, squareSelection.getTargets().size());
    }
  }
}