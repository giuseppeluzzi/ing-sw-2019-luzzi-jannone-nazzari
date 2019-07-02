package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.ServerConfig;
import it.polimi.se2019.adrenalina.controller.PlayerController;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;

import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
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

    if (board.isDominationBoard() && getPlayer().getSquare().isSpawnPoint()
        && getPlayer().getSquare().getColor().getEquivalentAmmoColor() != null) {

      getPlayer().addDamages(getPlayer().getColor(), 1, false);

      addDominationDamages(getPlayer(), board);
    }

    for (Player player : getDeadPlayers(board)) {
      PlayerController.sendMessageAllClients(player,
          String.format("%s%s%s è morto", player.getColor().getAnsiColor(), player.getName(),
          ANSIColor.RESET), board);
      if (player.getStatus() != PlayerStatus.DISCONNECTED
          && player.getStatus() != PlayerStatus.SUSPENDED) {
        getTurnController().addRespawn(player);
      }

      if (getTurnController().getBoardController().getBoard().isDominationBoard()
          && getTurnController().getBoardController().getBoard().existsOverKilledPlayer()) {
        try {
          getTurnController().addTurnActions(
              new SpawnPointTrackSelection(board.getPlayerByColor(
                  player.getDamages().get(ServerConfig.getInstance().getDeathDamages() - 1))));
        } catch (InvalidPlayerException ignore) {
          Log.debug("Invalid player exception thrown");
        }
      }

      player.setStatus(PlayerStatus.PLAYING);
      player.assignPoints(); // Assign points and clear his status
    }
  }

  /**
   * Adds damages for players who stay alone on a spawnPoint in domination mode.
   * @param currentPlayer the considered player
   */
  private void addDominationDamages(Player currentPlayer, Board board) {
    if (currentPlayer.getSquare().getPlayers().size() == 1) {
      ((DominationBoard) board)
          .addDamage(currentPlayer.getSquare().getColor().getEquivalentAmmoColor(),
              currentPlayer.getColor());
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
}
