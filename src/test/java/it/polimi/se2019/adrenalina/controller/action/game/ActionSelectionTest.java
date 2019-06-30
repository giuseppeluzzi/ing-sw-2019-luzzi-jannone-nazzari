package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    assertEquals(1, turnActions.size());
    assertTrue(turnActions.contains(TurnAction.FF_WALK_FETCH));

    turnActions = ActionSelection.finalFrenzyTurnActions(0, true, 1);
    assertTrue(turnActions.contains(TurnAction.FF_WALK_RELOAD_SHOOT));
    assertEquals(2, turnActions.size());

    turnActions = ActionSelection.finalFrenzyTurnActions(1, false, 0);
    assertTrue(turnActions.contains(TurnAction.FF_RUN));
    assertTrue(turnActions.contains(TurnAction.FF_RUN_FETCH));
    assertEquals(2, turnActions.size());

    turnActions = ActionSelection.finalFrenzyTurnActions(1, true, 0);
    assertTrue(turnActions.contains(TurnAction.FF_RUN_RELOAD_SHOOT));
    assertEquals(3, turnActions.size());
  }

  @Test
  public void testReset() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    TargetingScope targetingScope = new TargetingScope(AmmoColor.RED);
    Weapon weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","t");
    Weapon weapon2 = new Weapon(3,0,0,AmmoColor.BLUE,"test2","p");
    player.addWeapon(weapon);
    player.addWeapon(weapon2);
    try {
      player.addPowerUp(targetingScope);
    } catch (InvalidPowerUpException ignore) {
      //
    }
    ActionSelection.resetWeapons(player);
    assertTrue(!weapon.targetHistoryContainsKey(1));
    ActionSelection.resetPowerUps(player);
    assertTrue(!targetingScope.targetHistoryContainsKey(1));
  }

  @Test
  public void testSetTurnAction() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Weapon weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","t");
    ActionSelection actionSelection = new ActionSelection(null, player);
    player.addWeapon(weapon);
    weapon.setLoaded(true);
    board.addPlayer(player);
    board.setFinalFrenzySelected(true);
    board.setFinalFrenzyActivator(PlayerColor.GREEN);
    board.setStatus(BoardStatus.FINAL_FRENZY);
    List<TurnAction> actions = actionSelection.setTurnActions(board);
    assertTrue(actions.contains(TurnAction.FF_WALK_FETCH));
    assertTrue(actions.contains(TurnAction.FF_WALK_RELOAD_SHOOT));
  }
}