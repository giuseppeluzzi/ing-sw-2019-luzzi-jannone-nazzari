package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class MapSelectionEvent implements Event {
  private final int map;

  public MapSelectionEvent(int map) {
    this.map = map;
  }

  public static MapSelectionEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, MapSelectionEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType",
        EventType.MAP_SELECTION_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public int getMap() {
    return map;
  }

}
