package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;

public class PlayerCollectAmmoEvent implements Event {
  private final Player player;
  private final Square square;

  public PlayerCollectAmmoEvent(Player player, Square square) {
    this.player = player;
    this.square = square;
  }

  @Override
  public String getEventName() {
    return "PlayerCollectAmmo";
  }

  public Player getPlayer() {
    return player;
  }

  public Square getSquare() {
    return square;
  }
}
