package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {
  // TODO: complete test suite
  @Test
  public void testCopyConstructor() {
    Board board = new Board();
    Board board2;
    Player player = new Player("test", PlayerColor.GREEN);

    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 4; y++) {
        board.setSquare(x,y,new Square(1,2,
            PlayerColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
      }
    }
    board.addPlayer(player);
    board2 = new Board(board, false);

    assertEquals("test", board2.getPlayerByColor(PlayerColor.GREEN).getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Board board = null;
    Board board2 = new Board(board, false);
  }
}