package it.polimi.se2019.adrenalina.controller.event.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;

/**
 * Event fired a player discards a powerup to spawn
 */
public class PlayerDiscardPowerUpEvent implements Event {

  private static final long serialVersionUID = -4129538766030971119L;
  private final PlayerColor playerColor;
  private final PowerUp powerUp;

  public PlayerDiscardPowerUpEvent(PlayerColor playerColor,
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
    return EventType.PLAYER_DISCARD_POWERUP_EVENT;
  }
}
