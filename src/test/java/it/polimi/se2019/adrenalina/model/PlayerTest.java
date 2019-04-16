package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {
  @Test
  public void testAddDamage() {
    Player player = new Player("test", PlayerColor.YELLOW);
    try {
      for (int i = 0; i < 12; i++) {
        player.addDamage(PlayerColor.BLUE);
      }
    } catch (IllegalStateException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testAddDamageException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    for (int i = 0; i < 13; i++) {
      player.addDamage(PlayerColor.BLUE);
    }
  }

  @Test
  public void testAddTag() {
    Player player = new Player("test", PlayerColor.YELLOW);
    try {
      for (int i = 0; i < 3; i++) {
        player.addTag(PlayerColor.BLUE);
      }
    } catch (IllegalStateException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testAddTagException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    for (int i = 0; i < 4; i++) {
      player.addTag(PlayerColor.BLUE);
    }
  }

  @Test
  public void testAddPowerUp() {
    Player player = new Player("test", PlayerColor.YELLOW);
    try {
      for (int i = 0; i < 3; i++) {
        player.addPowerUp(new Newton(AmmoColor.YELLOW));
      }
    } catch (IllegalStateException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testAddPowerUpException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    for (int i = 0; i < 4; i++) {
      player.addPowerUp(new Newton(AmmoColor.RED));
    }
  }

  @Test
  public void testAddWeapon() {
    Player player = new Player("test", PlayerColor.YELLOW);
    try {
      for (int i = 0; i < 3; i++) {
        player.addWeapon(new Weapon(1,2,3,AmmoColor.BLUE,"testWeapon"));
      }
    } catch (IllegalStateException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testWeaponException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    for (int i = 0; i < 4; i++) {
      player.addWeapon(new Weapon(1,2,3,AmmoColor.BLUE,"testWeapon"));
    }
  }

  @Test
  public void testSerialization() {
    Player player = new Player("test", PlayerColor.YELLOW);
    Player player2;
    Weapon weapon = new Weapon(1,2,3,AmmoColor.BLUE, "testWeapon");
    String json;

    player.addWeapon(weapon);
    player.addDamage(PlayerColor.PURPLE);
    player.addTag(PlayerColor.GREEN);
    json = player.serialize();

    if (!json.contains("\"name\":\"test\"")){
      // TODO: testing a JSON string for a match is ugly and dangerous since JSON allows different style variations (e.g. whitespace and newlines)
      fail("Serialized JSON is not valid");
    }

    player2 = Player.deserialize(json);

    if (player2.getWeapons().isEmpty()) {
      fail("Deserialized class attributes not matching with actual class attributes");
    }

    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        "testWeapon",
        player2.getWeapons().get(0).getName());
  }

  @Test
  public void testCopyConstructor() {
    Player player = new Player("test", PlayerColor.GREEN);
    player.setSquare(new Square(1, 2, PlayerColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
    Player player2 = new Player(player, false);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        player.getName(),
        player2.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Player player = null;
    Player player2 = new Player(player, true);
  }
}