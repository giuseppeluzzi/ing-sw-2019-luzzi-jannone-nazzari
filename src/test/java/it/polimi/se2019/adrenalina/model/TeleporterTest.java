package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Before;
import org.junit.Test;

public class TeleporterTest {
  private Teleporter teleporter;

  @Before
  public void setTeleporter() {
    teleporter = new Teleporter(AmmoColor.RED);
  }

  @Test
  public void testCopy() {
    Teleporter teleporter2 = teleporter.copy();
    assertEquals(teleporter.getName(), teleporter2.getName());
    assertEquals(teleporter.getSymbol(), teleporter2.getSymbol());
    assertEquals(teleporter.getColor(), teleporter2.getColor());
  }
}