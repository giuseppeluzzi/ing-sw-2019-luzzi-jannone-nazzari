package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardPlayerConnectUpdate implements Event {

  private static final long serialVersionUID = -4856929399123717330L;
  private final String playerName;
  private final PlayerColor playerColor;
  private final int numPlayers;

  public BoardPlayerConnectUpdate(String playerName,
      PlayerColor playerColor, int numPlayers) {
    this.playerName = playerName;
    this.playerColor = playerColor;
    this.numPlayers = numPlayers;
  }

  public String getPlayerName() {
    return playerName;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_ADD_PLAYER_UPDATE;
  }
}
