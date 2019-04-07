package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;

public class PlayerCollectWeaponEvent implements Event {
  private final Player player;
  private final Weapon weapon;
  private final Square square;

  public PlayerCollectWeaponEvent(Player player, Weapon weapon,
      Square square) {
    this.player = player;
    this.weapon = weapon;
    this.square = square;
  }

  @Override
  public String getEventName() {
    return "PlayerCollectWeapon";
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Square getSquare() {
    return square;
  }
}
