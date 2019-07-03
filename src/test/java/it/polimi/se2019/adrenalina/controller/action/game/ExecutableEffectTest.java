package it.polimi.se2019.adrenalina.controller.action.game;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootSquareAction;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

public class ExecutableEffectTest {


 @Test
 public void testExecuteWeapon() {
   Board board = new Board();
   TurnController turnController = new TurnController(null);
   Player player = new Player("test", PlayerColor.GREEN, null);
   Player player2 = new Player("test2", PlayerColor.GREY, null);
   Player player3 = new Player("test3", PlayerColor.PURPLE, null);
   Square square  = new Square(2, 2, SquareColor.RED, WALL, WALL, WALL, WALL, null);
   Square square2 = new Square(2, 3, SquareColor.RED, WALL, WALL, WALL, WALL, null);
   Weapon weapon = new Weapon(1,0,0, AmmoColor.BLUE,"test","f");
   ExecutableEffect executableEffect = new ExecutableEffect(turnController, player, weapon, new ShootAction(1,1,1, false));
   weapon.setTargetHistory(0,player);
   weapon.setTargetHistory(1,player2);
   player.setSquare(square);
   player2.setSquare(square2);
   player3.setSquare(square);
   executableEffect.execute(board);
   assertFalse(weapon.isLoaded());
   weapon.setLoaded(true);
   ExecutableEffect executableEffect2 = new ExecutableEffect(turnController, player, weapon, new ShootRoomAction(1,1,0));
   executableEffect2.execute(board);
   assertFalse(weapon.isLoaded());
   ExecutableEffect executableEffect3 = new ExecutableEffect(turnController, player, weapon, new ShootSquareAction(1,1,0,0,new int[] {}));
   weapon.setLoaded(true);
   executableEffect3.execute(board);
   assertFalse(weapon.isLoaded());
 }
 
}