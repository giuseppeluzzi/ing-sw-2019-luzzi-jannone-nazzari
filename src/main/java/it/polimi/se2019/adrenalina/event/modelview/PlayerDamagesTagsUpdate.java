package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class PlayerDamagesTagsUpdate implements Event {

  private static final long serialVersionUID = 1014334077223621057L;
  private final List<PlayerColor> damages;
  private final List<PlayerColor> tags;

  public PlayerDamagesTagsUpdate(List<PlayerColor> damages, List<PlayerColor> tags) {
    this.damages = new ArrayList<>(damages);
    this.tags = new ArrayList<>(tags);
  }

  public List<PlayerColor> getDamages() {
    return new ArrayList<>(damages);
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DAMAGES_TAGS_UPDATE;
  }
}
