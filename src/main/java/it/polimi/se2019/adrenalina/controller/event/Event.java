package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.Serializable;

public interface Event extends Serializable {

  EventType getEventType();

  default String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.getEventTypeByClass(getClass()).toString());
    return gson.toJson(jsonElement);
  }
}