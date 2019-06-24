package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Event sent when the list of tags or weapons of a player changes.
 */
public class PlayerDamagesTagsUpdate implements Event {

  private static final long serialVersionUID = 1014334077223621057L;
  private final List<PlayerColor> damages;
  private final List<PlayerColor> tags;
  private final PlayerColor playerColor;
  private final PlayerColor killerColor;

  public PlayerDamagesTagsUpdate(List<PlayerColor> damages, List<PlayerColor> tags, PlayerColor playerColor, PlayerColor killerColor) {
    this.damages = new ArrayList<>(damages);
    this.tags = new ArrayList<>(tags);
    this.playerColor = playerColor;
    this.killerColor = killerColor;
  }

  public List<PlayerColor> getDamages() {
    return new ArrayList<>(damages);
  }

  public List<PlayerColor> getTags() {
    return new ArrayList<>(tags);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PlayerColor getKillerColor() {
    return killerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DAMAGES_TAGS_UPDATE;
  }
}
