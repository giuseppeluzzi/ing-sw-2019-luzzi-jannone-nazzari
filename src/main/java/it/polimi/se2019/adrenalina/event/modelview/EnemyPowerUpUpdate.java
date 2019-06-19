package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event sent when the number of powerUps of a player changes. This event is used by the player's enemies who
 * cannot see what the actual powerUps are.
 */
public class EnemyPowerUpUpdate implements Event {

  private static final long serialVersionUID = 3380422402729267446L;
  private final PlayerColor playerColor;
  private final int powerUpsNum;

  public EnemyPowerUpUpdate(PlayerColor playerColor, int powerUpsNum) {
    this.playerColor = playerColor;
    this.powerUpsNum = powerUpsNum;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public int getPowerUpsNum() {
    return powerUpsNum;
  }

  @Override
  public EventType getEventType() {
    return EventType.ENEMY_POWER_UP_UPDATE;
  }
}
