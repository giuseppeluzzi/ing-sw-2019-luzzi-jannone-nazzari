package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

public class Kill {
  private final PlayerColor color;
  private final boolean overKill;

  public Kill(PlayerColor color, boolean overKill) {
    this.color = color;
    this.overKill = overKill;
  }

  public Kill(Kill kill) {
    color = kill.color;
    overKill = kill.overKill;
  }

  public PlayerColor getPlayerColor() {
    return color;
  }

  public boolean isOverKill() {
    return overKill;
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public Kill deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Kill.class);
  }
}
