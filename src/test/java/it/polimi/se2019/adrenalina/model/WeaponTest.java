package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.Action;
import it.polimi.se2019.adrenalina.controller.action.ActionType;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.MoveAction;
import it.polimi.se2019.adrenalina.controller.action.OptionalMoveAction;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.SelectDirectionAction;
import it.polimi.se2019.adrenalina.controller.action.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.ShootSquareAction;
import it.polimi.se2019.adrenalina.controller.action.TargetType;
import org.junit.Test;

public class WeaponTest {
  @Test
  public void testCopyConstructor() {
    Weapon weapon1 = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    weapon1.setTargetHistory(1, new Square(0, 0, SquareColor.YELLOW, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
    weapon1.setTargetHistory(2, new Player("test", PlayerColor.YELLOW));
    weapon1.setOptMoveGroups(5);
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
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
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

  @Test
  public void testEffectSerialization() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    Effect base = new Effect("test", weapon, 0, 1, 2, false);
    base.addAction(new SelectAction(0, 1, 0, 0, new int[]{}, new int[]{}, true, false, true, false, TargetType.ATTACK_TARGET, false));
    base.addAction(new ShootAction(1, 2, 1));
    base.addAction(new MoveAction(2, 0));
    base.addAction(new OptionalMoveAction(2, 0, 0));

    Effect bis = new Effect("test_bis", weapon, 1, 0, 0, false);
    bis.addAction(new SelectAction(0, 1, 0, 0,  new int[]{}, new int[]{},true, false, true, false, TargetType.ATTACK_TARGET, false));
    bis.addAction(new ShootAction(1, 2, 1));
    bis.addAction(new MoveAction(2, 0));
    bis.addAction(new OptionalMoveAction(2, 0, 0));

    base.addSubEffect(bis);
    weapon.addEffect(base);
    String json = weapon.serialize();

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

    assertEquals("Deserialized weapon effect action not matching",
        ActionType.SELECT,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(0).getActionType());
    assertEquals("Deserialized weapon effect action not matching",
        ActionType.SHOOT,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(1).getActionType());
    assertEquals("Deserialized weapon effect action not matching",
        ActionType.MOVE,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(2).getActionType());
    assertEquals("Deserialized weapon effect action not matching",
        ActionType.OPTIONAL_MOVE,
        Weapon.deserialize(json).getEffects().get(0).getActions().get(3).getActionType());

    assertEquals("Deserialized weapon subeffect action not matching",
        ActionType.SELECT,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(0).getActionType());
    assertEquals("Deserialized weapon subeffect action not matching",
        ActionType.SHOOT,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(1).getActionType());
    assertEquals("Deserialized weapon subeffect action not matching",
        ActionType.MOVE,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(2).getActionType());
    assertEquals("Deserialized weapon subeffect action not matching",
        ActionType.OPTIONAL_MOVE,
        Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions().get(3).getActionType());

    int effectIndex = 0;
    for (Action action: Weapon.deserialize(json).getEffects().get(0).getActions()) {
      assertEquals("Deserialized action not matching", base.getActions().get(effectIndex), action);
      effectIndex++;
    }

    int subIndex = 0;
    for (Action action: Weapon.deserialize(json).getEffects().get(0).getSubEffects().get(0).getActions()) {
      assertEquals("Deserialized action not matching", bis.getActions().get(subIndex), action);
      subIndex++;
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Weapon.deserialize(null);
  }

  @Test
  public void testSetSelectedEffect() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    weapon.addEffect(new Effect("test", weapon, 0, 1, 2, false));
    try {
      weapon.setSelectedEffect(weapon.getEffects().get(0));
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown unnecessarily");
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSelectedEffectException() {
    Weapon weapon = new Weapon(0, 1, 2, AmmoColor.YELLOW, "test");
    Effect effect = new Effect("test", weapon, 0, 1, 2, false);
    weapon.setSelectedEffect(effect);
  }
}