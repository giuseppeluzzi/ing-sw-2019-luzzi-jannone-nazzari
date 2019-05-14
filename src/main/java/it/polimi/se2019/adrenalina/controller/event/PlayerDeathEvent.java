package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.io.Serializable;

public class PlayerDeathEvent implements Event {

  private static final long serialVersionUID = -4934503267095818667L;
  private final PlayerColor playerColor;
  private final PlayerColor killerColor;

  public PlayerDeathEvent(PlayerColor playerColor,
      PlayerColor killerColor) {
    this.playerColor = playerColor;
    this.killerColor = killerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PlayerColor getKillerColor() {
    return killerColor;
  }
}
