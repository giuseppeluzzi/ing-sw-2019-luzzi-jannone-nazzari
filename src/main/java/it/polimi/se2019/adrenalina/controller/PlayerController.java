package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.CheckReloadWeapons;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.ObjectPickup;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.controller.action.game.PowerUpEffect;
import it.polimi.se2019.adrenalina.controller.action.game.SelectEffect;
import it.polimi.se2019.adrenalina.controller.action.game.SelectWeapon;
import it.polimi.se2019.adrenalina.controller.action.game.SquareSelection;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerNoCollectEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = 5970759489567761611L;

  private final BoardController boardController;

  private final Set<EventType> registeredEvents = new HashSet<>();

  PlayerController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;

    registeredEvents.add(EventType.PLAYER_NO_COLLECT_EVENT);
    registeredEvents.add(EventType.PLAYER_COLLECT_AMMO_EVENT);
    registeredEvents.add(EventType.PLAYER_COLLECT_WEAPON_EVENT);
    registeredEvents.add(EventType.PLAYER_POWERUP_EVENT);
    registeredEvents.add(EventType.PLAYER_ACTION_SELECTION_EVENT);
    registeredEvents.add(EventType.PLAYER_DISCARD_POWERUP_EVENT);
    registeredEvents.add(EventType.PLAYER_PAYMENT_EVENT);
    registeredEvents.add(EventType.PLAYER_SELECT_WEAPON_EVENT);
    registeredEvents.add(EventType.PLAYER_SELECT_WEAPON_EFFECT_EVENT);
  }

  private Player getPlayerFromBoard(Board board, PlayerColor playerColor) {
    Player player;
    try {
      player = board.getPlayerByColor(playerColor);
    } catch (InvalidPlayerException ignored) {
      return null;
    }
    return player;
  }

  /**
   * Instances a new player with a name and color and adds it to the board.
   *
   * @param name the player's name.
   * @param color the player's color.
   * @return the player instance.
   * @throws IllegalArgumentException thrown if the name is already used by another player in this
   * board.
   */
  public Player createPlayer(String name, PlayerColor color) {
    for (Player player : boardController.getBoard().getPlayers()) {
      if (player.getName().equalsIgnoreCase(name)) {
        throw new IllegalArgumentException(
            "This name is already used by another player in this board");
      }
    }
    return new Player(name, color, boardController.getBoard());
  }

  public void update(PlayerNoCollectEvent event) {
    boardController.getTurnController().executeGameActionQueue();
    event.getPlayerColor();
  }

  public void update(PlayerCollectAmmoEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }

    if (board.getSquare(event.getSquareX(), event.getSquareY()).hasAmmoCard()) {
      AmmoCard ammoCard = board.getSquare(event.getSquareX(), event.getSquareY()).getAmmoCard();
      for (AmmoColor color : AmmoColor.getValidColor()) {
        player.addAmmo(color, ammoCard.getAmmo(color));
      }
      for (int i = 0; i < ammoCard.getPowerUp(); i++) {
        PowerUp powerUp = board.getPowerUps().get(0);
        board.drawPowerUp(powerUp);
        try {
          player.addPowerUp(powerUp);
        } catch (InvalidPowerUpException e) {
          board.undrawPowerUp(powerUp);
        }
      }
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerCollectWeaponEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }

    boardController.getTurnController().addTurnActions(
        new Payment(boardController.getTurnController(),
            player,
            board.getWeaponByName(event.getWeaponName())));
    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerPowerUpEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }
    List<GameAction> actions = new ArrayList<>();
    actions.add(new Payment(boardController.getTurnController(), player, event.getPowerUp()));
    for (WeaponAction action : event.getPowerUp().getActions()) {
      actions.add(new PowerUpEffect(player, event.getPowerUp(), action));
    }

    boardController.getTurnController().addTurnActions(actions);
  }

  public void update(PlayerActionSelectionEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }

    List<GameAction> actions = new ArrayList<>();
    switch (event.getTurnAction()) {
      case RUN:
        actions.add(new SquareSelection(player, 3));
        break;
      case WALK_FETCH:
        actions.add(new SquareSelection(player, 1));
        actions.add(new ObjectPickup(player));
        break;
      case WALK_FETCH3:
      case FF_RUN_FETCH:
        actions.add(new SquareSelection(player, 2));
        actions.add(new ObjectPickup(player));
        break;
      case SHOOT:
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case SHOOT6:
        actions.add(new SquareSelection(player, 1));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case FF_RUN:
        actions.add(new SquareSelection(player, 4));
        break;
      case FF_RUN_RELOAD_SHOOT:
        actions.add(new SquareSelection(player, 1));
        actions.add(new CheckReloadWeapons(boardController.getTurnController(), player));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case FF_WALK_FETCH:
        actions.add(new SquareSelection(player, 3));
        actions.add(new ObjectPickup(player));
        break;
      case FF_WALK_RELOAD_SHOOT:
        actions.add(new SquareSelection(player, 2));
        actions.add(new CheckReloadWeapons(boardController.getTurnController(), player));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      default:
        return;
    }

    boardController.getTurnController().addTurnActions(actions);
    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerDiscardPowerUpEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());

    if (player == null) {
      return;
    }

    PowerUp powerUp = board.getPowerUpByNameAndColor(event.getPowerUp().getName(),
        event.getPowerUp().getColor());

    try {
      player.removePowerUp(powerUp);
    } catch (InvalidPowerUpException ignored) {
      return;
    }
    board.undrawPowerUp(powerUp);

    if (board.getTurnCounter() > 1) {
      player.respawn(event.getPowerUp().getColor());
    } else {
      player.setSquare(board.getSpawnPointSquare(powerUp.getColor()));
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerPaymentEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());

    if (player == null) {
      return;
    }

    if (player.getAmmo(AmmoColor.BLUE) >= event.getBlue()
        && player.getAmmo(AmmoColor.RED) >= event.getRed()
        && player.getAmmo(AmmoColor.YELLOW) >= event.getYellow()
        && player.getPowerUps().containsAll(event.getPowerUps())) {

      player.addAmmo(AmmoColor.BLUE, player.getAmmo(AmmoColor.BLUE) - event.getBlue());
      player.addAmmo(AmmoColor.RED, player.getAmmo(AmmoColor.RED) - event.getRed());
      player.addAmmo(AmmoColor.YELLOW, player.getAmmo(AmmoColor.YELLOW) - event.getYellow());

      for (PowerUp powerUp : event.getPowerUps()) {
        try {
          player.removePowerUp(powerUp);
        } catch (InvalidPowerUpException e) {
          return;
        }
        board.undrawPowerUp(powerUp);
      }
    }

    event.getItem().afterPaymentCompleted(boardController.getTurnController(), board, player);

    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerSelectWeaponEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }
    Weapon selectedWeapon = board.getWeaponByName(event.getWeaponName());
    if (selectedWeapon != null) {
      player.setCurrentWeapon(selectedWeapon);
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerSelectWeaponEffectEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }
    Weapon weapon = board.getWeaponByName(event.getWeaponName());
    if (weapon != null) {
      List<GameAction> actions = new ArrayList<>();
      for (String effectName : event.getEffectNames()) {
        actions.add(new Payment(boardController.getTurnController(), player,
            weapon.getEffectByName(effectName)));
      }

      boardController.getTurnController().addTurnActions(actions);
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("PlayerController", "Inoltrato evento: " + event.getEventType());
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
    return obj instanceof PlayerController &&
        ((PlayerController) obj).boardController.equals(boardController);
  }

  @Override
  public int hashCode() {
    return boardController.hashCode();
  }
}
