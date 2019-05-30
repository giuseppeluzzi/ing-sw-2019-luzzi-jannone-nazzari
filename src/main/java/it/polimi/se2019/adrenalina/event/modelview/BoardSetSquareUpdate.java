package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.Square;

public class BoardSetSquareUpdate implements Event {

  private static final long serialVersionUID = -2673936916461806650L;
  private final int posX;
  private final int posY;
  private final boolean spawnPoint;
  private final SquareColor color;
  private final BorderType edgeUp;
  private final BorderType edgeRight;
  private final BorderType edgeDown;
  private final BorderType edgeLeft;

  public BoardSetSquareUpdate(Square square) {
    posX = square.getPosX();
    posY = square.getPosY();
    color = square.getColor();
    spawnPoint = square.isSpawnPoint();
    edgeUp = square.getEdge(Direction.NORTH);
    edgeRight = square.getEdge(Direction.EAST);
    edgeLeft = square.getEdge(Direction.WEST);
    edgeDown = square.getEdge(Direction.SOUTH);
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public SquareColor getColor() {
    return color;
  }

  public boolean isSpawnPoint() {
    return spawnPoint;
  }

  public BorderType getEdgeUp() {
    return edgeUp;
  }

  public BorderType getEdgeRight() {
    return edgeRight;
  }

  public BorderType getEdgeDown() {
    return edgeDown;
  }

  public BorderType getEdgeLeft() {
    return edgeLeft;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_SET_SQUARE_UPDATE;
  }
}
