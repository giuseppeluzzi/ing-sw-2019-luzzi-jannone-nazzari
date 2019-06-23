package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * This class describes an AmmoCard.
 */
public class AmmoCard implements Serializable {

  private static final long serialVersionUID = -2038045748292008287L;
  private final HashMap<AmmoColor, Integer> ammo;
  private final int powerUp;

  /**
   * Class constructor.
   * @param red number of red ammos
   * @param blue  number of blue ammos
   * @param yellow  number of yellow ammos
   * @param powerUp number of powerUps
   * @throws InvalidParameterException thrown if the sum of parameters is
   * different than 3
   */
  public AmmoCard(int red, int blue, int yellow, int powerUp) {
    if (red + blue + yellow + powerUp != 3) {
      throw new InvalidParameterException("AmmoCard has three slots");
    }
    this.powerUp = powerUp;
    ammo = new HashMap<>();
    ammo.put(AmmoColor.RED, red);
    ammo.put(AmmoColor.BLUE, blue);
    ammo.put(AmmoColor.YELLOW, yellow);
  }

  /**
   * Copy constructor, creates an exact copy of ammoCard.
   * @param ammoCard the ammoCard to be cloned, has to be not null
   */
  public AmmoCard(AmmoCard ammoCard) {
    if (ammoCard == null) {
      throw new IllegalArgumentException("Argument ammoCard cannot be null");
    }
    powerUp = ammoCard.powerUp;
    ammo = new HashMap<>();
    ammo.put(AmmoColor.RED, ammoCard.ammo.get(AmmoColor.RED));
    ammo.put(AmmoColor.BLUE, ammoCard.ammo.get(AmmoColor.BLUE));
    ammo.put(AmmoColor.YELLOW, ammoCard.ammo.get(AmmoColor.YELLOW));
  }

  /**
   * Get the amount of ammos of a given color.
   * @param color the ammo color to consider
   * @return returns the amount of ammos of a given color
   */
  public int getAmmo(AmmoColor color) {
    return ammo.get(color);
  }

  /**
   * Get the powerUp featured in an ammoCard.
   * @return the powerUp featured in the ammoCard
   */
  public int getPowerUp() {
    return powerUp;
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
   * Builds and returns a three character string representing a specific ammoCard. Each character
   * can be either 'R' (for a red ammo), 'B' (for a blue ammo), 'Y' (for a yellow ammo), 'P' (for
   * a powerUp). The order is fixed and is always 'R', 'B', 'Y', 'P'
   * @return the string representation of the ammoCard
   */
  public String toString() {
    StringBuilder out = new StringBuilder(3);
    for (int i = 0; i < ammo.get(AmmoColor.RED); i++) {
      out.append("R");
    }
    for (int i = 0; i < ammo.get(AmmoColor.BLUE); i++) {
      out.append("B");
    }
    for (int i = 0; i < ammo.get(AmmoColor.YELLOW); i++) {
      out.append("Y");
    }
    for (int i = 0; i < powerUp; i++) {
      out.append("P");
    }
    return out.toString();
  }

  /**
   * Creates AmmoCard object from a JSON serialized object.
   * @param json JSON input String
   * @return AmmoCard object
   */
  public static AmmoCard deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, AmmoCard.class);
  }
}
