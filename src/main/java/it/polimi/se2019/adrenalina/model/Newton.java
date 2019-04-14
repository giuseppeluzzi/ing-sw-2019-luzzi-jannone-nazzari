package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

public class Newton extends PowerUp {
  public Newton(AmmoColor color) {
    super(color);
  }

  public Newton(Newton powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }

  public static Newton deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Newton.class);
  }
}




