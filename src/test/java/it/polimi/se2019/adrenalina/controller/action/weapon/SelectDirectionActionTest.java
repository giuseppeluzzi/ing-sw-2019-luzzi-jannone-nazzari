package it.polimi.se2019.adrenalina.controller.action.weapon;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.ui.text.TUIBoardView;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

public class SelectDirectionActionTest {

  private BoardController boardController;
  private TurnController turnController;
  private Player player;
  private Weapon weapon;
  private Player player2;
  private Square square1;

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
    square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().addPlayer(player);
  }

  @Test
  public void testSelectDirection() {
    ExecutableEffect effect = new ExecutableEffect(turnController, player, weapon, new SelectDirectionAction());
    assertTrue(effect.getWeaponAction().isSync());
    String json = effect.getWeaponAction().serialize();
    assertEquals(effect.getWeaponAction(), SelectDirectionAction.deserialize(json));
    assertEquals(effect.getWeaponAction().hashCode(), SelectDirectionAction.deserialize(json).hashCode());
    assertEquals(effect.getWeaponAction().getActionType(), SelectDirectionAction.deserialize(json).getActionType());
    assertNotEquals(effect.getWeaponAction(), new ShootAction(1,1,1,false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeException() {
    SelectDirectionAction.deserialize(null);
  }
}