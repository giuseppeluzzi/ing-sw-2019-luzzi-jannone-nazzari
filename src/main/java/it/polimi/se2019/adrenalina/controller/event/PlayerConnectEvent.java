package it.polimi.se2019.adrenalina.controller.event;

public class PlayerConnectEvent implements Event {

  private static final long serialVersionUID = -6091901271781502467L;
  private final String playerName;
  private final boolean domination;

  public PlayerConnectEvent(String playerName, boolean domination) {
    this.playerName = playerName;
    this.domination = domination;
  }

  public String getPlayerName() {
    return playerName;
  }

  public boolean isDomination() {
    return domination;
  }
}
