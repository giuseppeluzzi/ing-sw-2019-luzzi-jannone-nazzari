package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public class CheckRespawn extends GameActionAsync {

  private final boolean everyPlayer;

  public CheckRespawn(TurnController turnController, Player player, boolean everyPlayer) {
    super(turnController, player);
    this.everyPlayer = everyPlayer;
  }

  @Override
  public void execute(Board board) {
    if (everyPlayer) {
      for (Player player : board.getPlayers()) {
        if (player.isDead()) {
          getTurnController().addRespawn(player);
        }
      }
    } else {
      if (getPlayer().isDead()) {
        getTurnController().addRespawn(getPlayer());
      }
    }
  }
}
