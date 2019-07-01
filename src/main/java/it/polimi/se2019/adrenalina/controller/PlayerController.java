package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.CheckReloadWeapons;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.ObjectPickup;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.controller.action.game.SelectEffect;
import it.polimi.se2019.adrenalina.controller.action.game.SelectWeapon;
import it.polimi.se2019.adrenalina.controller.action.game.SquareSelection;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSwapWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerUnsuspendEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.PowerUpUsage;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.model.WeaponBuy;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
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
 * Controller in charge of managing players.
 */
public class PlayerController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = 5970759489567761611L;

  private final BoardController boardController;

  private final Set<EventType> registeredEvents = new HashSet<>();

  PlayerController(BoardController boardController) throws RemoteException {
    // N.B. Sonar incorrectly reports the "throws" clause of this method as redoundant, but it's not.

    this.boardController = boardController;
    registeredEvents.add(EventType.PLAYER_COLLECT_AMMO_EVENT);
    registeredEvents.add(EventType.PLAYER_COLLECT_WEAPON_EVENT);
    registeredEvents.add(EventType.PLAYER_POWERUP_EVENT);
    registeredEvents.add(EventType.PLAYER_ACTION_SELECTION_EVENT);
    registeredEvents.add(EventType.PLAYER_DISCARD_POWERUP_EVENT);
    registeredEvents.add(EventType.PLAYER_PAYMENT_EVENT);
    registeredEvents.add(EventType.PLAYER_SELECT_WEAPON_EVENT);
    registeredEvents.add(EventType.PLAYER_SELECT_WEAPON_EFFECT_EVENT);
    registeredEvents.add(EventType.PLAYER_SWAP_WEAPON_EVENT);
    registeredEvents.add(EventType.PLAYER_UNSUSPEND_EVENT);
  }

  /**
   * Get a player of the specified color from the board.
   * @param board the game board
   * @param playerColor the color of the player to get
   * @return the Player object
   */
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

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerCollectAmmoEvent
   */
  public void update(PlayerCollectAmmoEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }

    executeCollectAmmo(board, event, player);

    boardController.getTurnController().executeGameActionQueue();
  }

  public void executeCollectAmmo(Board board, PlayerCollectAmmoEvent event, Player player) {
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
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerCollectWeaponEvent
   */
  public void update(PlayerCollectWeaponEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }

    boardController.getTurnController().addTurnActions(
        new Payment(boardController.getTurnController(),
            player, new WeaponBuy(board.getWeaponByName(event.getWeaponName()))
            ));
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerPowerUpEvent
   */
  public void update(PlayerPowerUpEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    if (player == null) {
      return;
    }
    if (event.getColor() == null || event.getType() == null) {
      boardController.getTurnController().executeGameActionQueue();
      return;
    }

    PowerUp powerUp = player.getPowerUp(event.getType(), event.getColor());

    if (powerUp.getType() == PowerUpType.TAGBACK_GRANADE) {
      powerUp.setTargetHistory(1, getPlayerFromBoard(board, board.getCurrentPlayer()));
    }
    Buyable buyable = new PowerUpUsage(powerUp);
    player.setCurrentBuying(buyable);
    player.setOldExecutable(player.getCurrentExecutable());
    player.setCurrentExecutable(powerUp);
    boardController.getTurnController()
        .addTurnActions(new Payment(boardController.getTurnController(), player, buyable));
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerActionSelectionEvent
   */
  public void update(PlayerActionSelectionEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }

    for (Player notCurrentPlayer : board.getPlayers()) {
      if (notCurrentPlayer.getColor() != player.getColor() && notCurrentPlayer.getClient() != null) {
        try {
          notCurrentPlayer.getClient().showGameMessage(
              String.format("%s%s%s ha scelto di %s",
                  player.getColor().getAnsiColor(),
                  player.getName(),
                  ANSIColor.RESET,
                  event.getTurnAction().getMessage()));
        } catch (RemoteException e) {
          Log.exception(e);
        }
      }
    }

    List<GameAction> actions = new ArrayList<>();
    switch (event.getTurnAction()) {
      case RUN:
        actions.add(new SquareSelection(player, 3, false));
        break;
      case WALK_FETCH:
        actions.add(new SquareSelection(player, 1, true));
        actions.add(new ObjectPickup(player));
        break;
      case WALK_FETCH3:
      case FF_RUN_FETCH:
        actions.add(new SquareSelection(player, 2, true));
        actions.add(new ObjectPickup(player));
        break;
      case SHOOT:
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case SHOOT6:
        actions.add(new SquareSelection(player, 1, false));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case FF_RUN:
        actions.add(new SquareSelection(player, 4, false));
        break;
      case FF_RUN_RELOAD_SHOOT:
        actions.add(new SquareSelection(player, 1, false));
        actions.add(new CheckReloadWeapons(boardController.getTurnController(), player));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case FF_WALK_FETCH:
        actions.add(new SquareSelection(player, 3, true));
        actions.add(new ObjectPickup(player));
        break;
      case FF_WALK_RELOAD_SHOOT:
        actions.add(new SquareSelection(player, 2, false));
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

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerDiscardPowerUpEvent
   */
  public void update(PlayerDiscardPowerUpEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }

    PowerUp powerUp = player.getPowerUp(event.getPowerUpType(), event.getPowerUpColor());

    if (powerUp == null) {
      return;
    }

    spawnPlayer(player, powerUp);

    // CHEAT SUITE
    // TODO CANCELLARE
    player.addAmmo(AmmoColor.RED, 3);
    player.addAmmo(AmmoColor.BLUE, 3);
    player.addAmmo(AmmoColor.YELLOW, 3);
    /*Weapon weapon = board.getWeapons().get(0);
    Weapon weapon1 = board.getWeapons().get(1);
    Weapon weapon2 = board.getWeapons().get(2);
    board.takeWeapon(weapon);
    player.addWeapon(weapon);
    board.takeWeapon(weapon1);
    player.addWeapon(weapon1);
    board.takeWeapon(weapon2);
    player.addWeapon(weapon2);*/
    if ("PeppeSocket".equals(player.getName())) {
      player.addAmmo(AmmoColor.RED, 3);
      player.addAmmo(AmmoColor.BLUE, 3);
      player.addAmmo(AmmoColor.YELLOW, 3);
    }
    if ("socket2".equals(player.getName())) {
      player.addAmmo(AmmoColor.RED, 3);
      player.addAmmo(AmmoColor.BLUE, 3);
      player.addAmmo(AmmoColor.YELLOW, 3);
    }
    if ("socket3".equals(player.getName())) {
      player.addAmmo(AmmoColor.RED, 3);
      player.addAmmo(AmmoColor.BLUE, 3);
      player.addAmmo(AmmoColor.YELLOW, 3);
    }
    if ("socket4".equals(player.getName())) {
      player.addAmmo(AmmoColor.RED, 3);
      player.addAmmo(AmmoColor.BLUE, 3);
      player.addAmmo(AmmoColor.YELLOW, 3);
    }
    if ("socket5".equals(player.getName())) {
      player.addAmmo(AmmoColor.RED, 3);
      player.addAmmo(AmmoColor.BLUE, 3);
      player.addAmmo(AmmoColor.YELLOW, 3);
    }
    // END CHEAT SUITE

    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerPaymentEvent
   */
  public void update(PlayerPaymentEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }

    if (player.getAmmo(AmmoColor.BLUE) >= event.getBlue()
        && player.getAmmo(AmmoColor.RED) >= event.getRed()
        && player.getAmmo(AmmoColor.YELLOW) >= event.getYellow()) {

      player.addAmmo(AmmoColor.BLUE, -event.getBlue());
      player.addAmmo(AmmoColor.RED, -event.getRed());
      player.addAmmo(AmmoColor.YELLOW, -event.getYellow());

      for (PowerUp powerUp : event.getPowerUps()) {
        PowerUp localPowerUp = player.getPowerUp(powerUp.getType(), powerUp.getColor());
        try {
          player.removePowerUp(localPowerUp);
        } catch (InvalidPowerUpException ignored) {
          //
        }
        board.undrawPowerUp(localPowerUp);
      }
    }
    if (player.getCurrentBuying() != null) {
      TurnController turnController = boardController.getTurnController();
      player.getCurrentBuying()
          .afterPaymentCompleted(turnController, board, player);
      player.setCurrentBuying(null);
    }
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerSelectWeaponEvent
   */
  public void update(PlayerSelectWeaponEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }
    Weapon selectedWeapon = board.getWeaponByName(event.getWeaponName());
    if (selectedWeapon != null) {
      player.setCurrentExecutable(selectedWeapon);

      for (Player player1 : board.getPlayers()) {
        if (player1.getColor() != player.getColor() && player1.getClient() != null) {
          try {
            player1.getClient().showGameMessage(
                String.format("%s%s%s sta usando %s%s%s",
                    player.getColor().getAnsiColor(),
                    player.getName(),
                    ANSIColor.RESET,
                    selectedWeapon.getBaseCost().getAnsiColor(),
                    selectedWeapon.getName(),
                    ANSIColor.RESET));
          } catch (RemoteException e) {
            Log.exception(e);
          }
        }
      }
    }


    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerSwapWeaponEvent
   */
  public void update(PlayerSwapWeaponEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    if (player == null) {
      return;
    }
    Weapon ownWeapon = player.getWeaponByName(event.getOwnWeaponName());
    Weapon squareWeapon = board.getWeaponByName(event.getSquareWeaponName());

    if (ownWeapon != null && squareWeapon != null) {
      player.removeWeapon(ownWeapon);
      player.getSquare().removeWeapon(squareWeapon);
      player.addWeapon(squareWeapon);
      player.getSquare().addWeapon(ownWeapon);
    }
    boardController.getTurnController().executeGameActionQueue();
  }

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerSelectWeaponEffectEvent
   */
  public void update(PlayerSelectWeaponEffectEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
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

  /**
   * Event handling.
   * @param event the received event
   * @see PlayerUnsuspendEvent
   */
  public void update(PlayerUnsuspendEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }
    player.setStatus(PlayerStatus.PLAYING);
    player.resetTimeoutCount();
  }

  /**
   * Generic event handling.
   * @param event the received event
   */
  @Override
  public void update(Event event) {
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("PlayerController", "Event received: " + event.getEventType());
      try {
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      } catch (NoSuchMethodException | IllegalAccessException ignored) {
        //
      } catch (InvocationTargetException e) {
        Log.exception(e);
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

  /**
   * (re)spawn a player on the board.
   * @param player the player to (re)spawn
   * @param powerUp the powerUp he discarded
   */
  public void spawnPlayer(Player player, PowerUp powerUp) {
    Board board = boardController.getBoard();
    try {
      player.removePowerUp(powerUp);
    } catch (InvalidPowerUpException ignored) {
      return;
    }
    board.undrawPowerUp(powerUp);
    player.setSquare(board.getSpawnPointSquare(powerUp.getColor()));
  }
}
