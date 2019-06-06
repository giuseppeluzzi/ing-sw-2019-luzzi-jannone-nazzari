package it.polimi.se2019.adrenalina.controller.action.game;

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

  private List<PowerUp> getNotAttackPowerUps() {
    List<PowerUp> powerUpList = new ArrayList<>();
    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      if (powerUp.getType() != PowerUpType.TAGBACK_GRANADE
      && powerUp.getType() != PowerUpType.TARGETING_SCOPE) {
        powerUpList.add(powerUp);
      }
    }
    return powerUpList;
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
        powerUps.addAll(getNotAttackPowerUps());
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
        if (! validPowerUps.isEmpty()) {
          getPlayer().getClient().getPlayerDashboardsView()
              .showPowerUpSelection(validPowerUps, discard);
        } else {
          getTurnController().executeGameActionQueue();
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
