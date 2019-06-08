package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootSquareAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;

public class ExecutableEffect extends GameAction {

  private final ExecutableObject executableObject;
  private final WeaponAction weaponAction;

  public ExecutableEffect(Player player, ExecutableObject executableObject, WeaponAction weaponAction) {
    super(player);
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
    if (! executableObject.isCancelled()) {
      try {
        Log.debug("WA: " + weaponAction.getActionType());
        List<Player> players;
        weaponAction.execute(board, executableObject);
        switch (weaponAction.getActionType()) {
          case SHOOT:
            Target target = executableObject.getTargetHistory(((ShootAction) weaponAction).getTarget());
            getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), target.getPlayer(), false, false));
            break;
          case SHOOT_SQUARE:
            players = ((ShootSquareAction) weaponAction).getPlayers(board, executableObject);
            for (Player player : players) {
              getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), player, false, false));
            }
            break;
          case SHOOT_ROOM:
            players = ((ShootRoomAction) weaponAction).getPlayers(board, executableObject);
            for (Player player : players) {
              getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), player, false, false));
            }
            break;
        }
      } catch (NoTargetsException e) {
        if (e.isRollback()) {
          try {
            getPlayer().getClient().showGameMessage("L'effetto scelto non è utilizzabile, scegli una nuova azione.");
          } catch (RemoteException remoteException) {
            Log.exception(remoteException);
          }
          getTurnController().addTurnActions(new MoveRollback(getTurnController(), getPlayer(),
              executableObject));
        }
        executableObject.setCancelled(true);
      } catch (InvalidSquareException ignored) {
        //
      }
    }
  }

  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}