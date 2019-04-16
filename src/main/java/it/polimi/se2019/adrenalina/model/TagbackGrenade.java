package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

/**
 * Class defining a Tagback Grenade powerup card
 */
public class TagbackGrenade extends PowerUp {
  public TagbackGrenade(AmmoColor color) {
    super(color);
  }

  /**
   * Copy constructor
   * @param powerup TagbackGrenade object that has to be copied, can't be null
   */
  public TagbackGrenade(TagbackGrenade powerup) {
    // TODO: copy actions
    super(powerup.getColor());
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