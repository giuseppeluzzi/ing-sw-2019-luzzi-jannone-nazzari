package it.polimi.se2019.adrenalina.event;

public class PlayerDisconnectEvent implements Event {

  private static final long serialVersionUID = -6091901271781502467L;
  private final String message;

  public PlayerDisconnectEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DISCONNECT_EVENT;
  }
}
