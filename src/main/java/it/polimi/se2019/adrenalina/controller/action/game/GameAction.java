package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public abstract class GameAction {

  private final TurnController turnController;
  private final Player player;

  protected GameAction(Player player) {
    this(null, player);
  }

  protected GameAction(TurnController turnController, Player player) {
    this.turnController = turnController;
    this.player = player;
  }

  public TurnController getTurnController() {
    return turnController;
  }

  public Player getPlayer() {
    return player;
  }

  public abstract void execute(Board board);

  public boolean isSync() {
    return true;
  }
}
