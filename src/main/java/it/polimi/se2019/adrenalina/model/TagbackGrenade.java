package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Class defining a Tagback Grenade powerup card
 */
public class TagbackGrenade extends PowerUp {

  private static final long serialVersionUID = 8370118473308407561L;

  public TagbackGrenade(AmmoColor color) {
    super(color, false);
  }

  /**
   * Copy constructor
   * @param powerup TagbackGrenade object that has to be copied, can't be null
   */
  public TagbackGrenade(TagbackGrenade powerup) {
    // TODO: copy actions
    // TODO: copy target history
    super(powerup.getColor(), false);
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }

  @Override
  public TagbackGrenade copy() {
    return new TagbackGrenade(this);
  }

  @Override
  public String getName() {
    return "Granata a frammentazione";
  }

  /**
   * Create TagbackGreande object from json formatted String
   * @param json json input String
   * @return TagbackGrenade
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static TagbackGrenade deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, TagbackGrenade.class);
  }
}