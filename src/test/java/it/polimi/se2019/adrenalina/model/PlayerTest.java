package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class PlayerTest {
  @Test
  public void testAddDamage() {
    Player player = new Player("test", PlayerColor.YELLOW);
    player.addDamages(PlayerColor.BLUE, 2);
    player.addDamages(PlayerColor.YELLOW, 1);
    List<PlayerColor> damages = new ArrayList<>();
    damages.add(PlayerColor.BLUE);
    damages.add(PlayerColor.BLUE);
    damages.add(PlayerColor.YELLOW);
    assertEquals("Damages not matching",
        damages,
        player.getDamages());
  }

  @Test
  public void testAddTag() {
    Player player = new Player("test", PlayerColor.YELLOW);
    player.addTags(PlayerColor.BLUE, 2);
    player.addTags(PlayerColor.YELLOW, 1);
    List<PlayerColor> tags = new ArrayList<>();
    tags.add(PlayerColor.BLUE);
    tags.add(PlayerColor.BLUE);
    tags.add(PlayerColor.YELLOW);
    assertEquals("Tags not matching",
        tags,
        player.getTags());
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
    player.addDamages(PlayerColor.BLUE, 2);
    player.addDamages(PlayerColor.YELLOW, 1);
    player.addTags(PlayerColor.BLUE, 2);
    player.addTags(PlayerColor.YELLOW, 1);
    json = player.serialize();

    if (json.isEmpty()){
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

    List<PlayerColor> damages = new ArrayList<>();
    damages.add(PlayerColor.BLUE);
    damages.add(PlayerColor.BLUE);
    damages.add(PlayerColor.YELLOW);

    assertEquals("Deserialized damages not matching with actual damages",
        damages,
        player.getDamages());

    List<PlayerColor> tags = new ArrayList<>();
    tags.add(PlayerColor.BLUE);
    tags.add(PlayerColor.BLUE);
    tags.add(PlayerColor.YELLOW);
    assertEquals("Deserialized tags not matching with actual damages",
        tags,
        player.getDamages());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Player.deserialize(null);
  }

  @Test
  public void testCopyConstructor() {
    Player player = new Player("test", PlayerColor.GREEN);
    player.setSquare(new Square(1, 2, SquareColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
    player.addPowerUp(new Newton(AmmoColor.YELLOW));
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test1");
    weapon1.setLoaded(false);
    player.addWeapon(weapon1);
    player.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test2"));
    Player player2 = new Player(player, false);
    Player player3 = new Player(player, true);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        player.getName(),
        player2.getName());

    assertTrue(
        "Non-public copy of Player contains private attributes",
        player3.getPowerUps().isEmpty()
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Player player = null;
    Player player2 = new Player(player, true);
  }

  @Test
  public void testReload() {
    Player player = new Player("test", PlayerColor.GREEN);
    Weapon weapon = new Weapon(0,0,1,AmmoColor.BLUE, "test");
    player.setAmmo(AmmoColor.BLUE, 1);
    player.setAmmo(AmmoColor.YELLOW, 1);
    assertTrue(player.canReload(weapon));
  }
}