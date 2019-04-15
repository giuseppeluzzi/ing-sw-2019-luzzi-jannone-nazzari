package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

/**
 * Class defining a Kill.
 */
public class Kill {
  private final PlayerColor color;
  private final boolean overKill;

  /**
   * Class constructor.
   * @param color color of the killer.
   * @param overKill determines if there was an overKill.
   */
  public Kill(PlayerColor color, boolean overKill) {
    this.color = color;
    this.overKill = overKill;
  }

  /**
   * Copy constructor, creates an exact copy of a Kill.
   * @param kill the kill to be cloned, has to be not null.
   */
  public Kill(Kill kill) {
    if (kill == null) {
      throw new IllegalArgumentException("Kill argument can't be null");
    }
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

  public static Kill deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Kill.class);
  }
}
