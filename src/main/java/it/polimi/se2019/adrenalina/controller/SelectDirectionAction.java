package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Target;
import java.util.ArrayList;
import java.util.List;

public class SelectDirectionAction implements Action {
  private final ActionType type;

  public SelectDirectionAction() {
    type = ActionType.SELECT_DIRECTION;
  }

  @Override
  public ActionType getActionType() {
    return type;
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
    return object instanceof Action &&
        ((Action) object).getActionType() == ActionType.SELECT_DIRECTION;
  }

  @Override
  public int hashCode() {
    return type.ordinal();
  }
}
