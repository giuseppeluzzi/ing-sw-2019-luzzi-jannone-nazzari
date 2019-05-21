package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerNoCollectEvent implements Event {

  private static final long serialVersionUID = -2462908384751495127L;
  private final PlayerColor playerColor;

  public PlayerNoCollectEvent(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }
}
