package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import java.io.Serializable;

public class PlayerChatEvent implements Event, Serializable {
  private final Player player;
  private final String message;

  public PlayerChatEvent(Player player, String message) {
    this.player = player;
    this.message = message;
  }

  public Player getPlayer() {
    return player;
  }

  public String getMessage() {
    return message;
  }

  public static PlayerChatEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerChatEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_CHAT_EVENT.toString());
    return gson.toJson(jsonElement);
  }
}
