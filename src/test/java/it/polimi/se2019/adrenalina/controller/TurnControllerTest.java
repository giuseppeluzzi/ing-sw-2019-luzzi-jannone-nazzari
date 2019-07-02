package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.action.game.EndGame;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TurnControllerTest {

  @Test
  public void testAddActions() {
    BoardController boardController = null;
    try {
      boardController = new BoardController(false);
    } catch (RemoteException e) {
      Log.exception(e);
    }
    TurnController turnController = new TurnController(boardController);
    assertEquals(0, turnController.getActionQueueSize());
    turnController.addTurnActions(new EndGame());
    assertEquals(1, turnController.getActionQueueSize());
    List<GameAction> actions = new ArrayList<>();
    actions.add(new EndGame());
    actions.add(new EndGame());
    turnController.addTurnActions(actions);
    turnController.disableActionsUntilPowerup();
    assertEquals(3, turnController.getActionQueueSize());
  }
}