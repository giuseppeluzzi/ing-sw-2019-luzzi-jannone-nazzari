package it.polimi.se2019.adrenalina.controller.action.weapon;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

public class MoveActionTest {

  private BoardController boardController;
  private TurnController turnController;
  private Player player;
  private Player player2;
  private Player player3;
  private Weapon weapon;
  private Square square1;
  private Square square2;

  @Before
  public void set() {
    try {
      boardController = new BoardController(false);
    } catch (RemoteException e) {
      Log.exception(e);
    }
    turnController = new TurnController(boardController);
    weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test", "f");
    player = new Player("test", PlayerColor.GREY, boardController.getBoard());
    player2 = new Player("test2", PlayerColor.GREEN, boardController.getBoard());
    player3 = new Player("test3", PlayerColor.PURPLE, boardController.getBoard());
    square1 = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, boardController.getBoard());
    square2 = new Square(1, 0, SquareColor.BLUE, WALL, WALL, WALL, WALL, boardController.getBoard());
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().addPlayer(player);
  }

  @Test
  public void testMoveAction() {
    ExecutableEffect effect = new ExecutableEffect(turnController, player, weapon,
        new MoveAction(0,1));
    player.setSquare(square1);
    //weapon.setInitialPlayerPosition(player, square1);
    weapon.setTargetHistory(0,player);
    weapon.setTargetHistory(1,square2);
    effect.execute(boardController.getBoard());
    assertEquals(SquareColor.BLUE, player.getSquare().getColor());
    String json = effect.getWeaponAction().serialize();
    assertEquals(0, MoveAction.deserialize(json).getTarget());
    assertEquals(1, MoveAction.deserialize(json).getDestination());

    assertEquals(1 + effect.getWeaponAction().getActionType().ordinal(), MoveAction.deserialize(json).hashCode());
    assertEquals(effect.getWeaponAction(), MoveAction.deserialize(json));
    assertNotEquals(effect.getWeaponAction(), new ShootAction(0, 0, 0, true));
    assertNotEquals(effect.getWeaponAction(), new MoveAction(1,1));
    assertNotEquals(effect.getWeaponAction(), new MoveAction(0,0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeException() {
    MoveAction.deserialize(null);
  }

}