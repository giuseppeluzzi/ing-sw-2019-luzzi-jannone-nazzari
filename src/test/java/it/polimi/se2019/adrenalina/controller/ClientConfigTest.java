package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientConfigTest {

  @Test
  public void testConfig() {
    assertNotNull(ClientConfig.getInstance(true));
    ClientConfig.getInstance(true).setTurnTimeout(120);
    ClientConfig.getInstance(true).setMinNumPlayers(3);
    assertEquals(new Integer(3), ClientConfig.getInstance(true).getMinNumPlayers());
    assertEquals(new Integer(120), ClientConfig.getInstance(true).getTurnTimeout());
    assertTrue(ClientConfig.getInstance(true).getSocketPort() > 0 && ClientConfig.getInstance(true).getSocketPort() < 65535);
    assertTrue(ClientConfig.getInstance(true).getRmiPort() > 0 && ClientConfig.getInstance(true).getRmiPort() < 65535);
    assertTrue(ClientConfig.getInstance(true).getServerIP().matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"));
  }

  @Test
  public void testInstance() {
    assertNotNull(ClientConfig.getInstance());
    assertNotNull(ServerConfig.getInstance());
  }
}