package it.polimi.se2019.adrenalina.event;

/**
 * Event sent for disconnecting a client.
 */
public class PlayerDisconnectEvent implements Event {

  private static final long serialVersionUID = -6091901271781502467L;
  private final String message;
  private final boolean keepAlive;

  public PlayerDisconnectEvent(String message, boolean keepAlive) {
    this.message = message;
    this.keepAlive = keepAlive;
  }

  public String getMessage() {
    return message;
  }

  public boolean keepAlive() {
    return keepAlive;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DISCONNECT_EVENT;
  }
}
