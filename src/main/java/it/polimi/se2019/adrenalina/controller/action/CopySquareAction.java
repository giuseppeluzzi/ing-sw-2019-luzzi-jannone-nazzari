package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.Arrays;

public class CopySquareAction implements Action {

  private final int origin;
  private final int destination;
  private final ActionType type;

  public CopySquareAction(int origin, int destination) {
    this.origin = origin;
    this.destination = destination;
    type = ActionType.COPY_SQUARE;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, Weapon weapon) {
    // TODO: excecution
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static SelectAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, SelectAction.class);
  }

  public int getOrigin() {
    return origin;
  }

  public int getDestination() {
    return destination;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.COPY_SQUARE
        && ((CopySquareAction) object).origin == origin
        && ((CopySquareAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return origin + destination;
  }
}
