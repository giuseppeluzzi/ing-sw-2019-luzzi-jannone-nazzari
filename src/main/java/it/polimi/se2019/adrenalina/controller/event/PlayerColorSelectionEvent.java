package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerColorSelectionEvent implements Event {

  private static final long serialVersionUID = -7326023641847202438L;
  private final PlayerColor playerColor;
  private final PlayerColor newPlayerColor;

  public PlayerColorSelectionEvent(PlayerColor playerColor,
      PlayerColor newPlayerColor) {
    this.playerColor = playerColor;
    this.newPlayerColor = newPlayerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PlayerColor getNewPlayerColor() {
    return newPlayerColor;
  }
}
