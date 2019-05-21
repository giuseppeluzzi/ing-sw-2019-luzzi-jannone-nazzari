package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Player;

public abstract class GameActionAsync extends GameAction {

  protected GameActionAsync(Player player) {
    super(player);
  }

  @Override
  public boolean isSync() {
    return false;
  }
}
