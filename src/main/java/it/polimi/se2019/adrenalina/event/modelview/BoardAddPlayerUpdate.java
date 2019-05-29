package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardAddPlayerUpdate implements Event {

  private static final long serialVersionUID = -2702366364883643012L;
  private final String playerName;
  private final PlayerColor playerColor;

  public BoardAddPlayerUpdate(String playerName,
      PlayerColor playerColor) {
    this.playerName = playerName;
    this.playerColor = playerColor;
  }

  public String getPlayerName() {
    return playerName;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_ADD_PLAYER_UPDATE;
  }
}
