package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import org.junit.Test;

public class SquareTest {
  @Test(expected = IllegalStateException.class)
  public void testAddAmmoException() {
    Square square = new Square(2,1, PlayerColor.GREEN, BorderType.WALL,
        BorderType.WALL, BorderType.WALL, BorderType.WALL);
    Weapon weapon = new Weapon(1,2,3, AmmoColor.RED, "test");
    square.setSpawnPoint(false);
    square.addWeapon(weapon);
  }

  @Test
  public void testSerialization() {
    Square square = new Square(2,1, PlayerColor.GREEN, BorderType.WALL,
        BorderType.WALL, BorderType.WALL, BorderType.WALL);
    String json = square.serialize();

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }

    assertEquals("Deserialized class attributes not matching with actual class attributes",
        2, Square.deserialize(json).getPosX());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Square.deserialize(null);
  }

  @Test
  public void testCopyConstructor() {
    Square square = new Square(2,1, PlayerColor.GREEN, BorderType.WALL,
        BorderType.WALL, BorderType.WALL, BorderType.WALL);
    Square square2 = new Square(square);

    assertEquals("Cloned class attributes not matching with original class attributes",
        square.getColor(), square2.getColor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Square square = null;
    Square square2 = new Square(square);
  }
}