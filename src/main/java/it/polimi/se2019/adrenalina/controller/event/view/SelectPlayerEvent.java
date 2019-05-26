package it.polimi.se2019.adrenalina.controller.event.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;

/**
 * Event fired when a player selects another player as a target during a shooting
 */
public class SelectPlayerEvent implements Event {

  private static final long serialVersionUID = 1994729247209772154L;
  private final PlayerColor playerColor;
  private final PlayerColor selectedColor;

  public SelectPlayerEvent(PlayerColor player,
      PlayerColor selectedColor) {
    playerColor = player;
    this.selectedColor = selectedColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PlayerColor getSelectedColor() {
    return selectedColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.SELECT_PLAYER_EVENT;
  }
}
