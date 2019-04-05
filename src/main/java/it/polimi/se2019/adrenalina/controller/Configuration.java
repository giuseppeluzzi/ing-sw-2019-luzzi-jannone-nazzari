package it.polimi.se2019.adrenalina.controller;

public class Configuration {
  private String serverIP;
  private Integer joinTimeout;
  private Integer turnTimeout;

  private static Configuration instance;

  private Configuration() {

  }

  public static synchronized Configuration getInstance() {
    if (instance == null) {
      instance = new Configuration();
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
}
