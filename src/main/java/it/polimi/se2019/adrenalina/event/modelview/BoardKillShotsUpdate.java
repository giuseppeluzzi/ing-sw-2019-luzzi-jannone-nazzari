package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class BoardKillShotsUpdate implements Event {

  private static final long serialVersionUID = -4036656260665892023L;
  private final List<PlayerColor> players;

  public BoardKillShotsUpdate(List<PlayerColor> players) {
    this.players = new ArrayList<>(players);
  }

  public List<PlayerColor> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_KILL_SHOTS_UPDATE;
  }
}
