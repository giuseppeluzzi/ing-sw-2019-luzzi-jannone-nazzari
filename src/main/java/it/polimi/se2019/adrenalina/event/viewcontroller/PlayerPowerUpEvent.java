package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUpType;

/**
 * Event fired when a player uses a powerup.
 */
public class PlayerPowerUpEvent implements Event {

  private static final long serialVersionUID = 1383594161714063049L;
  private final PlayerColor playerColor;
  private final PowerUpType type;
  private final AmmoColor color;

  public PlayerPowerUpEvent(PlayerColor playerColor,
      PowerUpType type, AmmoColor color) {
    this.playerColor = playerColor;
    this.type = type;
    this.color = color;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PowerUpType getType() {
    return type;
  }

  public AmmoColor getColor() {
    return color;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_POWERUP_EVENT;
  }
}
