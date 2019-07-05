package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootRoomAction;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.utils.Log;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

public class CheckRespawnTest {

  private BoardController boardController;
  private TurnController turnController;
  private Player player1;
  private Player player2;
  private Player player3;
  private Weapon weapon;

  @Before
  public void set() {
    try {
      boardController = new BoardController(true);
    } catch (RemoteException e) {
      Log.exception(e);
    }
    turnController = new TurnController(boardController);
    boardController.getBoard().setSquare(new Square(0,0, SquareColor.YELLOW, BorderType.WALL,BorderType.AIR,BorderType.AIR,BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(0,1, SquareColor.YELLOW, BorderType.AIR,BorderType.AIR,BorderType.AIR,BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(0,2, SquareColor.YELLOW, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(1,0, SquareColor.YELLOW, BorderType.WALL,BorderType.AIR,BorderType.AIR,BorderType.AIR, boardController.getBoard()));

    boardController.getBoard().setSquare(new Square(1,1, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.AIR,BorderType.AIR, boardController.getBoard()));

    boardController.getBoard().setSquare(new Square(1,2, SquareColor.BLUE, BorderType.AIR,BorderType.WALL,BorderType.AIR,BorderType.AIR, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(2,0, SquareColor.BLUE, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.WALL, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(2,1, SquareColor.BLUE, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.AIR, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(2,2, SquareColor.BLUE, BorderType.AIR,BorderType.WALL,BorderType.WALL,BorderType.AIR, boardController.getBoard()));

    player1 = new Player("P1", PlayerColor.GREEN, boardController.getBoard());
    player2 = new Player("P2", PlayerColor.GREY, boardController.getBoard());
    player3 = new Player("P3", PlayerColor.PURPLE, boardController.getBoard());
    player1.setSquare(boardController.getBoard().getSquare(1,1));
    player2.setSquare(boardController.getBoard().getSquare(0,1));
    player3.setSquare(boardController.getBoard().getSquare(1,2));
    boardController.getBoard().addPlayer(player1);
    boardController.getBoard().addPlayer(player2);
    boardController.getBoard().addPlayer(player3);
    weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","f");
    weapon.setTargetHistory(0, player1);
    weapon.setTargetHistory(1, player2);
    weapon.setTargetHistory(2, player3);
    boardController.getBoard().getSquare(1,1).setSpawnPoint(true);
  }

  @Test
  public void testGetDeadPlayers() {
    Board board = new Board();
    Player player1 = new Player("test", PlayerColor.GREEN, board);
    Player player2 = new Player("test2", PlayerColor.GREY, board);
    board.addPlayer(player1);
    board.addPlayer(player2);
    player1.addDamages(PlayerColor.PURPLE, ServerConfig.getInstance().getDeathDamages(), false);
    assertEquals(1, CheckRespawn.getDeadPlayers(board).size());
  }

  @Test
  public void testExecute() {
    player1.addDamages(player2.getColor(), ServerConfig.getInstance().getDeathDamages(), false);
    player1.setStatus(PlayerStatus.WAITING);
    CheckRespawn checkRespawn = new CheckRespawn(turnController, player1);
    checkRespawn.execute(boardController.getBoard());
    assertEquals(PlayerStatus.PLAYING, player1.getStatus());
  }
}