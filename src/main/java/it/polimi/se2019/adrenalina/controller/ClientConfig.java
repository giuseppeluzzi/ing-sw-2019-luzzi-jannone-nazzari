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
public class ClientConfig {

  private String serverIP;
  private Integer rmiPort;
  private Integer socketPort;
  private Integer turnTimeout;
  private Integer minNumPlayers;

  private static ClientConfig instance = null;

  private ClientConfig() {
    // private constructor
  }

  public static synchronized ClientConfig getInstance() {
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

  public String getServerIP() {
    return serverIP;
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
