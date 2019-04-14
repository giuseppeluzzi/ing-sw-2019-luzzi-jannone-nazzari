package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

  @Test
  public void testDamagesException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    try {
      for (int i = 0; i < 13; i++) {
        player.addDamage(PlayerColor.BLUE);
      }
      fail();
    } catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testPowerupException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    try {
      for (int i = 0; i < 4; i++) {
        player.addPowerUp(new Newton(AmmoColor.RED));
      }
      fail();
    } catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testWeaponException() {
    Player player = new Player("test", PlayerColor.YELLOW);

    try {
      for (int i = 0; i < 4; i++) {
        player.addWeapon(new Weapon(1,2,3,AmmoColor.BLUE,"testWeapon"));
      }
      fail();
    } catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testSerializationNested() {
    Player player = new Player("test", PlayerColor.YELLOW);
    Player player2;
    Weapon weapon = new Weapon(1,2,3,AmmoColor.BLUE, "testWeapon");
    String json;

    player.addWeapon(weapon);
    player.addDamage(PlayerColor.PURPLE);
    player.addTag(PlayerColor.GREEN);
    json = player.serialize();

    if (!json.contains("\"name\":\"test\"")){
      fail();
    }

    player2 = Player.deserialize(json);

    if (player2.getWeapons().isEmpty()) {
      fail();
    }

    assertEquals("testWeapon", player2.getWeapons().get(0).getName());
  }
}