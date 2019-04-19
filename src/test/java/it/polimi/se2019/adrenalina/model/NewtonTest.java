package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

public class NewtonTest {
  @Test
  public void testCopyConstructor() {
    Newton newton = new Newton(AmmoColor.RED);
    Newton newton2 = new Newton(newton);
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        newton.getColor(),
        newton2.getColor());
  }

  @Test
  public void testSerialization() {
    Newton newton = new Newton(AmmoColor.RED);
    Newton newton2;
    String json = newton.serialize();

    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    newton2 = Newton.deserialize(json);
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        newton.getColor().toString(),
        newton2.getColor().toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Newton.deserialize(null);
  }
}