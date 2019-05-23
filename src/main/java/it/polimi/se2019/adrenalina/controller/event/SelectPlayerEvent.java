package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

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
