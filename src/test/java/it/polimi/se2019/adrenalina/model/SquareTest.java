package it.polimi.se2019.adrenalina.model;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.GameMap;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.exceptions.InvalidWeaponException;
import java.rmi.RemoteException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SquareTest {

  Square base;
  Square north;
  Square south;
  Square west;
  Square east;

  @Before
  public void initialize() {
    base = new Square(2, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    north = new Square(2, 3, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    south = new Square(2, 1, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    west = new Square(1, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    east = new Square(3, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testAddAmmoException() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Weapon weapon = new Weapon(1,2,3, AmmoColor.RED, "test", "X");
    square.setSpawnPoint(false);
    square.addWeapon(weapon);
  }

  @Test
  public void testSerialization() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    String json = square.serialize();

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }

    assertEquals("Deserialized class attributes not matching with actual class attributes",
        2, Square.deserialize(json).getPosX());

    assertEquals("Deserialized class attributes not matching with actual class attributes",
        1, Square.deserialize(json).getPosY());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Square.deserialize(null);
  }

  @Test
  public void testCopyConstructor() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    square.setSpawnPoint(true);
    if (square.isSpawnPoint()) {
      Weapon weapon = new Weapon(1, 1, 1, AmmoColor.BLUE, "test", "X");
      square.addWeapon(weapon);
    }
    Square square2 = new Square(square);
    assertEquals("Cloned class attributes not matching with original class attributes",
        square.getColor(), square2.getColor());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Square square = null;
    Square square2 = new Square(square);
  }

  @Test
  public void testIsVisible() {
    BoardController boardController = null;
    List<GameMap> mappe;
    Board board;
    Square square1;
    Square square2;
    try {
      boardController = new BoardController(false);
    } catch (RemoteException e) {
      fail("Eccezione");
    }
    mappe = boardController.getValidMaps(2);
    boardController.createSquares(mappe.get(0));
    board = boardController.getBoard();
    assertTrue(board.getSquare(0,0).isVisible(board.getSquare(0,1)));
    assertFalse(board.getSquare(0,0).isVisible(board.getSquare(3,2)));
  }

  @Test
  public void testGetCardinalDirection() {
    try {
      assertEquals(Direction.NORTH, base.getCardinalDirection(north));
      assertEquals(Direction.SOUTH, base.getCardinalDirection(south));
      assertEquals(Direction.WEST, base.getCardinalDirection(west));
      assertEquals(Direction.EAST, base.getCardinalDirection(east));
    } catch (InvalidSquareException e) {
      fail();
    }
  }

  @Test(expected = InvalidSquareException.class)
  public void testGetCardinalDirectionException() throws InvalidSquareException {
    Square invalid = new Square(1, 1, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    base.getCardinalDirection(invalid);
  }

  @Test
  public void testGetCardinalDirectionSameSquare() throws InvalidSquareException{
    Square square = new Square(2,2, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    assertNull(base.getCardinalDirection(square));
  }

  @Test
  public void testGetDistance() {
    BoardController boardController = null;
    List<GameMap> mappe;
    Board board;
    Square square1;
    Square square2;
    try {
     boardController = new BoardController(false);
    } catch (RemoteException e) {
      fail("Eccezione");
    }
    mappe = boardController.getValidMaps(2);
    boardController.createSquares(mappe.get(0));
    board = boardController.getBoard();
    assertEquals(5, board.getSquare(0,0).getDistance(board.getSquare(3,2)));
    assertNotEquals(5,board.getSquare(0, 0).getDistance(board.getSquare(3, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionNeighbour()  {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    square.setNeighbour(Direction.NORTH, null);
  }

  @Test
  public void testGetPlayer() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Square square2 = new Square(2,0, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Player player = new Player("test", PlayerColor.GREEN, null);
    square.addPlayer(player);

    try {
      assertEquals("test", square.getPlayer().getName());
    } catch (InvalidSquareException e) {
      fail("Exception not handled");
    }

    try {
      assertNull(square2.getPlayer());
    } catch (InvalidSquareException e) {
      fail("Exception not handled");
    }
  }

  @Test
  public void testExceptionGetPlayer() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Player player = new Player("test", PlayerColor.GREEN, null);
    Player player2 = new Player("test2", PlayerColor.GREEN, null);
    square.addPlayer(player);
    square.addPlayer(player2);

    try {
      square.getPlayer();
      fail("Exception not handled correctly");
    } catch (InvalidSquareException e) {

    }
  }

  @Test
  public void testPlayers() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Player player = new Player("test", PlayerColor.GREEN, null);
    Player player2 = new Player("test2", PlayerColor.GREEN, null);
    square.addPlayer(player);
    square.addPlayer(player2);

    assertEquals(square.getPlayers().get(0).getName(), player.getName());
    square.removePlayer(player);
    assertEquals(square.getPlayers().get(0).getName(), player2.getName());
  }

  @Test
  public void testEquals() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Square square2 = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    if (!square.equals(square2)) {
      fail("equals not working correctly");
    }

  }
}