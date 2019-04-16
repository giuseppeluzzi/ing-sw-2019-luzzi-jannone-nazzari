package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

/**
 * Class defining a Newton powerup card
 */
public class Newton extends PowerUp {
  public Newton(AmmoColor color) {
    super(color);
  }

  /**
   * Copy constructor
   * @param powerup Newton object that has to be copied, can't be null
   */
  public Newton(Newton powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }

  @Override
  public Newton copy() {
    return new Newton(this);
  }

  /**
   * Create Newton object from json formatted String
   * @param json json input String
   * @return Newton
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static Newton deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Newton.class);
  }
}




