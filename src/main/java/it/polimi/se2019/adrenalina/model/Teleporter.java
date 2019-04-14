package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

public class Teleporter extends PowerUp {
  public Teleporter(AmmoColor color) {
    super(color);
  }

  public Teleporter(Teleporter powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }

  public static Teleporter deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Teleporter.class);
  }
}