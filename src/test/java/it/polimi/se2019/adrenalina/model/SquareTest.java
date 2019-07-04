package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

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
    west = new Square(3, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    east = new Square(1, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
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
    Weapon weapon = new Weapon(1, 1, 1, AmmoColor.BLUE, "test", "X");
    square.addWeapon(weapon);
    Square square2 = new Square(square);
    assertEquals("Cloned class attributes not matching with original class attributes",
        square.getColor(), square2.getColor());
    assertTrue("cloned square is not spawnpoint", square2.isSpawnPoint());
    square2.removeWeapon(weapon);
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
      assertEquals(Direction.NORTH, north.getCardinalDirection(base));
      assertEquals(Direction.SOUTH, south.getCardinalDirection(base));
      assertEquals(Direction.WEST, west.getCardinalDirection(base));
      assertEquals(Direction.EAST, east.getCardinalDirection(base));
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

  @Test
  public void testIsPlayer() {
    assertFalse("Square is player", base.isPlayer());
  }

  @Test
  public void testHasAmmoCard() {
    base.setAmmoCard(new AmmoCard(1, 1, 1, 0));
    assertTrue("unexpected response from hasAmmoCard", base.hasAmmoCard());
    base.setAmmoCard(null);
    assertFalse("unexpected response from hasAmmoCard", base.hasAmmoCard());
  }

  @Test
  public void testSetAmmoCard() {
    AmmoCard ammoCard = new AmmoCard(1, 1, 1, 0);
    base.setAmmoCard(ammoCard);
    assertEquals("unexpected result after hasAmmoCard", ammoCard, base.getAmmoCard());
  }

  @Test
  public void testSetWeapons() {
    List<Weapon> weapons = new ArrayList<>();
    weapons.add(new Weapon(0, 0, 0, AmmoColor.RED, "testWeapon", "B"));
    base.setWeapons(weapons);
    assertEquals("unexpected result after setWeapons", weapons, base.getWeapons());
  }


  @Test
  public void testAddDamages() {
    DominationBoard board = new DominationBoard();
    Square square1 = new Square(2, 2, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square2 = new Square(2, 2, SquareColor.YELLOW, WALL, WALL, WALL, WALL, board);
    Square square3 = new Square(2, 2, SquareColor.BLUE, WALL, WALL, WALL, WALL, board);
    Square square4 = new Square(2, 2, SquareColor.BLUE, WALL, WALL, WALL, WALL, board);
    square1.setSpawnPoint(true);
    square2.setSpawnPoint(true);
    square3.setSpawnPoint(true);
    square4.setSpawnPoint(false);
    assertEquals("unexpected result from getSquare", square1, square1.getSquare());
    assertEquals("unexpected board", board, square1.getBoard());
    square1.addDamages(PlayerColor.BLUE, 1, false);
    square1.addTags(PlayerColor.GREEN, 1);
    assertEquals("unexpected result from getSquare", square2, square2.getSquare());
    assertEquals("unexpected board", board, square2.getBoard());
    square2.addDamages(PlayerColor.BLUE, 1, false);
    assertEquals("unexpected result from getSquare", square3, square3.getSquare());
    assertEquals("unexpected board", board, square3.getBoard());
    square3.addDamages(PlayerColor.BLUE, 1, false);
    assertEquals("unexpected result after addDamages", 1, board.getRedDamages().size());
    assertEquals("unexpected result after addDamages", PlayerColor.BLUE, board.getRedDamages().get(0));
    square4.addDamages(PlayerColor.BLUE, 1, false);
    assertEquals("unexpected result after addDamages", 1, board.getRedDamages().size());
  }


  @Test (expected = IllegalStateException.class)
  public void testAddDamagesException() {
    DominationBoard board = new DominationBoard();
    Square square1 = new Square(2, 2, SquareColor.PURPLE, WALL, WALL, WALL, WALL, board);
    square1.setSpawnPoint(true);
    assertEquals("unexpected result from getSquare", square1, square1.getSquare());
    assertEquals("unexpected board", board, square1.getBoard());
    square1.addDamages(PlayerColor.BLUE, 1, false);
    square1.addTags(PlayerColor.GREEN, 1);
  }

  @Test
  public void testGetSquaresInRage() {
    Board board = new Board();
    Square root = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square dist1 = new Square(1, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square dist2 = new Square(0, 1, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    dist2.setSpawnPoint(true);
    root.setNeighbour(Direction.EAST, dist1);
    root.setNeighbour(Direction.SOUTH, dist2);
    dist1.setNeighbour(Direction.WEST, root);
    dist2.setNeighbour(Direction.NORTH, root);
    board.setSquare(root);
    board.setSquare(dist1);
    board.setSquare(dist2);
    List<Square> testRes = root.getSquaresInRange(1, 1, true);
    assertTrue("missing square", testRes.contains(dist2));
    assertFalse("wrong square present", testRes.contains(dist1));
    List<Square> testRes2 = root.getSquaresInRange(1, 1, false);
    assertTrue("missing square", testRes2.contains(dist2));
    assertTrue("missing square", testRes2.contains(dist1));
  }
}