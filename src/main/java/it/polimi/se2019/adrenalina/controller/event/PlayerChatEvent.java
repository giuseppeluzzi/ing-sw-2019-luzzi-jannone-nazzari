package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;

public class PlayerChatEvent {
  private final Player player;
  private final String message;

  public PlayerChatEvent(Player player, String message) {
    this.player = player;
    this.message = message;
  }

  public Player getPlayer() {
    return player;
  }

  public String getMessage() {
    return message;
  }
}
