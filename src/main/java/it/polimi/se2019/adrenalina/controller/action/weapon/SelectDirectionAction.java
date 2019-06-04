package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

public class SelectDirectionAction implements WeaponAction {

  private static final long serialVersionUID = 9176126026908579498L;
  private final WeaponActionType type;

  public SelectDirectionAction() {
    type = WeaponActionType.SELECT_DIRECTION;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    try {
      object.getOwner().getClient().getBoardView().showDirectionSelect();
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public boolean isSync() {
    return true;
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static SelectDirectionAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, SelectDirectionAction.class);
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction &&
        ((WeaponAction) object).getActionType() == WeaponActionType.SELECT_DIRECTION;
  }

  @Override
  public int hashCode() {
    return type.ordinal();
  }
}
