package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public class SquareSelection extends GameAction {

  private final int maxDistance;

  public SquareSelection(Player player, int maxDistance) {
    super(player);
    this.maxDistance = maxDistance;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  @Override
  public void execute(Board board) {
    // TODO
  }
}
