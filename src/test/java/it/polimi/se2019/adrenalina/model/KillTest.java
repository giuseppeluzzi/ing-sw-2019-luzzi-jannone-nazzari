package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class KillTest {
  @Test
  public void testCopyConstructor() {
    Kill kill = new Kill(PlayerColor.GREEN, true);
    Kill kill2;

    kill2 = new Kill(kill);
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        kill.isOverKill(),
        kill2.isOverKill());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    Kill kill = null;
    Kill kill2 = new Kill(kill);
  }

  @Test
  public void testSerialization() {
    Kill kill = new Kill(PlayerColor.GREEN, false);
    Kill kill2;
    String json;

    json = kill.serialize();
    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    kill2 = Kill.deserialize(json);
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        kill.getPlayerColor().toString(),
        kill2.getPlayerColor().toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    Kill.deserialize(null);
  }
}