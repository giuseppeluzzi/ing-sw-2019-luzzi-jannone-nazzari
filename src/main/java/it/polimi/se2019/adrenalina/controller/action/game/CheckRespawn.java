package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if any player needs to respawn, in that case it allows them to do so.
 */
public class CheckRespawn extends GameActionAsync {

  public CheckRespawn(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    for (Player player : getDeadPlayers(board)) {
      getTurnController().addRespawn(player);
    }
  }

  static List<Player> getDeadPlayers(Board board) {
    List<Player> output = new ArrayList<>();
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        output.add(player);
      }
    }
    return output;
  }
}
