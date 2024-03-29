package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AmmoCardTest {

  @Test
  public void testAmmoCard() {
    AmmoCard ammoCard = null;
    try {
      ammoCard = new AmmoCard(1,2,0,0);
    } catch (InvalidParameterException e) {
      fail("InvalidParameterException thrown unnecessarily");
    }
    assertEquals(
        "Blue ammo number not matching with constructor parameters",
        2,
        ammoCard.getAmmo(AmmoColor.BLUE));
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
      fail("JSON resulting from serialization is empty");
    }
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        1,
        AmmoCard.deserialize(json).getPowerUp());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    AmmoCard.deserialize(null);
  }

  @Test
  public void testCopyConstructor() {
    AmmoCard ammoCard = new AmmoCard(1,1,1,0);
    AmmoCard ammoCard2 = new AmmoCard(ammoCard);

    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        ammoCard.getAmmo(AmmoColor.RED),
        ammoCard2.getAmmo(AmmoColor.RED));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    AmmoCard ammoCard = null;
    AmmoCard ammoCard2 = new AmmoCard(ammoCard);
  }

  @Test
  public void testToSTring() {
    AmmoCard ammoCard1 = new AmmoCard(2, 1, 0, 0);
    AmmoCard ammoCard2 = new AmmoCard(0, 0, 1, 2);
    assertEquals(
        "toString() method returned unexpected value",
        "RRB",
        ammoCard1.toString()
    );
    assertEquals(
        "toString() method returned unexpected value",
        "YPP",
        ammoCard2.toString()
    );
  }
}
