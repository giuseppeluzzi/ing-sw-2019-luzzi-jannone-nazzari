package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class AmmoCard {
  private final HashMap<AmmoColor, Integer> ammo;
  private final int powerUp;

  /**
   * Class constructor.
   * @param red number of red ammos.
   * @param blue  number of blue ammos.
   * @param yellow  number of yellow ammos.
   * @param powerUp number of powerups.
   * @throws InvalidParameterException thrown if the sum of parameters is
   * different than 3.
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
   * @param ammoCard the ammoCard to be cloned, has to be not null.
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

  public int getAmmo(AmmoColor color) {
    return ammo.get(color);
  }

  public int getPowerUp() {
    return powerUp;
  }

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
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, AmmoCard.class);
  }
}
