package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.ReloadWeapon;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectSquareEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller in charge of handling attacks.
 */
public class AttackController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = -5414473871887210992L;
  private final BoardController boardController;

  private final Set<EventType> registeredEvents = new HashSet<>();

  public AttackController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;

    registeredEvents.add(EventType.PLAYER_RELOAD_EVENT);
    registeredEvents.add(EventType.SELECT_PLAYER_EVENT);
    registeredEvents.add(EventType.SELECT_SQUARE_EVENT);
    registeredEvents.add(EventType.SQUARE_MOVE_SELECTION_EVENT);
    registeredEvents.add(EventType.SELECT_DIRECTION_EVENT);
    registeredEvents.add(EventType.SPAWN_POINT_DAMAGE_EVENT);
  }

  /**
   * Event fired when a player reloads a weapon.
   * @param event event specifying the weapon reloaded
   */
  public void update(PlayerReloadEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    List<Weapon> weapons = player.getUnloadedWeapons();

    for (Weapon currWeapon : weapons) {
      if (!player.canReload(currWeapon)) {
        weapons.remove(currWeapon);
      }
    }

    if (event.getWeaponName() != null && !weapons.isEmpty()) {
      Weapon weapon = boardController.getBoard().getWeaponByName(event.getWeaponName());

      if (player.hasWeapon(weapon) && player.canReload(weapon)) {
        for (AmmoColor color : AmmoColor.getValidColor()) {
          player.addAmmo(color, -weapon.getCost(color));
        }
        player.addAmmo(weapon.getBaseCost(), -1);
      }
      try {
        weapons.remove(weapon);

        player.getClient().getPlayerDashboardsView().showReloadWeaponSelection(weapons);
      } catch (RemoteException e) {
        Log.exception(e);
      }
      boardController.getTurnController().addTurnActions(new ReloadWeapon(player, weapon));
    } else {

      boardController.getTurnController().executeGameActionQueue();
    }

  }

  /**
   * Event fired when a player select another player.
   * @param event event specifying selected target
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
    ExecutableObject executableObject = player.getCurrentExecutable();
    executableObject.setTargetHistory(executableObject.getCurrentSelectTargetSlot(), target);
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event fired when a player selects a square.
   * @param event event specifying selected target
   */
  public void update(SelectSquareEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }

    Square square = boardController.getBoard().getSquare(event.getSquareX(), event.getSquareY());
    ExecutableObject executableObject = player.getCurrentExecutable();
    executableObject.setTargetHistory(executableObject.getCurrentSelectTargetSlot(), square);
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event fired when a player selects a direction.
   * @param event event specifying selected direction
   */
  public void update(SelectDirectionEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    player.getCurrentExecutable().setLastUsageDirection(event.getSelectedDirection());
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event fired when a player moves to a different square.
   * @param event event specifying selected square
   */
  public void update(SquareMoveSelectionEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    player.setSquare(boardController.getBoard().getSquare(event.getSquareX(), event.getSquareY()));

    boardController.getTurnController().executeGameActionQueue();
  }


  /**
   * Event fired when a player decides a spawnpoint in domination mode.
   * @param event event specifying the spawnpoint color
   */
  public void update(SpawnPointDamageEvent event) {
    if (boardController.getBoard().isDominationBoard()) {
      ((DominationBoard) boardController.getBoard())
          .addDamage(event.getAmmoColor(), event.getPlayerColor());
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Generic event
   * @param event event received
   */
  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("AttackController", "Event received: " + event.getEventType());
      try {
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        //
      }
    }
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
