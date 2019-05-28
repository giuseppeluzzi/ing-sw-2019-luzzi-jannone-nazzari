package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class DominationBoardDamagesUpdate implements Event {

  private static final long serialVersionUID = 6822873911362508716L;
  private final AmmoColor spawnPointColor;
  private final List<PlayerColor> players;

  public DominationBoardDamagesUpdate(AmmoColor spawnPointColor, List<PlayerColor> players) {
    this.spawnPointColor = spawnPointColor;
    this.players = new ArrayList<>(players);
  }

  public AmmoColor getSpawnPointColor() {
    return spawnPointColor;
  }

  public List<PlayerColor> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public EventType getEventType() {
    return EventType.DOMINATION_BOARD_DAMAGES_UPDATE;
  }
}
