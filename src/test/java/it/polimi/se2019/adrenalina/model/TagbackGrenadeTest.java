package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

public class TagbackGrenadeTest {
  @Test
  public void testCopyConstructor() {
    TagbackGrenade tagbackGrenade = new TagbackGrenade(AmmoColor.RED);
    TagbackGrenade tagbackGrenade2 = new TagbackGrenade(tagbackGrenade);
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        tagbackGrenade.getColor(),
        tagbackGrenade2.getColor());
  }

  @Test(expected = NullPointerException.class)
  public void testCopyConstructorException() {
    TagbackGrenade tagbackGrenade = null;
    TagbackGrenade tagbackGrenade2 = new TagbackGrenade(tagbackGrenade);
  }

  @Test
  public void testSerialization() {
    TagbackGrenade tagbackGrenade = new TagbackGrenade(AmmoColor.RED);
    TagbackGrenade tagbackGrenade2;
    String json = tagbackGrenade.serialize();

    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    tagbackGrenade2 = TagbackGrenade.deserialize(json);
    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        tagbackGrenade.getColor(),
        tagbackGrenade2.getColor());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    TagbackGrenade.deserialize(null);
  }
}