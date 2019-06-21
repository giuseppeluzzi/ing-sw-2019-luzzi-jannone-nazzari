package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * This event informs players about the color the master player (the game creator).
 */
public class PlayerMasterUpdate implements Event {

  private static final long serialVersionUID = -571718616952529807L;
  private final PlayerColor playerColor;
  private final boolean master;

  public PlayerMasterUpdate(PlayerColor playerColor, boolean master) {
    this.playerColor = playerColor;
    this.master = master;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public boolean isMaster() {
    return master;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_MASTER_UPDATE;
  }
}
