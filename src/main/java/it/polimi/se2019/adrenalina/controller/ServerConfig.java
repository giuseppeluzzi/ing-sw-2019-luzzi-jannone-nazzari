package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.IOUtils;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Server specific configuration class
 */
public final class ServerConfig implements Configuration {

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
  private static ServerConfig internalInstance = null;

  private ServerConfig() {
    // private constructor
  }

  public static ServerConfig getInstance() {
    return getInstance(false);
  }

  /**
   * Get configuration instance
   * @param forceInternal whether the internal configuration file must be used insted of the external one.
   * @return the singleton instance
   */
  public static ServerConfig getInstance(boolean forceInternal) {
    if (! forceInternal) {
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
    } else {
      if (internalInstance == null) {
        String json = null;
        try {
          json = IOUtils.readResourceFile("server_config.json");
        } catch (IOException e) {
          Log.severe("Configuration file not found!");
          System.exit(1);
        }
        Gson gson = new Gson();
        internalInstance = gson.fromJson(json, ServerConfig.class);
      }
      return internalInstance;
    }
  }

  public Integer getJoinTimeout() {
    return joinTimeout;
  }

  @Override
  public Integer getTurnTimeout() {
    return turnTimeout;
  }

  @Override
  public Integer getRmiPort() {
    return rmiPort;
  }

  @Override
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

  @Override
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
