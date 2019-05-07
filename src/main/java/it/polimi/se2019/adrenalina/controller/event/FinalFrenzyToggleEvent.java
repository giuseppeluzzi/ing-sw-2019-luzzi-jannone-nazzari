package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class FinalFrenzyToggleEvent implements Event {
  private final boolean enabled;

  public FinalFrenzyToggleEvent(boolean enabled) {
    this.enabled = enabled;
  }

  public static FinalFrenzyToggleEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, FinalFrenzyToggleEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType",
        EventType.FINAL_FRENZY_TOGGLE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public boolean isEnabled() {
    return enabled;
  }

}
