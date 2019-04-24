package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

public class TeleporterTest {
  @Test
  public void testCopyConstructor() {
    Teleporter teleporter = new Teleporter(AmmoColor.RED);
    Teleporter teleporter2 = new Teleporter(teleporter);
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        teleporter.getColor(),
        teleporter2.getColor());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyConstructorException() {
    Teleporter teleporter = null;
    Teleporter teleporter2 = new Teleporter(teleporter);
  }

  @Test
  public void testSerialization() {
    Teleporter teleporter = new Teleporter(AmmoColor.RED);
    Teleporter teleporter2;
    String json = teleporter.serialize();

    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    teleporter2 = Teleporter.deserialize(json);
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        teleporter.getColor().toString(),
        teleporter2.getColor().toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Teleporter.deserialize(null);
  }
}