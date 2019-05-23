package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Kill;

public class KillShotEvent implements Event {

  private static final long serialVersionUID = 452185862548139792L;
  private final Kill kill;

  public KillShotEvent(Kill kill) {
    this.kill = kill;
  }

  public static KillShotEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, KillShotEvent.class);
  }

  public Kill getKill() {
    return kill;
  }

  @Override
  public EventType getEventType() {
    return EventType.KILLSHOT_EVENT;
  }
}
