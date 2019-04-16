package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

/**
 * Class defining a Kill
 */
public class Kill {
  private final PlayerColor color;
  private final boolean overKill;

  /**
   * Class constructor
   * @param color color of the player
   * @param overKill defines if overKill was achieved
   */
  public Kill(PlayerColor color, boolean overKill) {
    this.color = color;
    this.overKill = overKill;
  }

  /**
   * Copy constructor
   * @param kill kill to be copied, has to be not null
   * @exception IllegalArgumentException thrown if kill argument is null
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

  /**
   * Create json serialization of an Kill object
   * @return String
   */
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
