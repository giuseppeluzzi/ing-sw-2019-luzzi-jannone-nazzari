package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponActionType;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.weapon.MoveAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.OptionalMoveAction;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.utils.Log;
import org.junit.Test;

public class WeaponTest {
  @Test
  public void testCopyConstructor() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X", false);
    weapon1.setTargetHistory(1, new Square(0, 0, SquareColor.YELLOW, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL, null));
    weapon1.setTargetHistory(2, new Player("test", PlayerColor.YELLOW, null));
    weapon1.setGroupMoveUsed(5);
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
    base.addAction(new SelectAction(0, 1, 0, 0, new int[]{}, new int[]{}, true, false, true, false, TargetType.ATTACK_TARGET));

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

  @Test
  public void testEffectSerialization() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test", "X");
    Effect base = new Effect("test", weapon, 0, 1, 2, false);
    base.addAction(new SelectAction(0, 1, 0, 0, new int[]{}, new int[]{}, true, false, true, false, TargetType.ATTACK_TARGET));
    base.addAction(new ShootAction(1, 2, 1));
    base.addAction(new MoveAction(2, 0));
    base.addAction(new OptionalMoveAction(2, 0, 0));

    Effect bis = new Effect("test_bis", weapon, 1, 0, 0, false);
    bis.addAction(new SelectAction(0, 1, 0, 0,  new int[]{}, new int[]{},true, false, true, false, TargetType.ATTACK_TARGET));
    bis.addAction(new ShootAction(1, 2, 1));
    bis.addAction(new MoveAction(2, 0));
    bis.addAction(new OptionalMoveAction(2, 0, 0));

    base.addSubEffect(bis);
    weapon.addEffect(base);
    String json = weapon.serialize();
    Log.println(json);

    if (json.isEmpty()) {
      fail("JSON resulting from serialization is empty");
    }

    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        2,
        Weapon.deserialize(json).getCost(AmmoColor.YELLOW));

    assertEquals("Deserialized weapon effects not matching with actual weapon effects",
        1,
        Weapon.deserialize(json).getEffects().size());

    assertEquals("Deserialized weapon effect actions not matching",
        4,
        Weapon.deserialize(json).getEffects().get(0).getActions().size());

    assertEquals("Deserialized weapon subeffects not matching with actual weapon subeffects",
        1,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().size());

    assertEquals("Deserialized weapon subeffect actions not matching",
        4,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().size());

    assertEquals("Deserialized weapon effect weaponaction not matching",
        WeaponActionType.SELECT,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(0).getActionType());
    assertEquals("Deserialized weapon effect weaponaction not matching",
        WeaponActionType.SHOOT,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(1).getActionType());
    assertEquals("Deserialized weapon effect weaponaction not matching",
        WeaponActionType.MOVE,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(2).getActionType());
    assertEquals("Deserialized weapon effect weaponaction not matching",
        WeaponActionType.OPTIONAL_MOVE,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(3).getActionType());

    assertEquals("Deserialized weapon subeffect weaponaction not matching",
        WeaponActionType.SELECT,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(0).getActionType());
    assertEquals("Deserialized weapon subeffect weaponaction not matching",
        WeaponActionType.SHOOT,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(1).getActionType());
    assertEquals("Deserialized weapon subeffect weaponaction not matching",
        WeaponActionType.MOVE,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(2).getActionType());
    assertEquals("Deserialized weapon subeffect weaponaction not matching",
        WeaponActionType.OPTIONAL_MOVE,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(3).getActionType());

    int effectIndex = 0;
    for (WeaponAction action: Weapon.deserialize(json).getEffects().get(0).getActions()) {
      assertEquals("Deserialized weaponaction not matching", base.getActions().get(effectIndex), action);
      effectIndex++;
    }

    int subIndex = 0;
    for (WeaponAction action: Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions()) {
      assertEquals("Deserialized weaponaction not matching", bis.getActions().get(subIndex), action);
      subIndex++;
    }
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


}