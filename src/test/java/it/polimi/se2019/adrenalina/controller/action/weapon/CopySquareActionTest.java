package it.polimi.se2019.adrenalina.controller.action.weapon;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

public class CopySquareActionTest {

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
    weapon = new Weapon(0,0,0,AmmoColor.BLUE,"test", "f");
    player = new Player("test", PlayerColor.GREY, boardController.getBoard());
    square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().addPlayer(player);
  }

  @Test
  public void testCopySquare() {
    player.addWeapon(weapon);
    player.setSquare(square1);
    ExecutableEffect effect = new ExecutableEffect(turnController, player, weapon, new CopySquareAction(0,1));
    weapon.setTargetHistory(0, player);
    assertEquals(1, effect.getWeaponAction().hashCode());
    assertEquals(1, ((CopySquareAction) effect.getWeaponAction()).getDestination());
    assertEquals(0, ((CopySquareAction) effect.getWeaponAction()).getOrigin());
    assertEquals(WeaponActionType.COPY_SQUARE, effect.getWeaponAction().getActionType());
    effect.execute(boardController.getBoard());
    assertEquals(SquareColor.RED, weapon.getTargetHistory(1).getSquare().getColor());
    String json = effect.getWeaponAction().serialize();
    assertEquals(effect.getWeaponAction().getActionType(), CopySquareAction.deserialize(json).getActionType());
    assertEquals(effect.getWeaponAction(), CopySquareAction.deserialize(json));
    assertNotEquals(effect.getWeaponAction(), new ShootAction(1, 0, 0, false));
    assertNotEquals(effect.getWeaponAction(), new CopySquareAction(1, 1));
    assertNotEquals(effect.getWeaponAction(), new CopySquareAction(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeException() {
    CopySquareAction.deserialize(null);
  }
}