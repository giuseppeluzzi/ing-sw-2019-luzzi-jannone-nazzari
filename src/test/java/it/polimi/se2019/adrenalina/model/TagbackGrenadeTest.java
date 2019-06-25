package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Test;

public class TagbackGrenadeTest {

  @Test
  public void testCopy() {
    TagbackGrenade tagbackGrenade1 = new TagbackGrenade(AmmoColor.RED);
    TagbackGrenade tagbackGrenade2 = tagbackGrenade1.copy();
    assertEquals(tagbackGrenade1.getName(), tagbackGrenade2.getName());
    assertEquals(tagbackGrenade1.getSymbol(), tagbackGrenade2.getSymbol());
    assertEquals(tagbackGrenade1.getColor(), tagbackGrenade2.getColor());
  }
}