package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidWeaponException;
import it.polimi.se2019.adrenalina.utils.Log;
import org.junit.Assert;
import org.junit.Test;

public class BoardTest {
  @Test
  public void testCopyConstructor() {
    Board board = new Board();
    Board board2;
    Board board3;
    Player player = new Player("test", PlayerColor.GREEN);

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        board.setSquare(x, y, new Square(1,2,
            SquareColor.GREEN, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
      }
    }
    board.addPlayer(player);
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test1"));
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test2"));
    board.addPowerUp(new Newton(AmmoColor.YELLOW));
    board.addPowerUp(new Newton(AmmoColor.BLUE));
    try {
      board.takeWeapon(board.getWeapons().get(0));
    } catch (InvalidWeaponException e) {
      Log.debug("No Weapons in board object");
    }
    board.usePowerUp(board.getPowerUps().get(0));
    board.addKillShot(new Kill(PlayerColor.YELLOW, true));
    board.addDoubleKill(player);
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
    Board board = null;
    Board board2 = new Board(board, false);
  }

  @Test
  public void testSetSquare() {
    try {
      Board board = new Board();
      Square square = new Square(1, 1, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR);
      board.setSquare(1, 1, square);
      board.setSquare(2, 1, new Square(2, 1, SquareColor.YELLOW, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
      board.setSquare(1, 2, new Square(1, 2, SquareColor.BLUE, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
      board.setSquare(0, 1, new Square(0, 1, SquareColor.PURPLE, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
      board.setSquare(1, 0, new Square(1, 0, SquareColor.GREY, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));

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

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException1() {
    Board board = new Board();
    board.setSquare(3, 3, new Square(0, 2, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException2() {
    Board board = new Board();
    board.setSquare(-1, -1, new Square(0, 2, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException3() {
    Board board = new Board();
    board.setSquare(0, 4, new Square(0, 2, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException4() {
    Board board = new Board();
    board.setSquare(0, -1, new Square(0, 2, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException5() {
    Board board = new Board();
    board.setSquare(0, 0, null);
  }

  @Test
  public void testGetSquare() {
    try {
      Board board = new Board();
      board.getSquare(0, 2);
      board.getSquare(2, 0);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetSquareException1() {
    Board board = new Board();
    board.getSquare(3, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetSquareException2() {
    Board board = new Board();
    board.getSquare(-1, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetSquareException3() {
    Board board = new Board();
    board.getSquare(0, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetSquareException4() {
    Board board = new Board();
    board.getSquare(0, -1);
  }

  @Test
  public void testUseWeapon() {
    Board board = new Board();
    Weapon weapon = new Weapon(1, 0, 0, AmmoColor.YELLOW, "test");
    board.addWeapon(weapon);
    try {
      board.takeWeapon(board.getWeapons().get(0));
    } catch (InvalidWeaponException e) {
      fail("InvalidWeaponException thrown unnecessarily");
    }
  }

  @Test
  public void testUseWeaponException() {
    Board board = new Board();
    Weapon weapon1 = new Weapon(1, 0, 0, AmmoColor.YELLOW, "test");
    Weapon weapon2 = new Weapon(1, 0, 0, AmmoColor.BLUE, "test2");
    board.addWeapon(weapon1);
    try {
      board.takeWeapon(weapon2);
      fail("Exception not handled correctly");
    } catch (InvalidWeaponException e) {
      Log.debug("No weapon in board object");
    }
  }

  @Test
  public void testUsePowerUp() {
    Board board = new Board();
    Newton powerUp = new Newton(AmmoColor.YELLOW);
    board.addPowerUp(powerUp);
    try {
      board.usePowerUp(board.getPowerUps().get(0));
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testUsePowerUpException() {
    Board board = new Board();
    Newton powerUp1 = new Newton(AmmoColor.YELLOW);
    Newton powerUp2 = new Newton(AmmoColor.YELLOW);
    board.addPowerUp(powerUp1);
    board.usePowerUp(powerUp2);
  }

  @Test
  public void testGetPlayerByColor() {
    Board board = new Board();
    Player player1 = new Player("test1", PlayerColor.YELLOW);
    Player player2 = new Player("test2", PlayerColor.BLUE);
    board.addPlayer(player1);
    board.addPlayer(player2);
    assertEquals(
        "Returned wrong player",
        player1,
        board.getPlayerByColor(PlayerColor.YELLOW)
    );
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayerByColorException() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.YELLOW);
    board.addPlayer(player);
    board.getPlayerByColor(PlayerColor.BLUE);
  }

  @Test
  public void testSerialization() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.YELLOW);
    board.addPlayer(player);
    board.setSquare(1, 1, new Square(1, 1, SquareColor.RED, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
    board.setSquare(2, 1, new Square(2, 1, SquareColor.YELLOW, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
    board.setSquare(1, 2, new Square(1, 2, SquareColor.BLUE, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
    board.setSquare(0, 1, new Square(0, 1, SquareColor.PURPLE, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
    board.setSquare(1, 0, new Square(1, 0, SquareColor.GREY, BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR));
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

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Board.deserialize(null);
  }
}