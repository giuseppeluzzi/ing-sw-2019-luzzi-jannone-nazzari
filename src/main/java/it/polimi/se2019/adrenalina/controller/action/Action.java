package it.polimi.se2019.adrenalina.controller.action;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Weapon;

public interface Action {
  ActionType getActionType();

  void execute(Board board, Weapon weapon);

  String serialize();

  boolean equals(Object object);

  int hashCode();
}
