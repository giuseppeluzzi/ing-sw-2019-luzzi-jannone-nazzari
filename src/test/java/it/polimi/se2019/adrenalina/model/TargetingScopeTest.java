package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

public class TargetingScopeTest {
  @Test
  public void testCopyConstructor() {
    TargetingScope targetingScope = new TargetingScope(AmmoColor.RED);
    TargetingScope targetingScope2 = new TargetingScope(targetingScope);
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        targetingScope.getColor(),
        targetingScope2.getColor());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyConstructorException() {
    TargetingScope targetingScope = null;
    TargetingScope targetingScope2 = new TargetingScope(targetingScope);
  }

  @Test
  public void testSerialization() {
    TargetingScope targetingScope = new TargetingScope(AmmoColor.RED);
    TargetingScope targetingScope2;
    String json = targetingScope.serialize();

    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    targetingScope2 = TargetingScope.deserialize(json);
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        targetingScope.getColor().toString(),
        targetingScope2.getColor().toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    TargetingScope.deserialize(null);
  }
}