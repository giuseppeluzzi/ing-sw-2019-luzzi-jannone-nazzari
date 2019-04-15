package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

public class TagbackGrenade extends PowerUp {
  public TagbackGrenade(AmmoColor color) {
    super(color);
  }

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
  public PowerUpType powerUpType() {
    return PowerUpType.TAGBACK_GRANADE;
  }

  public static TagbackGrenade deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, TagbackGrenade.class);
  }
}