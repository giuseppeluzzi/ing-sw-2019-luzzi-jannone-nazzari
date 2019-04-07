package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;

public class PlayerReloadEvent implements Event {
  private final Player player;
  private final Weapon weapon;

  public PlayerReloadEvent(Player player, Weapon weapon) {
    this.player = player;
    this.weapon = weapon;
  }

  @Override
  public String getEventName() {
    return "PlayerReload";
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() {
    return weapon;
  }
}
