package it.polimi.se2019.adrenalina.controller.action;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.io.Serializable;

public interface Action extends Serializable {
  ActionType getActionType();

  void execute(Board board, Weapon weapon);

  String serialize();

  boolean equals(Object object);

  int hashCode();
}
