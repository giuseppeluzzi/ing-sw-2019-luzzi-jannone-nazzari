package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.*;

import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class WeaponReloadTest {
  private Weapon weapon;
  private WeaponReload weaponReload;

  @Before
  public void setObjects() {
    weapon = new Weapon(1,2,1, AmmoColor.YELLOW,"test","w");
    weaponReload = new WeaponReload(weapon);
  }

  @Test
  public void testGet() {
    assertEquals(BuyableType.WEAPON_RELOAD ,weaponReload.getBuyableType());
    assertEquals(weapon.getCost(true), weaponReload.getCost());
    assertEquals(weapon.getName() + " (ricarica)", weaponReload.promptMessage());
    assertEquals(2, weaponReload.getCost(AmmoColor.BLUE));
    assertEquals(weapon, weaponReload.getBaseBuyable());
  }

  @Test
  public void testAfterPayment() {
    BoardController boardController = null;
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
    TurnController turnController = spy(new TurnController(boardController));
    doNothing().when(turnController).executeGameActionQueue();

    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    Player player2 = new Player("test2", PlayerColor.YELLOW, boardController.getBoard());
    boardController.getBoard().addPlayer(player);
    boardController.getBoard().addPlayer(player2);
    boardController.getBoard().setCurrentPlayer(PlayerColor.YELLOW);

    player.addWeapon(weapon);
    Square square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square2 = new Square(0, 1, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square3 = new Square(1, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square4 = new Square(1, 1, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    square1.setSpawnPoint(true);
    square1.addWeapon(weapon);
    board.setSquare(square1);
    board.setSquare(square2);
    board.setSquare(square3);
    board.setSquare(square4);
    board.addWeapon(weapon);
    weaponReload.afterPaymentCompleted(turnController, board, player);
    assertTrue(player.getWeaponByName("test").isLoaded());
  }

}