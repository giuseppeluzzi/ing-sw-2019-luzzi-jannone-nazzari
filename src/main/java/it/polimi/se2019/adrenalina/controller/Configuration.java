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
  private Integer tuiDashboardWidth;
  private Integer tuiDashboardHeight;
  private Integer tuiSquareWidth;
  private Integer tuiDoorWidth;
  private String tuiHorizontalLine;
  private String tuiPlayerIcon;
  private Integer tuiDoorHeight;
  private Integer tuiSquareHeight;
  private String tuiVerticalLine;
  private String tuiSpawnPointIcon;
  private String tuiTagIcon;
  private String tuiDamageIcon;

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

  public Integer getTuiDashboardWidth() {
    return tuiDashboardWidth;
  }

  public Integer getTuiDashboardHeight() {
    return tuiDashboardHeight;
  }

  public Integer getTuiSquareWidth() {
    return tuiSquareWidth;
  }

  public Integer getTuiDoorWidth() {
    return tuiDoorWidth;
  }

  public String getTuiHorizontalLine() {
    return tuiHorizontalLine;
  }

  public String getTuiPlayerIcon() {
    return tuiPlayerIcon;
  }

  public Integer getTuiDoorHeight() {
    return tuiDoorHeight;
  }

  public Integer getTuiSquareHeight() {
    return tuiSquareHeight;
  }

  public String getTuiVerticalLine() {
    return tuiVerticalLine;
  }

  public String getTuiSpawnPointIcon() {
    return tuiSpawnPointIcon;
  }

  public String getTuiTagIcon() {
    return tuiTagIcon;
  }

  public String getTuiDamageIcon() {
    return tuiDamageIcon;
  }

}
