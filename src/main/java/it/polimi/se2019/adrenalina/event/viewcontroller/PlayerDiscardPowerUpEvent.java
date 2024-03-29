package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUpType;

/**
 * Event fired a player discards a powerup for (re)spawning.
 */
public class PlayerDiscardPowerUpEvent implements Event {

  private static final long serialVersionUID = -4129538766030971119L;
  private final PlayerColor playerColor;
  private final PowerUpType type;
  private final AmmoColor color;

  public PlayerDiscardPowerUpEvent(PlayerColor playerColor, PowerUpType type,
      AmmoColor color) {
    this.playerColor = playerColor;
    this.type = type;
    this.color = color;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PowerUpType getPowerUpType() {
    return type;
  }

  public AmmoColor getPowerUpColor() {
    return color;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DISCARD_POWERUP_EVENT;
  }
}
