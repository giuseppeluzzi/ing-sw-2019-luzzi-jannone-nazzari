package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;

/**
 * Action used to print the final ranks and terminate the game.
 */
public class EndGame extends GameActionAsync {

  public EndGame() {
    super(null);
  }

  @Override
  public void execute(Board board) {
    for (Player player : board.getPlayers()) {
      try {
        if (player.isDead()) {
          // if a player died during the last turn
          player.assignPoints();
        }
        if (player.getClient() != null) {
          player.getClient().showGameMessage("Partita terminata ciao");
          player.getClient().getBoardView().showFinalRanks();
          player.getClient().disconnect("");
        }
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }
}
