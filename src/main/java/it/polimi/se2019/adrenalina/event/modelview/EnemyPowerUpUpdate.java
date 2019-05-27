package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class EnemyPowerUpUpdate implements Event {

  private static final long serialVersionUID = 3380422402729267446L;
  private final PlayerColor enemyColor;
  private final int powerUpsNum;

  public EnemyPowerUpUpdate(PlayerColor enemyColor, int powerUpsNum) {
    this.enemyColor = enemyColor;
    this.powerUpsNum = powerUpsNum;
  }

  public PlayerColor getEnemyColor() {
    return enemyColor;
  }

  public int getPowerUpsNum() {
    return powerUpsNum;
  }

  @Override
  public EventType getEventType() {
    return EventType.ENEMY_POWER_UP_UPDATE;
  }
}
