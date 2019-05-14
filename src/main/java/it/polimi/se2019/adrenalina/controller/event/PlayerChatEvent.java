package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerChatEvent implements Event {

  private static final long serialVersionUID = 1994729247209772154L;

  private final PlayerColor playerColor;
  private final String message;

  public PlayerChatEvent(PlayerColor playerColor, String message) {
    this.playerColor = playerColor;
    this.message = message;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getMessage() {
    return message;
  }
}
