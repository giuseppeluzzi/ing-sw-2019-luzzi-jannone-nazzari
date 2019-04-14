package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Class defining an ammunition card
 */

public class AmmoCard {
  private final HashMap<AmmoColor, Integer> ammo;
  private final int powerUp;

  /**
   * Class constructor
   * @param red number of red ammo
   * @param blue  number of blue ammo
   * @param yellow  number of yellow ammo
   * @param powerUp number of powerup
   * @throws InvalidParameterException exception thrown if sum of parameters is different than 3
   */
  public AmmoCard(int red, int blue, int yellow, int powerUp) throws InvalidParameterException {
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
   * Copy constructor, create an exact copy of ammoCard parameter
   * @param ammoCard card to be copied, has to be not null
   */
  public AmmoCard(AmmoCard ammoCard) {
    powerUp = ammoCard.powerUp;
    ammo = new HashMap<>();
    ammo.put(AmmoColor.RED, ammoCard.ammo.get(AmmoColor.RED));
    ammo.put(AmmoColor.BLUE, ammoCard.ammo.get(AmmoColor.BLUE));
    ammo.put(AmmoColor.YELLOW, ammoCard.ammo.get(AmmoColor.YELLOW));
  }

  public int getAmmo(AmmoColor color) {
    return ammo.get(color);
  }

  public int getPowerUp() {
    return powerUp;
  }

  /**
   * Create json serialization of an AmmoCard object
   * @return String
   */
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /**
   * Create AmmoCard object from json formatted String
   * @param json json input String
   * @return AmmoCard
   */
  public static AmmoCard deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, AmmoCard.class);
  }
}
