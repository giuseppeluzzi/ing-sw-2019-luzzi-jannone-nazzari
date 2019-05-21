package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.controller.event.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.controller.event.SelectSquareEvent;
import it.polimi.se2019.adrenalina.controller.event.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AttackController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = -5414473871887210992L;
  private final BoardController boardController;

  public AttackController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;
  }

  /**
   * Event fired when a player uses an effect.
   * @param event event specifing the effect used
   */
  public void update(PlayerAttackEvent event) {
    Weapon weapon = boardController.getBoard().getWeaponByName(event.getWeaponName());
    try {
      boardController.getBoard().getPlayerByColor(event.getPlayerColor()).setCurrentWeapon(weapon);
    } catch (InvalidPlayerException ignored) {
      return;
    }
    weapon.executeActionQueue(boardController.getBoard());
  }

  /**
   * Event fired when a player reloads a weapon.
   * @param event event specifing the weapon reloaded
   */
  public void update(PlayerReloadEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    Weapon weapon = boardController.getBoard().getWeaponByName(event.getWeaponName());

    if (player.hasWeapon(weapon) && player.canReload(weapon)) {
      for (AmmoColor color : AmmoColor.getValidColor()) {
        player.setAmmo(color, player.getAmmo(color) - weapon.getCost(color));
      }
      player.setAmmo(weapon.getBaseCost(), player.getAmmo(weapon.getBaseCost()) - 1);
    }
  }

  /**
   * Event fired when a player select another player.
   * @param event event specifing selected target
   */
  public void update(SelectPlayerEvent event) {
    Player player;
    Player target;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
      target = boardController.getBoard().getPlayerByColor(event.getSelectedColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    Weapon weapon = player.getCurrentWeapon();
    weapon.setTargetHistory(weapon.getCurrentSelectTargetSlot(), target);
  }

  /**
   * Event fired when a player select a square.
   * @param event event specifing selected target
   */
  public void update(SelectSquareEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    Square square = boardController.getBoard().getSquare(event.getSquareX(), event.getSquareY());
    Weapon weapon = player.getCurrentWeapon();
    weapon.setTargetHistory(weapon.getCurrentSelectTargetSlot(), square);
  }

  /**
   * Event fired when a player moves to a different square.
   * @param event event specifing selected square
   */
  public void update(SquareMoveSelectionEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    player.setSquare(boardController.getBoard().getSquare(event.getSquareX(), event.getSquareY()));
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof AttackController &&
        ((AttackController) obj).boardController.equals(boardController);
  }

  @Override
  public int hashCode() {
    return boardController.hashCode();
  }
}
