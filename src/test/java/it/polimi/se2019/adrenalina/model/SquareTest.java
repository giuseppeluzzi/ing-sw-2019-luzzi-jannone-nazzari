package it.polimi.se2019.adrenalina.model;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
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
    base = new Square(2, 2, SquareColor.RED, WALL, WALL, WALL, WALL);
    north = new Square(2, 3, SquareColor.RED, WALL, WALL, WALL, WALL);
    south = new Square(2, 1, SquareColor.RED, WALL, WALL, WALL, WALL);
    west = new Square(1, 2, SquareColor.RED, WALL, WALL, WALL, WALL);
    east = new Square(3, 2, SquareColor.RED, WALL, WALL, WALL, WALL);
  }

  @Test(expected = IllegalStateException.class)
  public void testAddAmmoException() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    Weapon weapon = new Weapon(1,2,3, AmmoColor.RED, "test");
    square.setSpawnPoint(false);
    square.addWeapon(weapon);
  }

  @Test
  public void testSerialization() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
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
        WALL, WALL, WALL);
    Square square2 = new Square(square);
    square.setSpawnPoint(true);
    if (square.isSpawnPoint()) {
      Weapon weapon = new Weapon(1, 1, 1, AmmoColor.BLUE, "test");
      square.addWeapon(weapon);
    }
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
    // TODO
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
    Square invalid = new Square(1, 1, SquareColor.RED, WALL, WALL, WALL, WALL);
    base.getCardinalDirection(invalid);
  }

  @Test
  public void testGetDistance() {
    assertEquals(1, base.getDistance(north));
    assertEquals(2, south.getDistance(north));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionNeighbour()  {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    square.setNeighbour(Direction.NORTH, null);
  }

  @Test
  public void testGetPlayer() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    Square square2 = new Square(2,0, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    Player player = new Player("test", PlayerColor.GREEN);
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
        WALL, WALL, WALL);
    Player player = new Player("test", PlayerColor.GREEN);
    Player player2 = new Player("test2", PlayerColor.GREEN);
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
        WALL, WALL, WALL);
    Player player = new Player("test", PlayerColor.GREEN);
    Player player2 = new Player("test2", PlayerColor.GREEN);
    square.addPlayer(player);
    square.addPlayer(player2);

    assertEquals(square.getPlayers().get(0).getName(), player.getName());
    square.removePlayer(player);
    assertEquals(square.getPlayers().get(0).getName(), player2.getName());
  }

  @Test
  public void testEquals() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    Square square2 = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL);
    if (!square.equals(square2)) {
      fail("equals not working correctly");
    }

  }
}