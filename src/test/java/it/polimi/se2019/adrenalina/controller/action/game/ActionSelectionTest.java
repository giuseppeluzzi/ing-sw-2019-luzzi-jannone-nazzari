package it.polimi.se2019.adrenalina.controller.action.game;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActionSelectionTest {

  @Test
  public void testStandardTurnActions() {
    List<TurnAction> turnActions;

    turnActions = ActionSelection.standardTurnActions(true, 0);
    assertTrue(turnActions.contains(TurnAction.SHOOT));
    assertTrue(turnActions.contains(TurnAction.RUN));
    assertTrue(turnActions.contains(TurnAction.WALK_FETCH));

    turnActions = ActionSelection.standardTurnActions(true, 6);
    assertTrue(turnActions.contains(TurnAction.SHOOT6));
    assertTrue(turnActions.contains(TurnAction.WALK_FETCH3));
  }

  @Test
  public void testFinalFrenzyTurnActions() {
    List<TurnAction> turnActions;

    turnActions = ActionSelection.finalFrenzyTurnActions(0, false, 1);
    assertEquals(2, turnActions.size());
    assertTrue(turnActions.contains(TurnAction.FF_RUN));
    assertTrue(turnActions.contains(TurnAction.FF_RUN_FETCH));

    turnActions = ActionSelection.finalFrenzyTurnActions(0, true, 1);
    assertTrue(turnActions.contains(TurnAction.FF_RUN_RELOAD_SHOOT));
    assertEquals(3, turnActions.size());

    turnActions = ActionSelection.finalFrenzyTurnActions(1, false, 0);
    assertTrue(turnActions.contains(TurnAction.FF_WALK_FETCH));
    assertEquals(1, turnActions.size());

    turnActions = ActionSelection.finalFrenzyTurnActions(1, true, 0);
    assertTrue(turnActions.contains(TurnAction.FF_WALK_RELOAD_SHOOT));
    assertEquals(2, turnActions.size());
  }
}