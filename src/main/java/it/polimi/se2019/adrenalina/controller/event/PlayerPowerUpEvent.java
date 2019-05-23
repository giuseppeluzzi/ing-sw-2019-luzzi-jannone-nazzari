package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerPowerUpEvent implements Event {

  private static final long serialVersionUID = 1383594161714063049L;
  private final PlayerColor playerColor;
  private final PowerUp powerUp;

  public PlayerPowerUpEvent(PlayerColor playerColor,
      PowerUp powerUp) {
    this.playerColor = playerColor;
    this.powerUp = powerUp;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_POWERUP_EVENT;
  }
}
