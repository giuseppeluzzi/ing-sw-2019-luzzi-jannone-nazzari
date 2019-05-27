package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class CurrentPlayerUpdate implements Event {

  private static final long serialVersionUID = -4472876069196135327L;
  private final PlayerColor currentPlayerColor;

  public CurrentPlayerUpdate(PlayerColor currentPlayerColor) {
    this.currentPlayerColor = currentPlayerColor;
  }

  public PlayerColor getCurrentPlayerColor() {
    return currentPlayerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.CURRENT_PLAYER_UPDATE;
  }
}
