package it.polimi.se2019.adrenalina.model;

public class Kill {
  private final Player player;
  private final boolean overKill;

  public Kill(Player player, boolean overKill) {
    this.player = player;
    this.overKill = overKill;
  }

  public Player getPlayer() {
    return player;
  }

  public boolean isOverKill() {
    return overKill;
  }
}
