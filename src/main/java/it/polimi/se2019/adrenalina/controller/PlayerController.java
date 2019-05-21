package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.Payment;
import it.polimi.se2019.adrenalina.controller.action.game.PickPowerUp;
import it.polimi.se2019.adrenalina.controller.action.game.PowerUpEffect;
import it.polimi.se2019.adrenalina.controller.action.game.SelectEffect;
import it.polimi.se2019.adrenalina.controller.action.game.SelectWeapon;
import it.polimi.se2019.adrenalina.controller.action.game.SquareSelection;
import it.polimi.se2019.adrenalina.controller.action.game.WeaponEffect;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerNoCollectEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.exceptions.InvalidWeaponException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class PlayerController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = 5970759489567761611L;

  private final BoardController boardController;

  PlayerController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;
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
        player.setAmmo(color, ammoCard.getAmmo(color));
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

    Weapon collectedWeapon = null;
    for (Weapon weapon : board.getSquare(event.getSquareX(), event.getSquareY()).getWeapons()) {
      if (weapon.getName().equals(event.getWeaponName())) {
        collectedWeapon = weapon;
      }
    }

    if (collectedWeapon != null) {
      try {
        board.takeWeapon(collectedWeapon);
      } catch (InvalidWeaponException ignored) {
        return;
      }
      player.addWeapon(collectedWeapon);
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  public void update(PlayerPowerUpEvent event) {
    Board board = boardController.getBoard();
    Player player = getPlayerFromBoard(board, event.getPlayerColor());
    if (player == null) {
      return;
    }
    List<GameAction> actions = new ArrayList<>();
    actions.add(new Payment(player, 0, 0,0, event.getPowerUp().doesCost() ? 1 : 0));
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
      case WALK_AND_FETCH:
        actions.add(new SquareSelection(player, 1));
        actions.add(new PickPowerUp(player));
        break;
      case SHOOT:
        actions.add(new SelectWeapon(player));
        actions.add(new SelectEffect(player));
        break;
      case FF_RUN_RELOAD_SHOOT:
        actions.add(new SquareSelection(player, 1));
        actions.add(new SelectWeapon(player));
        actions.add(new SelectWeapon(player));
        break;
      case FF_RUN_AND_FETCH:
        actions.add(new SquareSelection(player, 2));
        actions.add(new PickPowerUp(player));
        break;
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
    try {
      player.removePowerUp(event.getPowerUp());
    } catch (InvalidPowerUpException ignored) {
      return;
    }
    board.undrawPowerUp(event.getPowerUp());

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
      player.setAmmo(AmmoColor.BLUE, player.getAmmo(AmmoColor.BLUE) - event.getBlue());
      player.setAmmo(AmmoColor.RED, player.getAmmo(AmmoColor.RED) - event.getRed());
      player.setAmmo(AmmoColor.YELLOW, player.getAmmo(AmmoColor.YELLOW) - event.getYellow());
      for (PowerUp powerUp : event.getPowerUps()) {
        try {
          player.removePowerUp(powerUp);
        } catch (InvalidPowerUpException e) {
          return;
        }
        board.undrawPowerUp(powerUp);
      }
    }

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
      actions.add(new Payment(player, weapon.getCost(AmmoColor.RED), weapon.getCost(AmmoColor.BLUE), weapon.getCost(AmmoColor.YELLOW), 0));
      for (String effectName : event.getEffectNames()) {
        for (WeaponAction action : weapon.getEffectByName(effectName).getActions()) {
          actions.add(new WeaponEffect(player, weapon, action));
        }
      }

      boardController.getTurnController().addTurnActions(actions);
    }

    boardController.getTurnController().executeGameActionQueue();
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException("Wrong method type");
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
