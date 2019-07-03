package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientConfigTest {

  @Test
  public void testGetInstance() {
    assertNotNull(ClientConfig.getInstance());
    ClientConfig.getInstance().setTurnTimeout(120);
    ClientConfig.getInstance().setMinNumPlayers(3);
    assertEquals(new Integer(120), ClientConfig.getInstance().getTurnTimeout());
    assertEquals(new Integer(2345), ClientConfig.getInstance().getSocketPort());
    assertEquals(new Integer(2234), ClientConfig.getInstance().getRmiPort());
    assertEquals(new Integer(3), ClientConfig.getInstance().getMinNumPlayers());
    assertEquals("127.0.0.1", ClientConfig.getInstance().getServerIP());
  }
}