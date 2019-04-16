package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.Effect;
import org.junit.Test;

public class WeaponTest {
  @Test
  public void testCopyConstructor() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    weapon1.updateTargetHistory(new Square(0, 0, PlayerColor.YELLOW, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
    weapon1.updateTargetHistory(new Player("test", PlayerColor.YELLOW));
    weapon1.addEffect(new Effect("test", weapon1, 0, 1, 2));
    weapon1.setSelectedEffect(weapon1.getEffects().get(0));
    Weapon weapon2 = new Weapon(weapon1);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        2,
        weapon2.getCost(AmmoColor.YELLOW));
  }

  @Test
  public void testSerialization() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    // TODO: fix reconcileDeserializazion & uncomment this: weapon.addEffect(new Effect("test", weapon, 0, 1, 2));
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
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    weapon.addEffect(new Effect("test", weapon, 0, 1, 2));
    try {
      weapon.setSelectedEffect(weapon.getEffects().get(0));
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSelectedEffectException() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    Effect effect = new Effect("test", weapon, 0, 1, 2);
    weapon.setSelectedEffect(effect);
  }
}