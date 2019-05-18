package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;

public class TimerSetEvent implements Event {

  private static final long serialVersionUID = 113909288118601962L;
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

  public int getTimer() {
    return timer;
  }
}
