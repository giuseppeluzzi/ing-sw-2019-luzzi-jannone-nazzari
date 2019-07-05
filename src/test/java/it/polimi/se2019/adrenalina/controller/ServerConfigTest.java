package it.polimi.se2019.adrenalina.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServerConfigTest {

  @Test
  public void testConfig() {
    assertNotNull(ServerConfig.getInstance(true));
    assertEquals(new Integer(3), ServerConfig.getInstance(true).getMinNumPlayers());
    assertEquals(new Integer(120), ServerConfig.getInstance(true).getTurnTimeout());
    assertTrue(ServerConfig.getInstance(true).getSocketPort() > 0 && ServerConfig.getInstance(true).getSocketPort() < 65535);
    assertTrue(ServerConfig.getInstance(true).getRmiPort() > 0 && ServerConfig.getInstance(true).getRmiPort() < 65535);
    assertTrue(ServerConfig.getInstance().getJoinTimeout() > 0);
    assertTrue(ServerConfig.getInstance().getSuspendTimeoutCount() > 0);
    assertTrue(ServerConfig.getInstance().getDeathDamages() > 1);
    assertTrue(ServerConfig.getInstance().getSpawnPointDamagesFF() > 1);
    assertFalse(ServerConfig.getInstance().getWeaponFiles().isEmpty());
    assertFalse(ServerConfig.getInstance().getMapFiles().isEmpty());
  }

  @Test
  public void testInstance() {
    assertNotNull(ServerConfig.getInstance());
    assertNotNull(ServerConfig.getInstance());
  }
}