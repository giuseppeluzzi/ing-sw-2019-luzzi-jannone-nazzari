package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.action.weapon.MoveAction;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Before;
import org.junit.Test;

public class EffectTest {

  private Weapon weapon;
  private Effect effect;
  private Effect subEffect;

  @Before
  public void set() {
    weapon = new Weapon(0,0,0,AmmoColor.BLUE,"test", "f");
    effect = new Effect("effectTest", weapon, 0,0,0,false);
    effect.addAction(new MoveAction(0,1));
    subEffect = new Effect("subEffectTest", weapon, 0,0,0,false);
    effect.addSubEffect(subEffect);
    weapon.addEffect(effect);
  }

  @Test
  public void testCopyEffect() {
    Effect test = new Effect(effect, false);
    Effect test2 = new Effect(effect, true);
    assertEquals(effect.getName(), test.getName());
    assertEquals(effect.getWeapon(), subEffect.getRequiredEffect().getWeapon());
    assertEquals(effect.getCostRed(), subEffect.getRequiredEffect().getCostRed());
    assertEquals(effect.getCostBlue(), test.getCostBlue());
    assertEquals(effect.getCostYellow(), test.getCostYellow());
    assertFalse(test.isAnyTime());
    assertEquals(subEffect.getCost(AmmoColor.RED), effect.getSubEffects().get(0).getCost(AmmoColor.RED));
    assertEquals(BuyableType.EFFECT, test.getBuyableType());
    assertEquals(subEffect.getCost(AmmoColor.BLUE), effect.getSubEffects().get(0).getCost(AmmoColor.BLUE));
    assertEquals(subEffect.getCost(AmmoColor.YELLOW), effect.getSubEffects().get(0).getCost(AmmoColor.YELLOW));
    assertEquals(0, effect.getSubEffects().get(0).getCost(AmmoColor.ANY));
    assertEquals(test.getName(), test.promptMessage());
    test.setIndexConfirmed(true);
    assertEquals(test, test.getBaseBuyable());
    assertEquals(0, test.hashCode());
    assertTrue(test.isIndexConfirmed());
    assertEquals(1, test.getActions().size());
    assertEquals(0, test2.getActions().size());
  }
}