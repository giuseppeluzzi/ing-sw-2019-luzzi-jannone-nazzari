package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;
import org.junit.Test;

public class AmmoCardTest {

  @Test
  public void testAmmoCard() {
    AmmoCard ammoCard = null;

    try {
      ammoCard = new AmmoCard(1,2,0,0);
    } catch (InvalidParameterException e) {
      fail();
    }
    assertEquals(2, ammoCard.getAmmo(AmmoColor.BLUE));
  }

  @Test
  public void testExceptionAmmoCard() {
    try {
      AmmoCard ammoCard = new AmmoCard(1, 2, 3, 4);
    } catch (InvalidParameterException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testSerialization() {
    AmmoCard ammoCard = new AmmoCard(0,0,2,1);
    String json = ammoCard.serialize();

    if (json.isEmpty()) {
      fail();
    }
    assertEquals(1, AmmoCard.deserialize(json).getPowerUp());
  }
}
