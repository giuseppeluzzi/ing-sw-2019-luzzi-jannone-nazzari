package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlayerControllerTest {
  private BoardController boardController;
  private PlayerController playerController;

  {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
  }

  @Before
  public void setBoardController() {
    try {
      boardController.addPlayer(new Player("P1", PlayerColor.GREEN, null));
      boardController.addPlayer(new Player("P2", PlayerColor.BLUE, null));
      boardController.addPlayer(new Player("P3", PlayerColor.YELLOW, null));
    } catch (Exception ignore) {
      //
    }
  }

  @Test
  public void testCreatePlayer() {
    PlayerController playerController = null;
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }

    assertEquals("test", playerController.createPlayer("test", PlayerColor.GREY).getName());
  }

  @Test
  public void testExecuteCollectAmmo() {
    PlayerController playerController = null;
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    board.setSquare(new Square(0,0,SquareColor.GREEN,BorderType.WALL,BorderType.WALL,BorderType.WALL,BorderType.WALL,board));
    board.getSquare(0,0).setAmmoCard(new AmmoCard(1,1,1,0));
    PlayerCollectAmmoEvent event = new PlayerCollectAmmoEvent(PlayerColor.GREEN, 0, 0);
    playerController.executeCollectAmmo(board,event,player);
    assertEquals(1, player.getAmmo(AmmoColor.YELLOW));
    board.getSquare(0,0).setAmmoCard(new AmmoCard(1,1,0,1));
    board.addPowerUp(new Newton(AmmoColor.BLUE));
    playerController.executeCollectAmmo(board,event,player);
    assertEquals(AmmoColor.BLUE, player.getPowerUps().get(0).getColor());
  }


  @Test
  public void testCollectWeapon() {
    PlayerController playerController = null;
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }
    PlayerCollectWeaponEvent event = new PlayerCollectWeaponEvent(PlayerColor.GREEN, "Fucile al plasma");
    try {
      playerController.update(event);
    } catch (NullPointerException ignore) {
      //
    }
  }

  @Test
  public void testPayment() {
    PlayerController playerController = null;
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }
    PlayerPaymentEvent event = new PlayerPaymentEvent(PlayerColor.GREEN, 1,1,1,new ArrayList<>());
  }
}