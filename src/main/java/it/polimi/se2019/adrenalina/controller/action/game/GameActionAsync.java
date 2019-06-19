package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Player;

/**
 * Automatic game action that does not require user interaction.
 */
public abstract class GameActionAsync extends GameAction {

  protected GameActionAsync(TurnController turnController, Player player) {
    super(turnController, player);
  }

  protected GameActionAsync(Player player) {
    super(player);
  }

  @Override
  public boolean isSync() {
    return false;
  }
}
