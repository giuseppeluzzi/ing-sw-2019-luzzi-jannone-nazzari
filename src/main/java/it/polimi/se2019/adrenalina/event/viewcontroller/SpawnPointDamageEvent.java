package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player damages a spawn point.
 */
public class SpawnPointDamageEvent implements Event {

  private static final long serialVersionUID = 2800089775218160405L;
  private final PlayerColor playerColor;
  private final AmmoColor ammoColor;

  public SpawnPointDamageEvent(PlayerColor playerColor,
      AmmoColor ammoColor) {
    this.playerColor = playerColor;
    this.ammoColor = ammoColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public AmmoColor getAmmoColor() {
    return ammoColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.SPAWN_POINT_DAMAGE_EVENT;
  }
}
