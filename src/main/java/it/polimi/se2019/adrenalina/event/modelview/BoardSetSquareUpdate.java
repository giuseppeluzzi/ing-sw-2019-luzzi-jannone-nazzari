package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Square;

public class BoardSetSquareUpdate implements Event {

  private static final long serialVersionUID = -2673936916461806650L;
  private final Square square;

  public BoardSetSquareUpdate(Square square) {
    this.square = square;
  }

  public Square getSquare() {
    return square;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_SET_SQUARE_UPDATE;
  }
}
