package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewtonTest {
  @Test
  public void testCopyConstructor() {
    Newton newton = new Newton(AmmoColor.RED);
    Newton newton2 = new Newton(newton);

    assertEquals(newton.getColor(), newton2.getColor());
  }

  @Test
  public void testSerialization() {
    Newton newton = new Newton(AmmoColor.RED);
    Newton newton2;
    String json = newton.serialize();

    if (!json.contains("\"color\":\"RED\"")) {
      fail();
    }
    newton2 = Newton.deserialize(json);
    assertEquals(newton.getColor().toString(), newton2.getColor().toString());
  }
}