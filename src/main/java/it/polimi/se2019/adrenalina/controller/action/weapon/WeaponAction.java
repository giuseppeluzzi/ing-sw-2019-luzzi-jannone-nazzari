package it.polimi.se2019.adrenalina.controller.action.weapon;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.io.Serializable;

public interface WeaponAction extends Serializable {

  WeaponActionType getActionType();

  void execute(Board board, ExecutableObject object);

  default boolean isSync() {
    return false;
  }

  String serialize();

  boolean equals(Object object);

  int hashCode();
}
