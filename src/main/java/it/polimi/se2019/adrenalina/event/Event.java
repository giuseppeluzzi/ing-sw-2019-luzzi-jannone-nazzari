package it.polimi.se2019.adrenalina.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import java.io.Serializable;

/**
 * An event is an object passed between client and server (and vice versa)
 * to receive and provide updates during the game.
 */
public interface Event extends Serializable {

  EventType getEventType();

  /**
   * If an event from client to server contains private information about a specific player,
   * and thus must only be sent to his client, this method returns the color of that player.
   * @return the player color whose private information is contained in the event; {@code null}
   * if the event is public.
   */
  default PlayerColor getPrivatePlayerColor() {
    return null;
  }

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