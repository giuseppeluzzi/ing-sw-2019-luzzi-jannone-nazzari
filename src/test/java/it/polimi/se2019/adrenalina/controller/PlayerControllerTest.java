package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.model.Player;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

public class PlayerControllerTest {
  BoardController boardController;

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

  @Test(expected = IllegalArgumentException.class)
  public void testCreatePlayerException() {
    PlayerController playerController = null;
    try {
      playerController = new PlayerController(boardController);
    } catch (RemoteException e) {
      fail("Exception unexpected");
    }
    playerController.createPlayer("P1", PlayerColor.GREY);
    playerController.createPlayer("P1", PlayerColor.GREY);

  }
}