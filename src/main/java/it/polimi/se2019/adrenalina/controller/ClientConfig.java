package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.IOUtils;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Client specific configuration class
 */
public final class ClientConfig implements Configuration {

  private String serverIP;
  private Integer rmiPort;
  private Integer socketPort;
  private Integer turnTimeout;
  private Integer minNumPlayers;

  private static ClientConfig instance = null;
  private static ClientConfig internalInstance = null;

  private ClientConfig() {
    // private constructor
  }

  public static ClientConfig getInstance() {
    return getInstance(false);
  }

  /**
   * Get configuration instance
   * @param forceInternal whether the internal configuration file must be used insted of the external one.
   * @return the singleton instance
   */
  public static ClientConfig getInstance(boolean forceInternal) {
    if (forceInternal) {
      if (internalInstance == null) {
        String json = null;
        try {
          json = IOUtils.readResourceFile("client_config.json");
        } catch (IOException e) {
          Log.severe("Configuration file not found!");
          System.exit(1);
        }
        Gson gson = new Gson();
        internalInstance = gson.fromJson(json, ClientConfig.class);
      }
      return internalInstance;
    } else {
      if (instance == null) {
        String json = null;
        Path extFile = Paths.get(Constants.CLIENT_CONFIG_FILE);
        try {
          json = String.join("\n", Files.readAllLines(extFile));
        } catch (IOException e) {
          Log.severe("Configuration file not found!");
          System.exit(1);
        }
        Gson gson = new Gson();
        instance = gson.fromJson(json, ClientConfig.class);
      }
      return instance;
    }
  }

  public String getServerIP() {
    return serverIP;
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

  @Override
  public Integer getMinNumPlayers() {
    return minNumPlayers;
  }

  public ClientConfig setTurnTimeout(Integer turnTimeout) {
    this.turnTimeout = turnTimeout;
    return this;
  }

  public ClientConfig setMinNumPlayers(Integer minNumPlayers) {
    this.minNumPlayers = minNumPlayers;
    return this;
  }
}
