package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import java.util.Optional;
import javafx.application.Platform;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeaponTest {
  @Test
  public void testCopyConstructor() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X", false);
    weapon1.setTargetHistory(1, new Square(0, 0, SquareColor.YELLOW, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL, null));
    weapon1.setTargetHistory(2, new Player("test", PlayerColor.YELLOW, null));
    weapon1.addEffect(new Effect("test", weapon1, 0, 1, 2, false));
    weapon1.setSelectedEffect(weapon1.getEffects().get(0));
    weapon1.setLoaded(false);
    Weapon weapon2 = new Weapon(weapon1);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        2,
        weapon2.getCost(AmmoColor.YELLOW));
  }

  @Test
  public void testSerialization() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    Effect base = new Effect("test", weapon, 0, 1, 2, false);
    base.addAction(new SelectAction(0, 1, 0, 0, new int[]{}, new int[]{}, true, false, true, false, TargetType.ATTACK_TARGET, false));

    weapon.addEffect(base);
    String json = weapon.serialize();

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        2,
        Weapon.deserialize(json).getCost(AmmoColor.YELLOW));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Weapon.deserialize(null);
  }

  @Test
  public void testSetSelectedEffect() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    Effect effect = new Effect("test", weapon, 0, 1, 2, false);
    weapon.addEffect(effect);
    try {
      weapon.setSelectedEffect(weapon.getEffects().get(0));
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }

  }

  @Test
  public void testGetEffectByName() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    Effect effect1 = new Effect("test1", weapon, 0, 1, 2, false);
    Effect effect2 = new Effect("test2", weapon, 0, 1, 2, false);
    Effect effect3 = new Effect("test3", weapon, 0, 1, 2, false);
    effect2.addSubEffect(effect3);
    weapon.addEffect(effect1);
    weapon.addEffect(effect2);
    assertEquals("wrong effect by name", effect1, weapon.getEffectByName(effect1.getName()));
    assertEquals("wrong effect by name", effect3, weapon.getEffectByName(effect3.getName()));
    assertNull("wrong effect by name", weapon.getEffectByName("blablabla"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSelectedEffectException() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    Effect effect = new Effect("test", weapon, 0, 1, 2, false);
    weapon.setSelectedEffect(effect);
  }

  @Test (expected = IllegalStateException.class)
  public void testGetOwnerException() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    weapon.getOwner();
  }

  @Test
  public void testClearSelectedEffects() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X", false);
    weapon1.addEffect(new Effect("test", weapon1, 0, 1, 2, false));
    weapon1.setSelectedEffect(weapon1.getEffects().get(0));
    weapon1.clearSelectedEffects();
    assertEquals("selected effects not cleared", 0, weapon1.getSelectedEffects().size());
  }

  @Test
  public void testGetBuyableType() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X", false);
    assertEquals("wrong buyable type", BuyableType.WEAPON, weapon1.getBuyableType());
  }

  @Test
  public void testIsWeapon() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X", false);
    assertTrue("wrong is weapon attribute", weapon1.isWeapon());
  }

  @Test
  public void testGetCost() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.BLUE, "test", "X", false);
    assertEquals("wrong cost", 0, weapon1.getCost(AmmoColor.ANY));
    assertEquals("wrong cost", 1, weapon1.getCost(AmmoColor.BLUE));
    assertEquals("wrong cost", 1, (int) weapon1.getCost(false).get(AmmoColor.BLUE));
    assertEquals("wrong cost", 0, (int) weapon1.getCost(false).get(AmmoColor.ANY));
    assertEquals("wrong cost", 2, (int) weapon1.getCost(true).get(AmmoColor.BLUE));
    assertEquals("wrong cost", 0, (int) weapon1.getCost(true).get(AmmoColor.ANY));
  }

  @Test
  public void testReset() {
    Weapon weapon = new Weapon(1,0,0,AmmoColor.BLUE,"test","f");
    Player player = new Player("testPlayer", PlayerColor.GREY, null);
    weapon.setTargetHistory(0, player);
    if (weapon.targetHistoryContainsKey(0) && weapon.targetHistoryContainsValue(player)) {
      weapon.reset();
    }
  }

  @Test
  public void testGetOwner() {
    Weapon weapon = new Weapon(1,0,0,AmmoColor.BLUE,"test","f");
    weapon.setDidShoot();
    Player player = new Player("testPlayer", PlayerColor.GREY, null);
    if (! weapon.didShoot()) {
      weapon.setInitialPlayerPosition(player, new Square(0,0,SquareColor.GREY,BorderType.WALL,BorderType.WALL,BorderType.WALL,BorderType.WALL,null));
      if (weapon.isInitialPositionSet(player)) {
        weapon.getInitialPlayerPositions();
      }
    }
    weapon.setTargetHistory(0, player);
    assertEquals(player, weapon.getOwner());
  }

  @Test(expected = IllegalStateException.class)
  public void getOwnerException() {
    Weapon weapon = new Weapon(1,0,0,AmmoColor.BLUE,"test","f");
    weapon.getOwner();
  }

  @Test
  public void setTest() {
    Weapon weapon = new Weapon(0,0,0,AmmoColor.YELLOW, "test", "e");
    weapon.setCurrentSelectTargetSlot(0);
    weapon.setLastUsageDirection(Direction.NORTH);
    weapon.setCancelled(false);
    weapon.setSkipUntilSelect(true);
    assertEquals( 1, weapon.getCurrentSelectTargetSlot() + 1);
    assertEquals(Direction.NORTH, weapon.getLastUsageDirection());
    assertFalse(weapon.isCancelled());
    assertTrue(weapon.skipUntilSelect());
  }
}