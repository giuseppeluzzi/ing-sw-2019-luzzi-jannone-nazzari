package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Configuration {
  private String serverIP;
  private Integer rmiPort;
  private Integer socketPort;
  private Integer joinTimeout;
  private Integer turnTimeout;
  private Integer powerUpTimeout;

  private static Configuration instance = null;

  private Configuration() {
    // private constructor
  }

  public static synchronized Configuration getInstance() {
    if (instance == null) {
      String json = null;
      try {
        File file = new File(Configuration.class.getResource("/config.json").getFile());
        json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
      } catch (IOException e) {
        Log.severe("Configuration file not found!");
        System.exit(0);
      }
      Gson gson = new Gson();
      instance = gson.fromJson(json, Configuration.class);
    }
    return instance;
  }

  public String getServerIP() {
    return serverIP;
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

  public Integer getPowerUpTimeout() {
    return powerUpTimeout;
  }

}
