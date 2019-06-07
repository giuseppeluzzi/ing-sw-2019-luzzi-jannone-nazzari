package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PowerUpSelection extends GameAction {

  private final boolean discard;
  private final boolean attack;

  public PowerUpSelection(TurnController turnController, Player player, boolean discard, boolean attack) {
    super(turnController, player);
    this.discard = discard;
    this.attack = attack;
  }

  public boolean isDiscard() {
    return discard;
  }

  public boolean isAttack() {
    return attack;
  }

  private List<PowerUp> getTargetingScopes() {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() == PowerUpType.TARGETING_SCOPE) {
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
  }

  private List<PowerUp> getNotAttackPowerUps(Board board) {
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

  private int getPlayingPlayers(List<Player> players) {
    int result = 0;
    for (Player player : players) {
      if (player.getStatus() == PlayerStatus.PLAYING) {
        result++;
      }
    }
    return result;
  }

  private List<PowerUp> getGranades() {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() == PowerUpType.TAGBACK_GRANADE) {
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
  }

  private List<PowerUp> getValidPowerUps(Board board, boolean attacking) {
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
    try {
      if (discard) {
        getPlayer().getClient().getPlayerDashboardsView().showPowerUpSelection(getPlayer()
            .getPowerUps(), discard);
      } else {
        List<PowerUp> validPowerUps = getValidPowerUps(board, attack);
        if (validPowerUps.isEmpty()) {
          getTurnController().executeGameActionQueue();
        } else {
          getPlayer().getClient().getPlayerDashboardsView()
              .showPowerUpSelection(validPowerUps, discard);
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
