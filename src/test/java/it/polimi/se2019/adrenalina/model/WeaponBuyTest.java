package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.*;

import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeaponBuyTest {
  private Weapon weapon;
  private WeaponBuy weaponBuy;

  @Before
  public void setObjects() {
    weapon = new Weapon(1,2,1, AmmoColor.YELLOW,"test","w");
    weaponBuy = new WeaponBuy(weapon);
  }

  @Test
  public void testGet() {
    assertEquals(BuyableType.WEAPON ,weaponBuy.getBuyableType());
    assertEquals(weapon.getCost(), weaponBuy.getCost());
    assertEquals(weapon.getName(), weaponBuy.promptMessage());
    assertEquals(2, weaponBuy.getCost(AmmoColor.BLUE));
  }

  @Test
  public void testAfterPayment() {
    BoardController boardController = null;
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
    TurnController turnController = new TurnController(boardController);

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
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().setSquare(square2);
    boardController.getBoard().setSquare(square3);
    boardController.getBoard().setSquare(square4);
    weaponBuy.afterPaymentCompleted(turnController, boardController.getBoard(), player);
    assertTrue(((Weapon) weaponBuy.getBaseBuyable()).isLoaded());
  }

  @Test
  public void testAfterPayment2() {
    BoardController boardController = null;
    try {
      boardController = new BoardController(false);
    } catch (RemoteException ignore) {
      //
    }
    TurnController turnController = new TurnController(boardController);
    Player player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    Player player2 = new Player("test2", PlayerColor.YELLOW, boardController.getBoard());
    boardController.getBoard().addPlayer(player);
    boardController.getBoard().addPlayer(player2);

    boardController.getBoard().setCurrentPlayer(PlayerColor.YELLOW);

    Weapon weapon2 = new Weapon(1,2,1, AmmoColor.YELLOW,"test2","w");
    Square square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square2 = new Square(0, 1, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square3 = new Square(1, 0, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    Square square4 = new Square(1, 1, SquareColor.RED, new BorderType[]{WALL, WALL, WALL, WALL}, boardController.getBoard());
    WeaponBuy weaponBuy2 = new WeaponBuy(weapon2);
    square1.setSpawnPoint(true);
    square1.addWeapon(weapon2);
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().setSquare(square2);
    boardController.getBoard().setSquare(square3);
    boardController.getBoard().setSquare(square4);
    weaponBuy2.afterPaymentCompleted(turnController, boardController.getBoard(), player);
    assertFalse(square1.getWeapons().contains(weapon2));
  }
}