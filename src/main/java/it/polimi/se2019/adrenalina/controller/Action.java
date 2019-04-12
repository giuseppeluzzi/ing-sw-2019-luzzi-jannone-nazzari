package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.ActionType;

public interface Action {
  ActionType getActionType();

  String serialize();

  Action deserialize(String json);
}
