package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PlayerController extends UnicastRemoteObject implements Observer {

  private static final long serialVersionUID = 5970759489567761611L;

  private final BoardController boardController;

  PlayerController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;
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

  public void update(PlayerCollectAmmoEvent event) {
    Board board = boardController.getBoard();
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
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
  }

  public void update(PlayerCollectWeaponEvent event) {
    // TODO: invoked when a player wants to collect a weapon
  }

  public void update(PlayerCollectPowerUpEvent event) {
    // TODO
  }

  public void update(PlayerPowerUpEvent event) {
    // TODO: invoked when a player uses a powerup
  }

  public void update(PlayerActionSelectionEvent event) {
    // TODO
  }

  public void update(PlayerDiscardPowerUpEvent event) {
    // TODO
  }

  public void update(PlayerPaymentEvent event) {
    // TODO
  }

  public void update(PlayerSelectWeaponEvent event) {
    // TODO
  }

  public void update(PlayerSelectWeaponEffectEvent event) {
    // TODO
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
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
