package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.utils.IOUtils;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
  private List<String> weaponFiles;
  private List<String> mapFiles;
  private Integer suspendTimeoutCount;
  private Integer minNumPlayers;

  private static Configuration instance;

  private Configuration() {
    // private constructor
  }

  public static synchronized Configuration getInstance() {
    if (instance == null) {
      String json = null;
      try {
        json = IOUtils.readFile("config.json");
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
}
