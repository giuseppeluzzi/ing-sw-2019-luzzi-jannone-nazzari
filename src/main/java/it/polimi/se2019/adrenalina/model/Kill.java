package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.PlayerColor;

import java.io.Serializable;

/**
 * Class describing a Kill.
 */
public class Kill implements Serializable {
  private final PlayerColor color;
  private final boolean overKill;

  /**
   * Class constructor.
   * @param color color of the killer.
   * @param overKill determines if there was an overKill
   */
  public Kill(PlayerColor color, boolean overKill) {
    this.color = color;
    this.overKill = overKill;
  }

  /**
   * Copy constructor, creates an exact copy of a Kill.
   * @param kill the kill to be cloned, has to be not null
   */
  public Kill(Kill kill) {
    if (kill == null) {
      throw new IllegalArgumentException("Kill argument can't be null");
    }
    color = kill.color;
    overKill = kill.overKill;
  }

  /**
   * Returns the color of the killer.
   * @return the color of the killer
   */
  public PlayerColor getPlayerColor() {
    return color;
  }

  /**
   * Returns whether this is an overKill.
   * @return true if this is an overKill, false otherwhise.
   */
  public boolean isOverKill() {
    return overKill;
  }

  /**
   * Gson serialization.
   * @return JSON string containing serialized object
   */
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Creates a Kill object from a JSON serialized object.
   * @param json JSON input String
   * @return Kill object
   */
  public static Kill deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Kill.class);
  }
}
