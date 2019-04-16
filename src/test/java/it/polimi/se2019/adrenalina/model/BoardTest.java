package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {
  @Test
  public void testCopyConstructor() {
    Board board = new Board();
    Board board2;
    Board board3;
    Player player = new Player("test", PlayerColor.GREEN);

    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 4; y++) {
        board.setSquare(x, y, new Square(1,2,
            PlayerColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
      }
    }
    board.addPlayer(player);
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test1"));
    board.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test2"));
    board.addPowerUp(new Newton(AmmoColor.YELLOW));
    board.addPowerUp(new Newton(AmmoColor.BLUE));
    board.useWeapon(board.getWeapons().get(0));
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
      board.setSquare(0, 3, null);
      board.setSquare(2, 0, null);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException1() {
    Board board = new Board();
    board.setSquare(3, 3, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException2() {
    Board board = new Board();
    board.setSquare(-1, -1, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException3() {
    Board board = new Board();
    board.setSquare(0, 4, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSquareException4() {
    Board board = new Board();
    board.setSquare(0, -1, null);
  }

  @Test
  public void testGetSquare() {
    try {
      Board board = new Board();
      board.getSquare(0, 3);
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
      board.useWeapon(board.getWeapons().get(0));
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testUseWeaponException() {
    Board board = new Board();
    Weapon weapon1 = new Weapon(1, 0, 0, AmmoColor.YELLOW, "test");
    Weapon weapon2 = new Weapon(1, 0, 0, AmmoColor.BLUE, "test2");
    board.addWeapon(weapon1);
    board.useWeapon(weapon2);
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
    String json = board.serialize();

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        "test",
        Board.deserialize(json).getPlayers().get(0).getName());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Board.deserialize(null);
  }
}