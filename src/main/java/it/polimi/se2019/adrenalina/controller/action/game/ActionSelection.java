package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ActionSelection extends GameAction {

  public ActionSelection(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    List<TurnAction> actions = new ArrayList<>();

    if (board.isFinalFrenzyActive()) {
      actions.addAll(finalFrenzyActions(board));
    } else {
      actions.add(TurnAction.RUN);
      if (getPlayer().getDamages().size() >= 3) {
        actions.add(TurnAction.WALK_FETCH3);
      } else {
        actions.add(TurnAction.WALK_FETCH);
      }
      if (getPlayer().hasLoadedWeapons()) {
        if (getPlayer().getDamages().size() >= 6) {
          actions.add(TurnAction.SHOOT6);
        } else {
          actions.add(TurnAction.SHOOT);
        }
      }
    }

    try {
      getPlayer().getClient().getPlayerDashboardsView().showTurnActionSelection(actions);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  private List<TurnAction> finalFrenzyActions(Board board) {
    List<TurnAction> actions = new ArrayList<>();

    int ffActivatorIndex = 0;
    try {
      ffActivatorIndex = board.getPlayers()
          .indexOf(board.getPlayerByColor(board.getFinalFrenzyActivator()));
    } catch (InvalidPlayerException e) {
      // Shouldn't happen
      Log.critical("Player doesn't exists anymore!");
    }

    if (board.getPlayers().indexOf(getPlayer()) > ffActivatorIndex) {
      actions.add(TurnAction.FF_WALK_FETCH);
      if (getPlayer().hasLoadedWeapons()) {
        actions.add(TurnAction.FF_WALK_RELOAD_SHOOT);
      }
    } else {
      actions.add(TurnAction.FF_RUN);
      actions.add(TurnAction.FF_RUN_FETCH);
      if (getPlayer().hasLoadedWeapons()) {
        actions.add(TurnAction.FF_RUN_RELOAD_SHOOT);
      }
    }

    return actions;
  }
}
