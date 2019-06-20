package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootSquareAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsExceptionOptional;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Action used to execute executable objects.
 */
public class ExecutableEffect extends GameAction {

  private final ExecutableObject executableObject;
  private final WeaponAction weaponAction;
  private boolean enabled;

  public ExecutableEffect(TurnController turnController, Player player,
      ExecutableObject executableObject, WeaponAction weaponAction) {
    super(turnController, player);
    this.executableObject = executableObject;
    this.weaponAction = weaponAction;
    enabled = true;
  }

  public ExecutableObject getExecutableObject() {
    return executableObject;
  }

  public WeaponAction getWeaponAction() {
    return weaponAction;
  }

  @Override
  public void execute(Board board) {
    if (isEnabled()) {
      if (!executableObject.isCancelled()) {
        try {
          Log.debug("WA: " + weaponAction.getActionType());
          List<Player> players;
          weaponAction.execute(board, executableObject);
          if (executableObject.isWeapon()) {
            switch (weaponAction.getActionType()) {
              case SHOOT:
                Target target = executableObject
                    .getTargetHistory(((ShootAction) weaponAction).getTarget());
                executableObject.setLastUsageDirection(executableObject
                    .getOwner().getSquare().getCardinalDirection(target.getSquare()));
                if (((ShootAction) weaponAction).getDamages() > 0) {
                  getTurnController().addTurnActions(
                      new PowerUpSelection(getTurnController(), getPlayer(), target,
                          false, true));
                  getTurnController().addTurnActions(
                      new PowerUpSelection(getTurnController(), target.getPlayer(),
                          null, false, false));
                }
                ((Weapon) executableObject).setLoaded(false);
                break;
              case SHOOT_SQUARE:
                if (((ShootAction) weaponAction).getDamages() > 0) {
                  players = ((ShootSquareAction) weaponAction).getPlayers(board, executableObject);
                  players.remove(executableObject.getOwner());
                  for (Player player : players) {
                    getTurnController().addTurnActions(
                        new PowerUpSelection(getTurnController(), getPlayer(),
                            player, false, true));
                    getTurnController().addTurnActions(
                        new PowerUpSelection(getTurnController(), player,
                            getPlayer(), false, false));
                  }
                }
                ((Weapon) executableObject).setLoaded(false);
                break;
              case SHOOT_ROOM:
                if (((ShootAction) weaponAction).getDamages() > 0) {
                  players = ((ShootRoomAction) weaponAction).getPlayers(board, executableObject);
                  players.remove(executableObject.getOwner());
                  for (Player player : players) {
                    getTurnController().addTurnActions(
                        new PowerUpSelection(getTurnController(), getPlayer(), player,
                            false, true));
                    getTurnController().addTurnActions(
                        new PowerUpSelection(getTurnController(), player,
                            getPlayer(), false, false));
                  }
                }
                ((Weapon) executableObject).setLoaded(false);
                break;
            }
          }
        } catch (NoTargetsException e) {
          if (e.isRollback()) {
            getTurnController().disableActionsUntilPowerup();
            try {
              getPlayer().getClient()
                  .showGameMessage("L'effetto scelto non è utilizzabile, scegli una nuova azione.");
            } catch (RemoteException remoteException) {
              Log.exception(remoteException);
            }
            getTurnController().addTurnActions(new MoveRollback(getTurnController(), getPlayer(),
                executableObject));
          }
          executableObject.setCancelled(true);
          ((Weapon) executableObject).setLoaded(true);
          getTurnController().executeGameActionQueue();
        } catch (InvalidSquareException ignored) {
          //
        } catch (NoTargetsExceptionOptional e) {
          getTurnController().disableActionsUntilPowerup();
          getTurnController().executeGameActionQueue();
        }
      }
    } else {
      if (isSync()) {
        getTurnController().executeGameActionQueue();
      }
    }
  }


  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}
