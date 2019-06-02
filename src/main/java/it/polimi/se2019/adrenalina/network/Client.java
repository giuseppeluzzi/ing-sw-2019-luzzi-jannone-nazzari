package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.Serializable;

public abstract class Client implements ClientInterface, Serializable {

  private static final long serialVersionUID = -8182516938240148955L;
  private final String playerName;
  private PlayerColor playerColor;
  private boolean domination;
  private Long lastPing;

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

  @Override
  public void ping() {
    lastPing = System.currentTimeMillis();
  }

  @Override
  public Long getLastPing() {
    return lastPing;
  }

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    switch (severity) {
      case GAME:
        Log.println(message);
        break;
      default:
        if (!title.isEmpty()) {
          Log.println(String.format("%s: %s", severity, title));
        }
        Log.println(String.format("%s: %s", severity, message));
        break;
    }
  }

  @Override
  public void showMessage(MessageSeverity severity, String message) {
    showMessage(severity, "", message);
  }

  @Override
  public void showGameMessage(String message) {
    showMessage(MessageSeverity.GAME, "", message);
  }
}
