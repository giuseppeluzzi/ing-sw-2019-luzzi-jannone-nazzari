package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public abstract class GameAction {

  private final Player player;

  protected GameAction(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

  public abstract void execute(Board board);

  public boolean isSync() {
    return true;
  }
}
