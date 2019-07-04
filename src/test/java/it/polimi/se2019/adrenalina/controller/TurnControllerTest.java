package it.polimi.se2019.adrenalina.controller;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.action.game.EndGame;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TurnControllerTest {

  private BoardController boardController;
  private TurnController turnController;
  private Player player;
  private Player player2;

  @Before
  public void set() {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException e) {
      Log.exception(e);
    }
    turnController = new TurnController(boardController);
    player = new Player("test", PlayerColor.GREY, boardController.getBoard());
    player2 = new Player("test2", PlayerColor.GREEN, boardController.getBoard());
    Square square1 = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, boardController.getBoard());
    Square square2 = new Square(0, 1, SquareColor.RED, WALL, WALL, WALL, WALL, boardController.getBoard());
    Square square3 = new Square(1, 0, SquareColor.RED, WALL, WALL, WALL, WALL, boardController.getBoard());
    Square square4 = new Square(1, 1, SquareColor.RED, WALL, WALL, WALL, WALL, boardController.getBoard());
    square1.setSpawnPoint(true);
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().setSquare(square2);
    boardController.getBoard().setSquare(square3);
    boardController.getBoard().setSquare(square4);
    boardController.getBoard().addPlayer(player);
    for (int i = 0; i < 10; i++) {
      boardController.getBoard().addAmmoCard(new AmmoCard(3,0,0,0));
      boardController.getBoard().addWeapon(new Weapon(0,0,0,AmmoColor.RED, String.format("test%d", i), Integer.toString(i), true));
    }
  }

  @Test
  public void testAddActions() {
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

  @Test
  public void testPrepare() {
    assertFalse(turnController.getBoardController().getBoard().isDominationBoard());
    turnController.prepare();
    assertEquals(player.getColor(), boardController.getBoard().getCurrentPlayer());
    turnController.getBoardController().getBoard().incrementTurnCounter();
    turnController.prepare();
    assertEquals(player.getColor(), boardController.getBoard().getCurrentPlayer());
    boardController.getBoard().addPlayer(player2);
    boardController.getBoard().setFinalFrenzySelected(true);
    boardController.getBoard().setFinalFrenzyActivator(player2.getColor());
    boardController.getBoard().setSkulls(0);
    turnController.clearActionsQueue();
    turnController.endTurn();
    assertTrue(turnController.isEndGame());
  }

  @Test
  public void testEndGameReasons() {
    boardController.getBoard().setCurrentPlayer(player.getColor());
    player.addWeapon(new Weapon(0,0,0,AmmoColor.BLUE,"test", "r"));
    turnController.clearActionsQueue();
    turnController.endTurn();
    assertEquals(0, turnController.getEndGameReason());
    boardController.getBoard().setSkulls(0);
    boardController.getBoard().setFinalFrenzySelected(false);
    boardController.getBoard().addPlayer(new Player("test3", PlayerColor.BLUE, null));
    boardController.getBoard().addPlayer(new Player("test4", PlayerColor.GREEN, null));
    turnController.endTurn();
    boardController.getBoard().incrementTurnKillShots();
    boardController.getBoard().incrementTurnKillShots();
    assertEquals(1, turnController.getEndGameReason());
    boardController.getBoard().setStatus(BoardStatus.FINAL_FRENZY);
    boardController.getBoard().setFinalFrenzySelected(true);
    boardController.getBoard().setCurrentPlayer(PlayerColor.GREEN);
    boardController.getBoard().setFinalFrenzyActivator(PlayerColor.GREEN);
    turnController.endTurn();
    assertEquals(2, turnController.getEndGameReason());
  }

  @Test
  public void testAddRespawn() {
    turnController.addRespawn(player);
    assertEquals(2 , turnController.getActionQueueSize());
  }

  @Test
  public void testPrepareFF() {
    boardController.getBoard().setStatus(BoardStatus.FINAL_FRENZY);
    boardController.getBoard().incrementTurnCounter();
    boardController.getBoard().setFinalFrenzySelected(true);
    boardController.getBoard().setFinalFrenzyActivator(PlayerColor.GREEN);
    boardController.getBoard().addPlayer(new Player("test4", PlayerColor.GREEN, null));
    turnController.prepare();
    assertEquals(4, turnController.getActionQueueSize());
  }

  /*
  @Test
  public void testTurnTimeout() {
    GameAction syncAction = new SelectEffect(player);
    turnController.addTurnActions(syncAction);
    turnController.executeGameActionQueue();

  }*/
}