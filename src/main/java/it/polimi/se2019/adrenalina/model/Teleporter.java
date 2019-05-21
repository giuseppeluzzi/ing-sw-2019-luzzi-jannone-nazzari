package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Class defining a Teleporter powerup card
 */
public class Teleporter extends PowerUp {

  private static final long serialVersionUID = -384452700114270235L;

  public Teleporter(AmmoColor color) {
    super(color);
  }

  /**
   * Copy constructor
   * @param powerup Teleporter object that has to be copied, can't be null
   */
  public Teleporter(Teleporter powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }

  @Override
  public Teleporter copy() {
    return new Teleporter(this);
  }

  @Override
  public String getName() {
    return "Teletrasporto";
  }

  /**
   * Create Teleporter object from json formatted String
   * @param json json input String
   * @return Teleporter
   */
  public static Teleporter deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Teleporter.class);
  }
}