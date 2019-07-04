package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;

/**
 * Action used to copy a square with a certain ordinal position to another position in the target history.
 */
public class CopySquareAction implements WeaponAction {

  private static final long serialVersionUID = 8092881078549035801L;
  private final int origin;
  private final int destination;
  private final WeaponActionType type;

  public CopySquareAction(int origin, int destination) {
    this.origin = origin;
    this.destination = destination;
    type = WeaponActionType.COPY_SQUARE;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    object.setTargetHistory(destination, object.getTargetHistory(origin).getSquare());
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static CopySquareAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, CopySquareAction.class);
  }

  public int getOrigin() {
    return origin;
  }

  public int getDestination() {
    return destination;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.COPY_SQUARE
        && ((CopySquareAction) object).origin == origin
        && ((CopySquareAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return origin + destination;
  }
}
