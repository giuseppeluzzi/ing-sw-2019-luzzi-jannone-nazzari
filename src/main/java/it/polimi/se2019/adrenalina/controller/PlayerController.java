package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PlayerColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class PlayerController implements Observer {
  private final BoardController boardController;

  PlayerController(BoardController boardController) {
    this.boardController = boardController;
  }

  /**
   * Instances a new player with a name and color and adds it to the board.
   * @param name the player's name.
   * @param color the player's color.
   * @return the player instance.
   * @throws IllegalArgumentException thrown if the name is already used by
   * another player in this board.
   */
  public Player createPlayer(String name, PlayerColor color) {
    for (Player player: boardController.getBoard().getPlayers()) {
      if (player.getName().equalsIgnoreCase(name)) {
        throw new IllegalArgumentException("This name is already used by another player in this board");
      }
    }
    Player player = new Player(name, color);
    try {
      boardController.addPlayer(player);
    } catch (FullBoardException e) {
      Log.severe("Tried to add a player to a full board");
    } catch (PlayingBoardException e) {
      Log.severe("Tried to add a new player to a playing board");
    } catch (EndedGameException e) {
      Log.severe("Tried to add a player to an ended game");
    }
    return player;
  }

  public void update(PlayerMoveEvent event) {
    // TODO: invoked when a player wants to go in a square
  }

  public void update(PlayerCollectAmmoEvent event) {
    // TODO: invoked when a player wants to collect an ammocard
  }

  public void update(PlayerCollectWeaponEvent event) {
    // TODO: invoked when a player wants to collect a weapon
  }

  public void update(PlayerPowerUpEvent event) {
    // TODO: invoked when a player uses a powerup
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
