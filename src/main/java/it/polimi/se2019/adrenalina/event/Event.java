package it.polimi.se2019.adrenalina.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import java.io.Serializable;

public interface Event extends Serializable {

  EventType getEventType();

  default String serialize() {
    GsonBuilder builder = new GsonBuilder();

    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy())
        .create();
    JsonElement jsonElement = gson.toJsonTree(this);

    jsonElement.getAsJsonObject()
        .addProperty("eventType", EventType.getEventTypeByClass(getClass()).toString());
    return gson.toJson(jsonElement);
  }
}