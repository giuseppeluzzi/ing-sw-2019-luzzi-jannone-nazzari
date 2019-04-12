package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Kill;

public class KillShotEvent implements Event {
  private final Kill kill;

  public KillShotEvent(Kill kill) {
    this.kill = kill;
  }

  @Override
  public KillShotEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, KillShotEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.KILLSHOT_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Kill getKill() {
    return kill;
  }

}
