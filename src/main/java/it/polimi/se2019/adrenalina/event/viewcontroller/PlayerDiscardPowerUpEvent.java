package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;

/**
 * Event fired a player discards a powerup to spawn
 */
public class PlayerDiscardPowerUpEvent implements Event {

  private static final long serialVersionUID = -4129538766030971119L;
  private final PlayerColor playerColor;
  private final String name;
  private final AmmoColor color;

  public PlayerDiscardPowerUpEvent(PlayerColor playerColor, String name,
      AmmoColor color) {
    this.playerColor = playerColor;
    this.name = name;
    this.color = color;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getName() {
    return name;
  }

  public AmmoColor getColor() {
    return color;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_DISCARD_POWERUP_EVENT;
  }
}
