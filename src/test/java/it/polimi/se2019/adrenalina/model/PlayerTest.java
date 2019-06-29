package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.utils.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

public class PlayerTest {
  private Player player;
  private Player player2;
  private Player player3;
  private Player player4;
  private Board board;

  @Before
  public void setPlayer() {
    board = new Board();
    board.setSkulls(5);
    player = new Player("test", PlayerColor.GREEN, board);
    player2 = new Player("blue", PlayerColor.BLUE, board);
    player3 = new Player("grey", PlayerColor.GREY, board);
    player4 = new Player("yellow", PlayerColor.YELLOW, board);
    board.addPlayer(player);
    board.addPlayer(player2);
    board.addPlayer(player3);
    board.addPlayer(player4);
  }

  @Test
  public void testAddDamage() {
    player.addDamages(PlayerColor.BLUE, 2, false);
    player.addDamages(PlayerColor.YELLOW, 1, false);
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
    try {
      for (int i = 0; i < 3; i++) {
        player.addPowerUp(new Newton(AmmoColor.YELLOW));
      }
    } catch (InvalidPowerUpException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test
  public void testAddPowerUpException() {

    for (int i = 0; i < 3; i++) {
      try {
        player.addPowerUp(new Newton(AmmoColor.RED));
      } catch (InvalidPowerUpException e) {

      }
    }
    try {
      player.addPowerUp(new Newton(AmmoColor.RED));
      fail("Exception not caught correctly");
    } catch (InvalidPowerUpException e) {

    }
  }

  @Test
  public void testAddWeapon() {
    try {
      for (int i = 0; i < 3; i++) {
        player.addWeapon(new Weapon(1,2,3,AmmoColor.BLUE,"testWeapon", "X"));
      }
    } catch (IllegalStateException e) {
      fail("IllegalStateException thrown unnecessarily");
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testWeaponException() {

    for (int i = 0; i < 4; i++) {
      player.addWeapon(new Weapon(1,2,3,AmmoColor.BLUE,"testWeapon", "X"));
    }
  }

  @Test
  public void testSerialization() {
    Player player5;
    Weapon weapon = new Weapon(1,2,3,AmmoColor.BLUE, "testWeapon", "X");
    String json;

    player.addWeapon(weapon);
    player.addDamages(PlayerColor.BLUE, 2, false);
    player.addDamages(PlayerColor.YELLOW, 1, false);
    player.addTags(PlayerColor.BLUE, 2);
    player.addTags(PlayerColor.YELLOW, 1);
    json = player.serialize();

    if (json.isEmpty()){
      fail("Serialized JSON is not valid");
    }

    player2 = Player.deserialize(json);

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
    player.setSquare(new Square(1, 2, SquareColor.GREEN, WALL, WALL, WALL, WALL, null));
    try {
      player.addPowerUp(new Newton(AmmoColor.YELLOW));
    } catch (InvalidPowerUpException e) {

    }
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test1", "X");
    weapon1.setLoaded(false);
    player.addWeapon(weapon1);
    player.addWeapon(new Weapon(0, 1, 2, AmmoColor.YELLOW, "test2", "X"));
    Player player5 = new Player(player, false);
    Player player6 = new Player(player, true);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        player.getName(),
        player5.getName());

    assertTrue(
        "Non-public copy of Player contains private attributes",
        player6.getPowerUps().isEmpty()
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Player player = null;
    Player player5 = new Player(player, true);
  }

  @Test
  public void testReload() {
    Weapon weapon = new Weapon(0,0,1,AmmoColor.BLUE, "test", "X");
    player.addAmmo(AmmoColor.BLUE, 1);
    player.addAmmo(AmmoColor.YELLOW, 1);
    assertTrue(player.canReload(weapon));
  }

  @Test
  public void testReloadPowerUp() {
    Weapon weapon = new Weapon(1,0,1,AmmoColor.BLUE, "test", "X");
    PowerUp powerUp = new Newton(AmmoColor.RED);
    player.addAmmo(AmmoColor.BLUE, 1);
    player.addAmmo(AmmoColor.YELLOW, 1);
    try {
      player.addPowerUp(powerUp);
    } catch (InvalidPowerUpException ignored) {
      fail("Exception unexpected");
    }
    assertTrue(player.canReload(weapon));
  }


  @Test
  public void testSetSquare() {
    Square square = new Square(2,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    Square square2 = new Square(0,1, SquareColor.GREEN, WALL,
        WALL, WALL, WALL, null);
    player.resetTimeoutCount();
    player.incrementTimeoutCount();
    assertEquals(1, player.getTimeoutCount());
    player.getPlayer().setSquare(square);
    player.getPlayer().setSquare(square2);
    assertEquals(square2, player.getSquare());
  }

  @Test
  public void testAddDamages() {
    player.addTags(PlayerColor.GREY, 5);
    player.addDamages(PlayerColor.GREY, 1, false);
    player.isDead();
    assertEquals(4, player.getDamages().size());
  }

  @Test
  public void testHasWeaponReload() {
    Weapon weapon = new Weapon(0,1,0, AmmoColor.YELLOW, "test", "X");
    Weapon weapon2 = new Weapon(2,1,0, AmmoColor.RED, "test", "X");
    player.addWeapon(weapon);
    player.addAmmo(AmmoColor.BLUE, 1);
    player.addAmmo(AmmoColor.YELLOW, 1);
    player.setDeaths(2);
    player.getDeaths();
    player.getStatus();
    player.getDamages();
    player.setScore(2);
    player.getScore();
    player.getKillScore();
    player.setMaster(true);
    assertTrue(player.hasWeapon(weapon));
    assertTrue(player.canReload(weapon));
    assertFalse(player.hasWeapon(weapon2));
    assertFalse(player.canReload(weapon2));
  }

  @Test
  public void testGet() {
    Weapon weapon = new Weapon(0,1,0, AmmoColor.YELLOW, "test", "X");
    Weapon weapon2 = new Weapon(0,1,0, AmmoColor.YELLOW, "test2", "X");
    player.addWeapon(weapon2);
    player.addWeapon(weapon);
    player.setColor(PlayerColor.GREY);
    assertEquals(weapon, player.getWeaponByName("test"));
    assertNull(player.getWeaponByName("prova"));
  }

  @Test
  public void testAssignPoint() {
    player2 = new Player("blue", PlayerColor.BLUE, board);
    player3 = new Player("grey", PlayerColor.GREY, board);
    player4 = new Player("yellow", PlayerColor.YELLOW, board);
    player.setMaster(true);
    board.setDoubleKill(player2);
    player.addDamages(PlayerColor.BLUE, Constants.NORMAL_DEATH, false);
    board.setSkulls(1);
    player.addDamages(PlayerColor.GREY, 1, false);
    board.setFinalFrenzySelected(true);
    player.addDamages(PlayerColor.YELLOW, 1, false);
    assertTrue(player.isDead());
    player2.updateDamages(player.getDamages());
    player.assignPoints();
    player2.assignPoints();
  }

  @Test
  public void testGetPlayers() {
    assertEquals(4, player2.getBoard().getPlayers().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testAssignPointException() {
    player.assignPoints();
  }

  @Test
  public void testHandleLastSkull() {
    board.setFinalFrenzySelected(true);
    board.setSkulls(1);
    player.addDamages(PlayerColor.GREY, Constants.NORMAL_DEATH, false);
  }

  @Test
  public void testSet() {
    List<PlayerColor> tags = new ArrayList<>();
    Weapon weapon1 = new Weapon(0,0,0,AmmoColor.BLUE,"test", "r");
    Weapon weapon2 = new Weapon(0,0,0,AmmoColor.BLUE,"test2", "w");
    Weapon weapon3 = new Weapon(0,0,0,AmmoColor.BLUE,"test3", "q");

    List<Weapon> weapons = new ArrayList<>();
    weapons.add(weapon3);
    assertFalse(player.hasLoadedWeapons());
    player.addWeapon(weapon1);
    player.addWeapon(weapon2);
    weapon1.setLoaded(true);
    weapon2.setLoaded(false);

    assertEquals(1, player.getUnloadedWeapons().size());
    tags.add(PlayerColor.GREY);
    tags.add(PlayerColor.GREY);
    player.updateTags(tags);
    player.removeWeapon(weapon1);
    player.updateWeapons(weapons);
    assertNull(player.getPowerUp(PowerUpType.TARGETING_SCOPE, AmmoColor.RED));
    assertEquals(2, player.getTags().size());
    weapon3.setLoaded(true);
    assertTrue(player.hasLoadedWeapons());
    List<PowerUp> powerUps = new ArrayList<>();
    powerUps.add(new Teleporter(AmmoColor.BLUE));
    player.updatePowerUps(powerUps);
    assertEquals(1, player.getPowerUpCount());
    player.updateAmmo(AmmoColor.BLUE, 2);
    assertEquals(2, player.getAmmo(AmmoColor.BLUE));
  }

  @Test
  public void testRemovePowerUp() throws InvalidPowerUpException {
    Teleporter teleporter = new Teleporter(AmmoColor.RED);
    player.addPowerUp(teleporter);
    player.removePowerUp(teleporter);
    assertEquals(0, player.getPowerUpCount());
  }

  @Test(expected = InvalidPowerUpException.class)
  public void testRemovePowerUpException() throws InvalidPowerUpException {
    player.removePowerUp(new Teleporter(AmmoColor.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveWeaponException() {
    player.removeWeapon(new Weapon(0,0,0,AmmoColor.BLUE, "test", "q"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateAmmoException() {
    player.updateAmmo(AmmoColor.RED, 4);
  }

  @Test
  public void testCollect() {
    Weapon weapon = new Weapon(0,0,0,AmmoColor.BLUE,"test","t");
    Weapon weapon2 = new Weapon(3,0,0,AmmoColor.BLUE,"test2","p");
    player.addAmmo(AmmoColor.BLUE, 1);
    assertTrue(player.canCollectWeapon(weapon));
    assertFalse(player.canCollectWeapon(weapon2));

  }
}