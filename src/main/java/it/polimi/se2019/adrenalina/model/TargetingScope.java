package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;

public class TargetingScope extends PowerUp {
  public TargetingScope(AmmoColor color) {
    super(color);
  }

  public TargetingScope(TargetingScope powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }
  
  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }

  public static TargetingScope deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, TargetingScope.class);
  }
}
