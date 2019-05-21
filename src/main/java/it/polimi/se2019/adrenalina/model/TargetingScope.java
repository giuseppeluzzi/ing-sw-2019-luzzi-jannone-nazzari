package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Class defining a Targeting Scoper powerup card
 */
public class TargetingScope extends PowerUp {

  private static final long serialVersionUID = -9185952826985193602L;

  public TargetingScope(AmmoColor color) {
    super(color, true);
  }

  /**
   * Copy constructor
   * @param powerup TargetingScope object that has to be copied, can't be null
   */
  public TargetingScope(TargetingScope powerup) {
    // TODO: copy actions
    // TODO: copy target history
    super(powerup.getColor(), true);
  }
  
  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }

  @Override
  public String getName() {
    return "Mirino";
  }

  @Override
  public TargetingScope copy() {
    return new TargetingScope(this);
  }

  /**
   * Create TargetingScope object from json formatted String
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
