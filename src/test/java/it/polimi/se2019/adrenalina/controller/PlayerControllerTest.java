package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
}