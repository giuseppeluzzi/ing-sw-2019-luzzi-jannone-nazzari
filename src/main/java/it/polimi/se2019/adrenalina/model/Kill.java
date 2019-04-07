package it.polimi.se2019.adrenalina.model;

public class Kill {
  private final PlayerColor color;
  private final boolean overKill;

  public Kill(PlayerColor color, boolean overKill) {
    this.color = color;
    this.overKill = overKill;
  }

  public Kill(Kill kill) {
    color = kill.color;
    overKill = kill.overKill;
  }

  public PlayerColor getPlayerColor() {
    return color;
  }

  public boolean isOverKill() {
    return overKill;
  }
}
