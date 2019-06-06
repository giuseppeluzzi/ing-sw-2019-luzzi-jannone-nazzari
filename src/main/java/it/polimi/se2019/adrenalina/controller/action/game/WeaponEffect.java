package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootSquareAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponActionType;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;

public class WeaponEffect extends GameAction {

  private final Weapon weapon;
  private final WeaponAction weaponAction;

  public WeaponEffect(Player player, Weapon weapon, WeaponAction weaponAction) {
    super(player);
    this.weapon = weapon;
    this.weaponAction = weaponAction;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public WeaponAction getWeaponAction() {
    return weaponAction;
  }

  @Override
  public void execute(Board board) {
    if (! weapon.isCancelled()) {
      try {
        Log.debug("WA: " + weaponAction.getActionType());
        List<Player> players;
        weaponAction.execute(board, weapon);
        switch (weaponAction.getActionType()) {
          case SHOOT:
            Target target = weapon.getTargetHistory(((ShootAction) weaponAction).getTarget());
            getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), target.getPlayer(), false, false));
            break;
          case SHOOT_SQUARE:
            players = ((ShootSquareAction) weaponAction).getPlayers(board, weapon);
            for (Player player : players) {
              getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), player, false, false));
            }
            break;
          case SHOOT_ROOM:
            players = ((ShootRoomAction) weaponAction).getPlayers(board, weapon);
            for (Player player : players) {
              getTurnController().addTurnActions(new PowerUpSelection(getTurnController(), player, false, false));
            }
            break;
          default:
            break;
        }
      } catch (NoTargetsException e) {
        if (e.isRollback()) {
          try {
            getPlayer().getClient().showGameMessage("L'effetto scelto non è utilizzabile, scegli una nuova azione.");
          } catch (RemoteException remoteException) {
            Log.exception(remoteException);
          }
          getTurnController().addTurnActions(new MoveRollback(getTurnController(), getPlayer(), weapon));
        }
        weapon.setCancelled();
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
