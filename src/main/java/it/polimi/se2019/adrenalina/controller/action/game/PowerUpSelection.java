package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerController;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Action used to choose the power up to use.
 */
public class PowerUpSelection extends GameAction {

  private final boolean discard;
  private final boolean attack;
  private final Target target;

  public PowerUpSelection(TurnController turnController, Player player, Target target, boolean discard, boolean attack) {
    super(turnController, player);
    this.discard = discard;
    this.attack = attack;
    this.target = target;
  }

  /**
   * Specifies whether the power up will be discarded for (re)spawning.
   * @return true if the power up will be discarded, false otherwise.
   */
  public boolean isDiscard() {
    return discard;
  }

  /**
   * Specifies whether the power up attacks other players.
   * @return true if the power up attacks other players, false otherwise.
   */
  public boolean isAttack() {
    return attack;
  }

  List<PowerUp> getTargetingScopes() {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() == PowerUpType.TARGETING_SCOPE) {
        powerUp.setTargetHistory(1, target);
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
  }

  List<PowerUp> getNotAttackPowerUps(Board board) {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() != PowerUpType.TAGBACK_GRANADE
      && powerUp.getType() != PowerUpType.TARGETING_SCOPE) {
        if (powerUp.getType() == PowerUpType.NEWTON && getPlayingPlayers(board.getPlayers()) < 2) {
          continue;
        }
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
  }

  int getPlayingPlayers(List<Player> players) {
    int result = 0;
    for (Player player : players) {
      if (player.getStatus() == PlayerStatus.PLAYING) {
        result++;
      }
    }
    return result;
  }

  List<PowerUp> getGranades() {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() == PowerUpType.TAGBACK_GRANADE) {
        powerUp.setTargetHistory(1, getPlayer());
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
  }

  List<PowerUp> getValidPowerUps(Board board, boolean attacking) {
    List<PowerUp> powerUps = new ArrayList<>();
    if (board.getCurrentPlayer() == getPlayer().getColor()) {
      if (attacking) {
        powerUps.addAll(getTargetingScopes());
      } else {
        powerUps.addAll(getNotAttackPowerUps(board));
      }
    } else {
      powerUps.addAll(getGranades());
    }

    return powerUps;
  }

  @Override
  public void execute(Board board) {

    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      powerUp.reset();
    }

    try {

      String targetMessage = null;

      if (target != null) {
        targetMessage = String.format("%s%s%s",
            target.getAnsiColor(),
            target.getName(),
            ANSIColor.RESET);
      }

      if (discard) {
        handlePowerUpDiscard(board);
      } else {
        List<PowerUp> validPowerUps = getValidPowerUps(board, attack);
        if (isSync()) {

          PlayerController.sendMessageAllClients(getPlayer(), String.format(
              "%s%s%s sta scegliendo quale potenziamento usare",
              getPlayer().getColor().getAnsiColor(),
              getPlayer().getName(),
              ANSIColor.RESET), board);

          getPlayer().getClient().getPlayerDashboardsView()
              .showPowerUpSelection(targetMessage, validPowerUps, discard);
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  private void handlePowerUpDiscard(Board board) throws RemoteException {
    PlayerController.sendMessageAllClients(getPlayer(), String.format(
        "%s%s%s sta scegliendo quale potenziamento scartare",
        getPlayer().getColor().getAnsiColor(),
        getPlayer().getName(),
        ANSIColor.RESET), board);

    getPlayer().getClient().getPlayerDashboardsView().showPowerUpSelection(null, getPlayer()
            .getPowerUps(), discard);
  }

  @Override
  public void handleTimeout() {
    if (discard) {
      int randomPowerUpIndex = new Random().nextInt(getPlayer().getPowerUps().size());
      PowerUp powerUp = getPlayer().getPowerUps().get(randomPowerUpIndex);
      getTurnController().getBoardController().getPlayerController().spawnPlayer(getPlayer(), powerUp);
    }
  }

  @Override
  public boolean isSync() {
    if (discard) {
      return true;
    }
    return !getValidPowerUps(getTurnController().getBoardController().getBoard(), attack).isEmpty();
  }
}
