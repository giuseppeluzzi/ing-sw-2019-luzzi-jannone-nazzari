package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSwapWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerUnsuspendEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PlayerControllerTest {

  private TurnController turnController;
  private BoardController boardController;
  private PlayerController playerController;
  private Player player1;
  private Player player2;
  private Player player3;


  @Before
  public void setBoardController() {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }

    turnController = new TurnController(boardController);

    player1 = new Player("P1", PlayerColor.GREEN, boardController.getBoard());
    player2 = new Player("P2", PlayerColor.BLUE, boardController.getBoard());
    player3 = new Player("P3", PlayerColor.YELLOW, boardController.getBoard());
    try {
      boardController.addPlayer(player1);
      boardController.addPlayer(player2);
      boardController.addPlayer(player3);
    } catch (Exception ignore) {
      //
    }
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }
  }

  @Test
  public void testCreatePlayer() {

    assertEquals("test", playerController.createPlayer("test", PlayerColor.GREY).getName());
  }

  @Test
  public void testExecuteCollectAmmo() {
    boardController.getBoard().setSquare(
        new Square(0, 0, SquareColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL,
            BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().getSquare(0, 0).setAmmoCard(new AmmoCard(1, 1, 1, 0));
    PlayerCollectAmmoEvent event = new PlayerCollectAmmoEvent(PlayerColor.GREEN, 0, 0);
    playerController.executeCollectAmmo(boardController.getBoard(), event, player1);
    assertEquals(1, player1.getAmmo(AmmoColor.YELLOW));
    boardController.getBoard().getSquare(0, 0).setAmmoCard(new AmmoCard(1, 1, 0, 1));
    playerController.executeCollectAmmo(boardController.getBoard(), event, player1);
    assertEquals(1, player1.getPowerUps().size());
  }

  @Test
  public void testUpdateCollectAmmo() {
    PlayerCollectAmmoEvent event = new PlayerCollectAmmoEvent(PlayerColor.GREEN, 0, 0);

    boardController.getBoard().setSquare(
        new Square(0, 0, SquareColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL,
            BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().getSquare(0, 0).setAmmoCard(new AmmoCard(1, 1, 1, 0));
    turnController.clearActionsQueue();
    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    try {
      boardController.removePlayer(player2);
    } catch (InvalidPlayerException ignore) {
      //
    }
    playerController.update(event);
    assertEquals(1, player1.getAmmo(AmmoColor.RED));
  }

  @Test
  public void testDiscardPowerUpEvent() {
    Newton newton = new Newton(AmmoColor.RED);
    Square square = new Square(0, 0, SquareColor.RED, BorderType.WALL, BorderType.WALL,
        BorderType.WALL, BorderType.WALL, boardController.getBoard());
    square.setSpawnPoint(true);
    boardController.getBoard().addPowerUp(newton);
    boardController.getBoard().drawPowerUp(newton);
    boardController.getBoard().setSquare(square);
    boardController.getBoard().setCurrentPlayer(player1.getColor());
    try {
      boardController.removePlayer(player2);
      boardController.removePlayer(player3);
    } catch (InvalidPlayerException ignore) {
      //
    }
    PlayerDiscardPowerUpEvent event = new PlayerDiscardPowerUpEvent(PlayerColor.GREEN,
        PowerUpType.NEWTON, AmmoColor.RED);

    try {
      player1.addPowerUp(newton);
    } catch (InvalidPowerUpException ignore) {
      //
    }

    playerController.update(event);
    assertEquals(SquareColor.RED, player1.getSquare().getColor());
  }

  @Test
  public void testUnsuspendEvent() {
    PlayerUnsuspendEvent event = new PlayerUnsuspendEvent(PlayerColor.GREEN);
    player1.setStatus(PlayerStatus.SUSPENDED);
    player1.incrementTimeoutCount();
    playerController.update(event);
    assertEquals(PlayerStatus.PLAYING, player1.getStatus());
  }

  @Test
  public void testCollecWeaponEvent() {
    PlayerCollectWeaponEvent event = new PlayerCollectWeaponEvent(PlayerColor.GREEN, "test");
    boardController.getBoard().addWeapon(new Weapon(0, 0, 0, AmmoColor.BLUE, "test", "f"));
    boardController.getBoard().setCurrentPlayer(player1.getColor());
    try {
      boardController.removePlayer(player2);
      boardController.removePlayer(player3);
    } catch (InvalidPlayerException ignore) {
      //
    }
    playerController.update(event);
    assertEquals(1, player1.getWeapons().size());
  }

  @Test
  public void testPlayerPowerupEvent() {
    TagbackGrenade tagbackGrenade = new TagbackGrenade(AmmoColor.RED);
    boardController.getBoard().addPowerUp(tagbackGrenade);
    boardController.getBoard().drawPowerUp(tagbackGrenade);
    try {
      player1.addPowerUp(tagbackGrenade);
    } catch (InvalidPowerUpException ignore) {
      //
    }
    PlayerPowerUpEvent event = new PlayerPowerUpEvent(PlayerColor.GREEN,
        PowerUpType.TAGBACK_GRANADE, AmmoColor.RED);
    boardController.getBoard().setCurrentPlayer(player2.getColor());
    try {
      boardController.removePlayer(player3);
    } catch (InvalidPlayerException ignore) {
      //
    }
    playerController.update(event);
    assertTrue(player1.getPowerUps().isEmpty());
  }

  @Test
  public void testGetAction() {
    assertEquals(1, playerController.getActions(TurnAction.RUN, player1).size());
    assertEquals(2, playerController.getActions(TurnAction.WALK_FETCH, player1).size());
    assertEquals(2, playerController.getActions(TurnAction.WALK_FETCH3, player1).size());
    assertEquals(2, playerController.getActions(TurnAction.FF_RUN_FETCH, player1).size());
    assertEquals(2, playerController.getActions(TurnAction.SHOOT, player1).size());
    assertEquals(3, playerController.getActions(TurnAction.SHOOT6, player1).size());
    assertEquals(1, playerController.getActions(TurnAction.FF_RUN, player1).size());
    assertEquals(4, playerController.getActions(TurnAction.FF_RUN_RELOAD_SHOOT, player1).size());
    assertEquals(2, playerController.getActions(TurnAction.FF_WALK_FETCH, player1).size());
    assertEquals(4, playerController.getActions(TurnAction.FF_WALK_RELOAD_SHOOT, player1).size());
  }

  @Test
  public void testPlayerPaymentEvent() {
    player1.addAmmo(AmmoColor.BLUE, 3);
    player1.addAmmo(AmmoColor.RED, 3);
    player1.addAmmo(AmmoColor.YELLOW, 3);
    List<PowerUp> powerUps = new ArrayList<>();
    powerUps.add(new Newton(AmmoColor.RED));
    boardController.getBoard().addPowerUp(powerUps.get(0));
    boardController.getBoard().drawPowerUp(powerUps.get(0));
    try {
      player1.addPowerUp(powerUps.get(0));
    } catch (InvalidPowerUpException ignore) {
      //
    }
    PlayerPaymentEvent event = new PlayerPaymentEvent(PlayerColor.GREEN, 3, 0, 0, powerUps);
    player1.setCurrentBuying(null);
    playerController.update(event);
    assertTrue(player1.getPowerUps().isEmpty());
  }

  @Test
  public void testSwapWeaponEvent() {
    PlayerSwapWeaponEvent event = new PlayerSwapWeaponEvent(PlayerColor.GREEN, "test", "Lanciafiamme");
    Square square = new Square(0,0,SquareColor.BLUE,BorderType.WALL,BorderType.WALL,BorderType.WALL,BorderType.WALL,boardController.getBoard());
    square.setSpawnPoint(true);
    boardController.getBoard().setSquare(square);
    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    player1.setSquare(square);
    player1.addWeapon(new Weapon(0, 0, 0, AmmoColor.BLUE, "test", "f"));
    playerController.update(event);
    assertEquals("L", player1.getWeapons().get(0).getSymbol());
  }
}