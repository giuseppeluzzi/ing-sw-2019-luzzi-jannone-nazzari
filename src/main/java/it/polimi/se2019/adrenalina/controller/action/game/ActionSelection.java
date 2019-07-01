package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerController;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
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

  public static void resetWeapons(Player player) {
    for (Weapon weapon : player.getWeapons()) {
      weapon.reset();
    }
  }

  public static void resetPowerUps(Player player) {
    for (PowerUp powerUp : player.getPowerUps()) {
      powerUp.reset();
    }
  }

  List<TurnAction> setTurnActions(Board board) {
    List<TurnAction> turnActions = null;

    if (board.isFinalFrenzyActive()) {

      int playerIndex = board.getPlayers().indexOf(getPlayer());
      int finalFrenzyActivator;

      try {
        finalFrenzyActivator = board.getPlayers()
            .indexOf(board.getPlayerByColor(board.getFinalFrenzyActivator()));
      } catch (InvalidPlayerException e) {
        // Shouldn't happen
        Log.critical("Player doesn't exists anymore!");
        return null;
      }

      turnActions = finalFrenzyTurnActions(playerIndex, getPlayer().hasLoadedWeapons(),
          finalFrenzyActivator);
    } else {
      turnActions = standardTurnActions(getPlayer().hasLoadedWeapons(),
          getPlayer().getDamages().size());
    }
    return turnActions;
  }

  @Override
  public void execute(Board board) {
    Player player = getPlayer();
    player.setCurrentExecutable(null);
    player.setCurrentBuying(null);
    List<TurnAction> turnActions;

    resetWeapons(player);
    resetPowerUps(player);
    turnActions = setTurnActions(board);


    try {
      PlayerController.sendMessageAllClients(player,  String.format("%s%s%s sta scegliendo un'azione",
          player.getColor().getAnsiColor(),
          player.getName(),
          ANSIColor.RESET), board);
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
      actions.add(TurnAction.FF_RUN);
      actions.add(TurnAction.FF_RUN_FETCH);
      if (playerHasLoadedWeapons) {
        actions.add(TurnAction.FF_RUN_RELOAD_SHOOT);
      }
    } else {
      actions.add(TurnAction.FF_WALK_FETCH);
      if (playerHasLoadedWeapons) {
        actions.add(TurnAction.FF_WALK_RELOAD_SHOOT);
      }
    }

    return actions;
  }
}
