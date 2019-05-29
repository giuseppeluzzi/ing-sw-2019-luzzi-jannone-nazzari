package it.polimi.se2019.adrenalina.controller.action.weapon;

import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import java.io.Serializable;

public interface WeaponAction extends Serializable {

  WeaponActionType getActionType();

  void execute(Board board, ExecutableObject object) throws NoTargetsException;

  default boolean isSync() {
    return false;
  }

  String serialize();

  boolean equals(Object object);

  int hashCode();
}
