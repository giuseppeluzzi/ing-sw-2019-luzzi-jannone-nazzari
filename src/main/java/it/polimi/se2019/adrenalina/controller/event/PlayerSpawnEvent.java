package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerSpawnEvent implements Event {

  private static final long serialVersionUID = -3420489819986950040L;
  private final PlayerColor playerColor;
  private final int squareX;
  private final int squareY;
  private final PowerUp tossedPowerUp;

  public PlayerSpawnEvent(PlayerColor playerColor, int squareX, int squareY,
      PowerUp tossedPowerUp) {
    this.playerColor = playerColor;
    this.squareX = squareX;
    this.squareY = squareY;
    this.tossedPowerUp = tossedPowerUp;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }

  public PowerUp getTossedPowerUp() {
    return tossedPowerUp;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SPAWN_EVENT;
  }
}
