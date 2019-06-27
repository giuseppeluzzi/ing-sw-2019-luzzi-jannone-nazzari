package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import org.junit.Before;
import org.junit.Test;

public class TargetingScopeTest {
  private TargetingScope targetingScope;

  @Before
  public void setTargetingScope() {
    targetingScope = new TargetingScope(AmmoColor.BLUE);
  }

  @Test
  public void testCopy() {
    TargetingScope targetingScope2 = targetingScope.copy();
    assertEquals(targetingScope.getName(), targetingScope2.getName());
    assertEquals(targetingScope.getSymbol(), targetingScope2.getSymbol());
    assertEquals(targetingScope.getColor(), targetingScope2.getColor());
  }

  @Test
  public void testGetCost() {
    targetingScope = new TargetingScope(AmmoColor.BLUE);
    assertEquals(0, targetingScope.getCost(AmmoColor.RED));
    assertEquals(1, targetingScope.getCost(AmmoColor.ANY));
  }

  @Test
  public void testGetOwner() {
    Player player = new Player("test", PlayerColor.GREY, null);
    targetingScope.setTargetHistory(0, player);
    assertEquals(player, targetingScope.getOwner());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOwnerException() {
    targetingScope.getOwner();
  }

  @Test(expected = IllegalStateException.class)
  public void testAfterPaymentException() {
    TargetingScope targetingScope = new TargetingScope(AmmoColor.RED);
    if (targetingScope.isPowerUp() && targetingScope.doesCost() && !targetingScope.isWeapon()) {
      targetingScope.afterPaymentCompleted(null, null, null);
    }
  }
}