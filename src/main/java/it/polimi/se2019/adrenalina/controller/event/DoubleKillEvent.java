package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Kill;

public class DoubleKillEvent implements Event {
  private final Kill kill;

  public DoubleKillEvent(Kill kill) {
    this.kill = kill;
  }

  public Kill getKill() {
    return kill;
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.DOUBLE_KILL_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public static DoubleKillEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, DoubleKillEvent.class);
  }
}
