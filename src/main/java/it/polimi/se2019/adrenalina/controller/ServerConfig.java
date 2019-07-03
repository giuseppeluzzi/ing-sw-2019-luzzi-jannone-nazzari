package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Application configuration read from external file.
 */
public class ServerConfig {

  private Integer joinTimeout;
  private Integer turnTimeout;
  private Integer deathDamages;
  private Integer spawnPointDamagesFF;
  private Integer rmiPort;
  private Integer socketPort;
  private List<String> weaponFiles;
  private List<String> mapFiles;
  private Integer suspendTimeoutCount;
  private Integer minNumPlayers;

  private static ServerConfig instance = null;

  private ServerConfig() {
    // private constructor
  }

  public static synchronized ServerConfig getInstance() {
    if (instance == null) {
      String json = null;
      Path extFile = Paths.get(Constants.SERVER_CONFIG_FILE);
      try {
        json = String.join("\n", Files.readAllLines(extFile));
      } catch (IOException e) {
        Log.severe("Configuration file not found!");
        System.exit(1);
      }
      Gson gson = new Gson();
      instance = gson.fromJson(json, ServerConfig.class);
    }
    return instance;
  }

  public Integer getJoinTimeout() {
    return joinTimeout;
  }

  public Integer getTurnTimeout() {
    return turnTimeout;
  }

  public Integer getRmiPort() {
    return rmiPort;
  }

  public Integer getSocketPort() {
    return socketPort;
  }

  public List<String> getWeaponFiles() {
    return new ArrayList<>(weaponFiles);
  }

  public List<String> getMapFiles() {
    return new ArrayList<>(mapFiles);
  }

  public Integer getSuspendTimeoutCount() {
    return suspendTimeoutCount;
  }

  public Integer getMinNumPlayers() {
    return minNumPlayers;
  }

  public Integer getDeathDamages() {
    return deathDamages;
  }

  public Integer getSpawnPointDamagesFF() {
    return spawnPointDamagesFF;
  }
}
