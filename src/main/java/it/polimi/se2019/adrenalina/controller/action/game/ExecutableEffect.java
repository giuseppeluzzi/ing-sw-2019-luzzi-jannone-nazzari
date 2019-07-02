package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootSquareAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponActionType;
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

  public ExecutableEffect(TurnController turnController, Player player,
      ExecutableObject executableObject, WeaponAction weaponAction) {
    super(turnController, player);
    this.executableObject = executableObject;
    this.weaponAction = weaponAction;
  }

  public ExecutableObject getExecutableObject() {
    return executableObject;
  }

  public WeaponAction getWeaponAction() {
    return weaponAction;
  }

  @Override
  public void execute(Board board) {
    if (executableObject.skipUntilSelect() &&
        (weaponAction.getActionType() == WeaponActionType.SELECT
            || weaponAction.getActionType() == WeaponActionType.SELECT_DIRECTION)) {
      executableObject.setSkipUntilSelect(false);
    }
    if (isEnabled() && ! executableObject.skipUntilSelect()) {
      if (!executableObject.isCancelled()) {
        runAction(board);
      }
    } else {
      if (isSync()) {
        getTurnController().executeGameActionQueue();
      }
    }
  }

  void runAction(Board board) {
    try {
      Log.debug("WA: " + weaponAction.getActionType());
      weaponAction.execute(board, executableObject);
      if (executableObject.isWeapon()) {
        handlePowerUps(board);
      }
    } catch (NoTargetsException e) {
      handleNoTargets(e.isRollback());
    } catch (InvalidSquareException ignored) {
      //
    } catch (NoTargetsExceptionOptional e) {
      getTurnController().disableActionsUntilPowerup();
      getTurnController().executeGameActionQueue();
    }
  }

  void handlePowerUps(Board board) throws InvalidSquareException {
    switch (weaponAction.getActionType()) {
      case SHOOT:
        handleNormalShoot();
        break;
      case SHOOT_SQUARE:
        handleSquareShoot(board);
        break;
      case SHOOT_ROOM:
        handleRoomShoot(board);
        break;
      default:
    }
  }

  private void handleRoomShoot(Board board) {
    if (((ShootAction) weaponAction).getDamages() > 0) {
      List<Player> roomPlayers = ((ShootRoomAction) weaponAction).getPlayers(board, executableObject);
      roomPlayers.remove(executableObject.getOwner());
      for (Player player : roomPlayers) {
        if (player.getStatus() != PlayerStatus.DISCONNECTED
            && player.getStatus() != PlayerStatus.SUSPENDED) {
          getTurnController().addTurnActions(
              new PowerUpSelection(getTurnController(), getPlayer(), player,
                  false, true));
          getTurnController().addTurnActions(
              new PowerUpSelection(getTurnController(), player,
                  getPlayer(), false, false));
        }
      }
      if (board.isDominationBoard()) {
        getTurnController().addTurnActions(
            new PowerUpSelection(getTurnController(), getPlayer(),
                board.getSpawnPointSquare(executableObject
                    .getTargetHistory(((ShootRoomAction) weaponAction)
                        .getTarget()).getSquare().getColor()
                    .getEquivalentAmmoColor()), false, true));
      }
    }
    ((Weapon) executableObject).setLoaded(false);
  }

  private void handleSquareShoot(Board board) {
    if (((ShootAction) weaponAction).getDamages() > 0) {

      List<Player> squarePlayers = ((ShootSquareAction) weaponAction).getPlayers(board, executableObject);
      squarePlayers.remove(executableObject.getOwner());

      for (Player player : squarePlayers) {
        if (player.getStatus() != PlayerStatus.DISCONNECTED
            && player.getStatus() != PlayerStatus.SUSPENDED) {
          getTurnController().addTurnActions(
              new PowerUpSelection(getTurnController(), getPlayer(),
                  player, false, true));
          getTurnController().addTurnActions(
              new PowerUpSelection(getTurnController(), player,
                  getPlayer(), false, false));
        }
      }

      if (board.isDominationBoard() && getPlayer().getSquare().isSpawnPoint()) {
        getTurnController().addTurnActions(
            new PowerUpSelection(getTurnController(), getPlayer(),
                getPlayer().getSquare(), false, true));
      }
    }
    ((Weapon) executableObject).setLoaded(false);
  }

  private void handleNormalShoot() throws InvalidSquareException {
    Target target = executableObject
            .getTargetHistory(((ShootAction) weaponAction).getTarget());
    try {
      executableObject.setLastUsageDirection(executableObject
              .getOwner().getSquare().getCardinalDirection(target.getSquare()));
    } catch (InvalidSquareException e) {
      executableObject.setLastUsageDirection(null);
    }

    if (((ShootAction) weaponAction).getDamages() > 0) {
      getTurnController().addTurnActions(
              new PowerUpSelection(getTurnController(), getPlayer(), target,
                      false, true));
      if (target.isPlayer()) {
        getTurnController().addTurnActions(
                new PowerUpSelection(getTurnController(), target.getPlayer(),
                        null, false, false));
      }
    }
    ((Weapon) executableObject).setLoaded(false);
  }

  private void handleNoTargets(boolean isRollback) {
    if (isRollback) {
      getTurnController().disableActionsUntilPowerup();
      try {
        getPlayer().getClient()
                .showGameMessage("L'effetto scelto non è utilizzabile, scegli una nuova azione."
                );
      } catch (RemoteException remoteException) {
        Log.exception(remoteException);
      }
      getTurnController().addTurnActions(new MoveRollback(getTurnController(), getPlayer(),
              executableObject));
    }
    executableObject.setCancelled(true);
    ((Weapon) executableObject).setLoaded(true);
    getTurnController().executeGameActionQueue();
  }

  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}
