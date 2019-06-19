package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used when a player needs to choose what to do next during his turn.
 */
public class ActionSelection extends GameAction {

  public ActionSelection(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    getPlayer().setCurrentExecutable(null);
    getPlayer().setCurrentBuying(null);
    List<TurnAction> turnActions = null;

    for (Weapon weapon : getPlayer().getWeapons()) {
      weapon.reset();
    }

    for (PowerUp powerUp : getPlayer().getPowerUps()) {
      powerUp.reset();
    }

    if (board.isFinalFrenzyActive()) {
      int playerIndex = board.getPlayers().indexOf(getPlayer());
      int finalFrenzyActivator;

      try {
        finalFrenzyActivator = board.getPlayers()
            .indexOf(board.getPlayerByColor(board.getFinalFrenzyActivator()));
      } catch (InvalidPlayerException e) {
        // Shouldn't happen
        Log.critical("Player doesn't exists anymore!");
        return;
      }

      turnActions = finalFrenzyTurnActions(playerIndex, getPlayer().hasLoadedWeapons(),
          finalFrenzyActivator);
    } else {
      turnActions = standardTurnActions(getPlayer().hasLoadedWeapons(),
          getPlayer().getDamages().size());
    }

    try {
      getPlayer().getClient().getPlayerDashboardsView().showTurnActionSelection(turnActions);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  static List<TurnAction> standardTurnActions(boolean playerHasLoadedWeapons,
      int playerDamages) {
    List<TurnAction> actions = new ArrayList<>();

    actions.add(TurnAction.RUN);
    if (playerDamages >= 3) {
      actions.add(TurnAction.WALK_FETCH3);
    } else {
      actions.add(TurnAction.WALK_FETCH);
    }
    if (playerHasLoadedWeapons) {
      if (playerDamages >= 6) {
        actions.add(TurnAction.SHOOT6);
      } else {
        actions.add(TurnAction.SHOOT);
      }
    }

    return actions;
  }

  static List<TurnAction> finalFrenzyTurnActions(int playerIndex,
      boolean playerHasLoadedWeapons, int finalFrenzyActivatorIndex) {
    List<TurnAction> actions = new ArrayList<>();

    if (playerIndex > finalFrenzyActivatorIndex) {
      actions.add(TurnAction.FF_WALK_FETCH);
      if (playerHasLoadedWeapons) {
        actions.add(TurnAction.FF_WALK_RELOAD_SHOOT);
      }
    } else {
      actions.add(TurnAction.FF_RUN);
      actions.add(TurnAction.FF_RUN_FETCH);
      if (playerHasLoadedWeapons) {
        actions.add(TurnAction.FF_RUN_RELOAD_SHOOT);
      }
    }

    return actions;
  }
}
