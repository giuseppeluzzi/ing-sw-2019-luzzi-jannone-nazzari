package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.io.Serializable;

public abstract class Client implements ClientInterface, Serializable {

  private static final long serialVersionUID = -8182516938240148955L;
  private final String playerName;
  private PlayerColor playerColor;
  private boolean domination;

  protected Client(String playerName, boolean domination) {
    this.playerName = playerName;
    this.domination = domination;
  }

  @Override
  public String getName() {
    return playerName;
  }

  @Override
  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void setPlayerColor(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void setDomination(boolean domination) {
    this.domination = domination;
  }
}
