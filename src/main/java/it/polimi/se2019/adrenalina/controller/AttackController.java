package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.CheckReloadWeapons;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectSquareEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SkipSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.model.WeaponReload;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
    // N.B. Sonar incorrectly reports the "throws" clause of this method as redoundant, but it's not.

    this.boardController = boardController;
    registeredEvents.add(EventType.PLAYER_RELOAD_EVENT);
    registeredEvents.add(EventType.SELECT_PLAYER_EVENT);
    registeredEvents.add(EventType.SKIP_SELECTION_EVENT);
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

    if (event.getWeaponName() != null) {
      Weapon realoadingWeapon = boardController.getBoard().getWeaponByName(event.getWeaponName());
      List<Weapon> weapons = getReloadableWeapons(player, realoadingWeapon);

      if (weapons.isEmpty()) {
        boardController.getTurnController()
            .addTurnActions(new Payment(boardController.getTurnController(), player,
                new WeaponReload(realoadingWeapon)));
      } else {
        boardController.getTurnController()
            .addTurnActions(new Payment(boardController.getTurnController(), player,
                    new WeaponReload(realoadingWeapon)),
                new CheckReloadWeapons(boardController.getTurnController(), player));
      }
    }
    boardController.getTurnController().executeGameActionQueue();
  }

  public List<Weapon> getReloadableWeapons(Player player, Weapon realoadingWeapon) {
    List<Weapon> weapons = player.getUnloadedWeapons();
    weapons.remove(realoadingWeapon);
    for (Weapon currWeapon : new ArrayList<>(weapons)) {
      if (!player.canReload(currWeapon)) {
        weapons.remove(currWeapon);
      }
    }
    return weapons;
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
   * Event fired when a player skips the selection of a taget
   * @param event received event
   */
  public void update(SkipSelectionEvent event) {
    Player player;
    try {
      player = boardController.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      return;
    }
    ExecutableObject executableObject = player.getCurrentExecutable();
    executableObject.setSkipUntilSelect(true);
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
        Observer.invokeEventHandler(this, event);
      } catch (RemoteException e) {
        Log.exception(e);
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
