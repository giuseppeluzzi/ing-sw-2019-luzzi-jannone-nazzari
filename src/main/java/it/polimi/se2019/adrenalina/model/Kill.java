package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.PlayerColor;

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
   *
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

  /**
   * Create Kill object from json formatted String
   * @param json json input String
   * @return Kill
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static Kill deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Kill.class);
  }
}
