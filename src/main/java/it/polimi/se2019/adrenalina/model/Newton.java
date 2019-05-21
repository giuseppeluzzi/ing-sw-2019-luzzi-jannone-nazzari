package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Class describing a Newton powerUp card.
 */
public class Newton extends PowerUp {

  private static final long serialVersionUID = 3859349171755429567L;

  public Newton(AmmoColor color) {
    super(color);
  }

  /**
   * Copy constructor, creates an exact copy of a Newton.
   * @param powerup Newton object to be cloned, has to be not null
   */
  public Newton(Newton powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  // TODO: documentation
  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }

  @Override
  public String getName() {
    return "Raggio traente";
  }

  /**
   * Returns an exact copy of this object.
   * @return an exact copy of this object
   */
  @Override
  public Newton copy() {
    return new Newton(this);
  }

  /**
   * Creates Newton object from a JSON serialized object.
   * @param json JSON input String
   * @return Netwon object
   */
  public static Newton deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Newton.class);
  }
}
