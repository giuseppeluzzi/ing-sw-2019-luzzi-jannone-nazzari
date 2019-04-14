package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class KillTest {
  @Test
  public void testCopyConstructor() {
    Kill kill = new Kill(PlayerColor.GREEN, true);
    Kill kill2;

    kill2 = new Kill(kill);
    assertEquals(kill.isOverKill(), kill2.isOverKill());
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
    if (!json.contains("\"color\":\"GREEN\"")){
      fail();
    }
    kill2 = Kill.deserialize(json);
    assertEquals(kill.getPlayerColor().toString(), kill2.getPlayerColor().toString());
  }
}