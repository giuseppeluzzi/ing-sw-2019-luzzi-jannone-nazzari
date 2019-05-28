package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Kill;
import java.util.ArrayList;
import java.util.List;

public class BoardKillShotsUpdate implements Event {

  private static final long serialVersionUID = -4036656260665892023L;
  private final List<Kill> killShots;

  public BoardKillShotsUpdate(List<Kill> killShots) {
    this.killShots = new ArrayList<>(killShots);
  }

  public List<Kill> getPlayers() {
    return new ArrayList<>(killShots);
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_KILL_SHOTS_UPDATE;
  }
}
