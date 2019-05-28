package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Class defining a Targeting Scoper powerup card
 */
public class TargetingScope extends PowerUp {

  private static final long serialVersionUID = -9185952826985193602L;

  public TargetingScope(AmmoColor color) {
    super(color, true, PowerUpType.TARGETING_SCOPE);
  }

  @Override
  public String getName() {
    return "Mirino";
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    if (ammoColor == AmmoColor.ANY) {
      return 1;
    }
    return 0;
  }

  @Override
  public TargetingScope copy() {
    return new TargetingScope(getColor());
  }

  @Override
  public boolean canUse() {
    //TODO implement function
    return true;
  }

  /**
   * Create TargetingScope object from json formatted String
   *
   * @param json json input String
   * @return TargetingScope
   */
  public static TargetingScope deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, TargetingScope.class);
  }
}
