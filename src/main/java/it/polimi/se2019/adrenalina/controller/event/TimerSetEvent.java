package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Kill;

public class TimerSetEvent implements Event {
  private final int timer;

  public TimerSetEvent(int timer) {
    this.timer = timer;
  }

  public static TimerSetEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, TimerSetEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.TIMER_SET_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public int getTimer() {
    return timer;
  }

}
