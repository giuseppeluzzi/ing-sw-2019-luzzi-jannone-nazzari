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

  @Test(expected = InvalidParameterException.class)
  public void testExceptionAmmoCard() {
    AmmoCard ammoCard = new AmmoCard(1, 2, 3, 4);
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

  @Test
  public void testCopyConstructor() {
    AmmoCard ammoCard = new AmmoCard(1,1,1,0);
    AmmoCard ammoCard2;

    ammoCard2 = new AmmoCard(ammoCard);
    assertEquals(ammoCard.getAmmo(AmmoColor.RED), ammoCard2.getAmmo(AmmoColor.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    AmmoCard ammoCard = null;
    AmmoCard ammoCard2 = new AmmoCard(ammoCard);
  }
}
