package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerStatusUpdate implements Event {

  private static final long serialVersionUID = 2472359537454785873L;
  private final PlayerStatus playerStatus;

  public PlayerStatusUpdate(PlayerStatus playerStatus) {
    this.playerStatus = playerStatus;
  }

  public PlayerStatus getPlayerStatus() {
    return playerStatus;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_STATUS_UPDATE;
  }
}
