package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PowerUpUsageTest {
  private PowerUp powerUp;
  private PowerUpUsage powerUpUsage;

  @Before
  public void setObject() {
    powerUp = new Newton(AmmoColor.RED);
    powerUpUsage = new PowerUpUsage(powerUp);
  }

  @Test
  public void testGet() {
    assertEquals(BuyableType.POWERUP, powerUpUsage.getBuyableType());
    assertEquals(0, powerUpUsage.getCost(AmmoColor.RED));
    assertEquals(powerUp.getCost(), powerUpUsage.getCost());
    assertEquals("", powerUpUsage.promptMessage());
  }

  @Test(expected = NullPointerException.class)
  public void testAfterPayment() {
    Player player = new Player("test", PlayerColor.GREY, null);
    try {
      player.addPowerUp(powerUp);
    } catch (InvalidPowerUpException e) {
      fail("Eccezione sbagliata");
    }
    powerUpUsage.afterPaymentCompleted(null, null, player);
  }

  @Test
  public void testTargetingScope() {
    TargetingScope targetingScope = new TargetingScope(AmmoColor.RED);
    PowerUpUsage powerUpUsage2 = new PowerUpUsage(targetingScope);
    assertEquals("il mirino", powerUpUsage2.promptMessage());
  }
}