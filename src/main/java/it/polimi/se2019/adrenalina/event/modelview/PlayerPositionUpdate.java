package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class PlayerPositionUpdate implements Event {

  private static final long serialVersionUID = -3086636605968304095L;
  private final int posX;
  private final int posY;
  private final PlayerColor playerColor;

  public PlayerPositionUpdate(PlayerColor playerColor, int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
    this.playerColor = playerColor;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_POSITION_UPDATE;
  }
}
