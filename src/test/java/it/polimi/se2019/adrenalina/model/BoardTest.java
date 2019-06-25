package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import javafx.scene.layout.BorderPane;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

public class BoardTest {
  private Board board;

  @Before
  public void setBoard() {
    board = new Board();
  }

  @Test
  public void testCopyConstructor() throws InvalidPlayerException {
    Board board2;
    Board board3;
    Player player = new Player("test", PlayerColor.GREEN, board);

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        board.setSquare(new Square(x, y,
            SquareColor.GREEN, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            board));
      }
    }
    board.addPlayer(player);
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test1", "X"));
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test2", "X"));
    board.addPowerUp(new Newton(AmmoColor.YELLOW));
    board.addPowerUp(new Newton(AmmoColor.BLUE));

    board.takeWeapon(board.getWeapons().get(0));

    board.drawPowerUp(board.getPowerUps().get(0));
    board.addKillShot(new Kill(PlayerColor.YELLOW, true));
    board.setDoubleKill(player);
    board2 = new Board(board, false);
    board3 = new Board(board, true);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        "test",
        board2.getPlayerByColor(PlayerColor.GREEN).getName());

    assertTrue("Public copy has private attributes", board3.getWeapons().isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Board board2 = new Board(board, false);
  }

  @Test
  public void testSetSquare() {
    try {
      Square square = new Square(1, 1, SquareColor.RED, BorderType.AIR, BorderType.AIR,
          BorderType.AIR, BorderType.AIR, board);
      board.setSquare(square);
      board.setSquare(
          new Square(2, 1, SquareColor.YELLOW, BorderType.AIR, BorderType.AIR, BorderType.AIR,
              BorderType.AIR, board));
      board.setSquare(
          new Square(1, 2, SquareColor.BLUE, BorderType.AIR, BorderType.AIR, BorderType.AIR,
              BorderType.AIR, board));
      board.setSquare(
          new Square(0, 1, SquareColor.PURPLE, BorderType.AIR, BorderType.AIR, BorderType.AIR,
              BorderType.AIR, board));
      board.setSquare(
          new Square(1, 0, SquareColor.GREY, BorderType.AIR, BorderType.AIR, BorderType.AIR,
              BorderType.AIR, board));

      assertEquals(
          "Square navigation failed",
          SquareColor.GREY,
          square.getNeighbour(Direction.NORTH).getColor());

      assertEquals(
          "Square navigation failed",
          SquareColor.YELLOW,
          square.getNeighbour(Direction.EAST).getColor());

      assertEquals(
          "Square navigation failed",
          SquareColor.BLUE,
          square.getNeighbour(Direction.SOUTH).getColor());

      assertEquals(
          "Square navigation failed",
          SquareColor.PURPLE,
          square.getNeighbour(Direction.WEST).getColor());
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSquareException() {
    board.setSquare(null);
  }

  @Test
  public void testGetSquare() {
    try {
      board.getSquare(0, 2);
      board.getSquare(2, 0);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSquareException1() {
    board.getSquare(3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSquareException2() {
    board.getSquare(-1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSquareException3() {
    board.getSquare(0, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSquareException4() {
    board.getSquare(0, -1);
  }

  @Test
  public void testDrawPowerUp() {
    Newton powerUp = new Newton(AmmoColor.YELLOW);
    board.addPowerUp(powerUp);
    try {
      board.drawPowerUp(powerUp);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDrawPowerUpException() {
    Newton powerUp1 = new Newton(AmmoColor.YELLOW);
    Newton powerUp2 = new Newton(AmmoColor.YELLOW);
    board.addPowerUp(powerUp1);
    board.drawPowerUp(powerUp2);
  }

  @Test
  public void testGetPlayerByColor() throws InvalidPlayerException {
    Player player1 = new Player("test1", PlayerColor.YELLOW, board);
    Player player2 = new Player("test2", PlayerColor.BLUE, board);
    board.addPlayer(player1);
    board.addPlayer(player2);
    assertEquals(
        "Returned wrong player",
        player1,
        board.getPlayerByColor(PlayerColor.YELLOW)
    );
  }

  @Test(expected = InvalidPlayerException.class)
  public void testGetPlayerByColorException() throws InvalidPlayerException {
    Player player = new Player("test", PlayerColor.YELLOW, board);
    board.addPlayer(player);
    board.getPlayerByColor(PlayerColor.BLUE);
  }

  @Test
  public void testSerialization() {
    Player player = new Player("test", PlayerColor.YELLOW, board);
    board.addPlayer(player);
    board.setSquare(
        new Square(1, 1, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            BorderType.AIR, board));
    board.setSquare(
        new Square(2, 1, SquareColor.YELLOW, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            BorderType.AIR, board));
    board.setSquare(
        new Square(1, 2, SquareColor.BLUE, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            BorderType.AIR, board));
    board.setSquare(
        new Square(0, 1, SquareColor.PURPLE, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            BorderType.AIR, board));
    board.setSquare(
        new Square(1, 0, SquareColor.GREY, BorderType.AIR, BorderType.AIR, BorderType.AIR,
            BorderType.AIR, board));
    String json = board.serialize();

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }

    Board board2 = Board.deserialize(json);

    assertEquals(
        "Deserialized player attributes not matching with actual player attributes",
        "test",
        board2.getPlayers().get(0).getName());

    assertEquals(
        "Deserialized Square navigation failed",
        SquareColor.RED,
        board2.getSquare(1, 1).getColor());

    assertEquals(
        "Deserialized Square navigation failed",
        SquareColor.GREY,
        board2.getSquare(1, 1).getNeighbour(Direction.NORTH).getColor());

    assertEquals(
        "Deserialized Square navigation failed",
        SquareColor.YELLOW,
        board2.getSquare(1, 1).getNeighbour(Direction.EAST).getColor());

    assertEquals(
        "Deserialized Square navigation failed",
        SquareColor.BLUE,
        board2.getSquare(1, 1).getNeighbour(Direction.SOUTH).getColor());

    assertEquals(
        "Deserialized Square navigation failed",
        SquareColor.PURPLE,
        board2.getSquare(1, 1).getNeighbour(Direction.WEST).getColor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Board.deserialize(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSpawnPointSquare() {
    board.getSpawnPointSquare(AmmoColor.ANY);
  }

  @Test
  public void testSet() {
    Player player1 = new Player("test1", PlayerColor.GREEN, board);
    Player player2 = new Player("test2", PlayerColor.GREY, board);
    Player player3 = new Player("test3", PlayerColor.YELLOW, board);
    Player player4 = new Player("test4", PlayerColor.PURPLE, board);
    board.setMapId(1);
    board.setCurrentPlayer(PlayerColor.GREY);
    board.setFinalFrenzyActivator(PlayerColor.GREEN);
    player1.setStatus(PlayerStatus.DISCONNECTED);
    player2.setStatus(PlayerStatus.SUSPENDED);
    player3.setStatus(PlayerStatus.WAITING);
    player4.setStatus(PlayerStatus.PLAYING);
    board.addPlayer(player1);
    board.addPlayer(player2);
    board.addPlayer(player3);
    board.addPlayer(player4);
    assertEquals(2, board.getActivePlayers().size());
    assertEquals(1, board.getPlayingPlayers().size());
    assertEquals(PlayerColor.GREEN, board.getFinalFrenzyActivator());
    assertEquals(PlayerColor.GREY, board.getCurrentPlayer());
    assertEquals(1, board.getMapId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTakeWeaponsException() {
    board.takeWeapon(new Weapon(0,0,0,AmmoColor.BLUE,"test","p"));
  }
}