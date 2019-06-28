package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;

import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if any player needs to respawn, in that case it allows them to do so.
 */
public class CheckRespawn extends GameAction {

  public CheckRespawn(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    for (Player player : getDeadPlayers(board)) {
      if (board.isDominationBoard() && player.getDamages().size() == Constants.OVERKILL_DEATH) {
        try {
          board.getPlayerByColor(player.getDamages().get(Constants.OVERKILL_DEATH-1))
              .getClient().getBoardView().showSpawnPointTrackSelection(((DominationBoard) board).getSpawnPointDamages());
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignore) {
          //
        }
      }
      getTurnController().addRespawn(player);
      getTurnController().executeGameActionQueue();
    }
  }

  static List<Player> getDeadPlayers(Board board) {
    List<Player> output = new ArrayList<>();
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        player.setStatus(PlayerStatus.WAITING);
        output.add(player);
      }
    }
    return output;
  }

  @Override
  public boolean isSync() {
    return getTurnController().getBoardController().getBoard().existsOverkilledPlayer();
  }
}
