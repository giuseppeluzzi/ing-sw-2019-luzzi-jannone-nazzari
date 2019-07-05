package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AttackControllerTest {
  private BoardController boardController;
  private TurnController turnController;
  private Player player;
  private Player player2;
  private Weapon weapon;

  @Before
  public void setAttackController() {

    try {
      boardController = new BoardController(false);
      boardController.setAttackController(new AttackController(boardController));
      turnController = spy(new TurnController(boardController));
      doNothing().when(turnController).executeGameActionQueue();
      boardController.setTurnController(turnController);
    } catch (RemoteException ignore) {
      //
    }

    player = new Player("test", PlayerColor.GREEN, boardController.getBoard());
    player2 = new Player("test2", PlayerColor.YELLOW, boardController.getBoard());
    weapon = new Weapon(0,0,0,AmmoColor.YELLOW, "test", "q");
    boardController.getBoard().addPlayer(player);
    boardController.getBoard().addPlayer(player2);
    player.setCurrentExecutable(weapon);
    weapon.setCurrentSelectTargetSlot(0);
    boardController.getBoard().setCurrentPlayer(PlayerColor.YELLOW);
  }

  @Test
  public void testGetReloadablegWeapons() {
    Weapon weapon2 = new Weapon(0,0,0,AmmoColor.YELLOW, "test2", "w");
    Weapon weapon3 = new Weapon(0,1 ,0,AmmoColor.YELLOW, "test3", "e");
    player.addWeapon(weapon);
    player.addWeapon(weapon2);
    player.addWeapon(weapon3);
    player.addAmmo(AmmoColor.YELLOW, 3);
    weapon.setLoaded(false);
    weapon2.setLoaded(false);
    weapon3.setLoaded(false);
    assertEquals(1, boardController.getAttackController().getReloadableWeapons(player,weapon).size());
  }

  @Test
  public void testSelectPlayer() {
    SelectPlayerEvent event = new SelectPlayerEvent(PlayerColor.GREEN, PlayerColor.YELLOW);
    SelectPlayerEvent event2 = new SelectPlayerEvent(PlayerColor.BLUE, PlayerColor.PURPLE);
    boardController.getAttackController().update(event2);
    boardController.getAttackController().update(event);
    assertEquals(weapon.getTargetHistory(0).getName(), player2.getName());
  }

  @Test
  public void testSkipSelection() {
    SkipSelectionEvent event = new SkipSelectionEvent(PlayerColor.GREEN);
    SkipSelectionEvent event2 = new SkipSelectionEvent(PlayerColor.BLUE);
    boardController.getAttackController().update(event2);
    boardController.getAttackController().update(event);
    assertTrue(player.getCurrentExecutable().skipUntilSelect());
  }

  @Test
  public void testSelectSquare() {
    SelectSquareEvent event = new SelectSquareEvent(PlayerColor.GREEN, 0, 0);
    SelectSquareEvent event2 = new SelectSquareEvent(PlayerColor.BLUE, 0, 0);
    boardController.getBoard().setSquare(new Square(0, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController.getBoard()));
    boardController.getAttackController().update(event2);
    boardController.getAttackController().update(event);
    assertEquals(SquareColor.RED.getAnsiColor(), player.getCurrentExecutable().getTargetHistory(0).getAnsiColor());
  }

  @Test
  public void testSelectDirection() {
    SelectDirectionEvent event = new SelectDirectionEvent(PlayerColor.GREEN, Direction.NORTH);
    SelectDirectionEvent event2 = new SelectDirectionEvent(PlayerColor.BLUE, Direction.NORTH);
    boardController.getAttackController().update(event2);
    boardController.getAttackController().update(event);
    assertEquals(Direction.NORTH, player.getCurrentExecutable().getLastUsageDirection());
  }

  @Test
  public void testSquareMove() {
    SquareMoveSelectionEvent event = new SquareMoveSelectionEvent(PlayerColor.GREEN, 1, 1);
    SquareMoveSelectionEvent event2 = new SquareMoveSelectionEvent(PlayerColor.BLUE, 1, 1);

    boardController.getBoard().setSquare(new Square(0, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController.getBoard()));
    boardController.getBoard().setSquare(new Square(1, 1, SquareColor.YELLOW, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController.getBoard()));
    boardController.getAttackController().update(event2);
    boardController.getAttackController().update(event);
    assertEquals(SquareColor.YELLOW, player.getSquare().getColor());
  }

  @Test
  public void testSpawnPointDamage() {
    BoardController boardController2 = null;
    AttackController attackController2 = null;
    TurnController turnController2 = null;
    try {
      boardController2 = new BoardController(true);
      attackController2 = new AttackController(boardController2);
      turnController2 = spy(new TurnController(boardController2));
      boardController2.setTurnController(turnController2);
      boardController2.setAttackController(attackController2);
      doNothing().when(turnController2).executeGameActionQueue();

    } catch (RemoteException ignore) {
      //
    }
    Square square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController2.getBoard());
    Square square2 = new Square(1, 0, SquareColor.YELLOW, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController2.getBoard());
    Square square3 = new Square(2, 0, SquareColor.BLUE, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController2.getBoard());

    square1.setSpawnPoint(true);
    square2.setSpawnPoint(true);
    square3.setSpawnPoint(true);

    boardController2.getBoard().setSquare(square1);
    boardController2.getBoard().setSquare(square2);
    boardController2.getBoard().setSquare(square3);

    boardController2.getBoard().addPlayer(player);
    boardController2.getBoard().addPlayer(player2);
    boardController2.getBoard().setCurrentPlayer(PlayerColor.YELLOW);

    SpawnPointDamageEvent event = new SpawnPointDamageEvent(PlayerColor.GREEN, AmmoColor.RED);
    boardController2.getAttackController().update(event);

    assertEquals(1, ((DominationBoard) boardController2.getBoard()).getRedDamages().size());
  }

  @Test
  public void testEventGeneric() {
    Square square1 = new Square(0, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController.getBoard());
    Square square2 = new Square(1, 0, SquareColor.YELLOW, new BorderType[]{BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL}, boardController.getBoard());
    Event event = new SquareMoveSelectionEvent(PlayerColor.GREEN, 1,0);
    boardController.getBoard().setSquare(square1);
    boardController.getBoard().setSquare(square2);
    player.setSquare(square1);
    boardController.getAttackController().update(event);
    assertEquals(player.getSquare(), square2);
    assertEquals(boardController.getAttackController().hashCode(), boardController.hashCode());
  }

  @Test
  public void testReloadWeaponEvent() {
    weapon.setLoaded(false);
    Weapon weapon2 = new Weapon(0,0,1,AmmoColor.YELLOW,"test2","g", false);
    player.addWeapon(weapon);
    boardController.getBoard().addWeapon(weapon);
    boardController.getBoard().addWeapon(weapon2);
    player.addAmmo(AmmoColor.YELLOW, 2);
    PlayerReloadEvent event = new PlayerReloadEvent(PlayerColor.GREEN, "test");
    boardController.getAttackController().update(event);
    assertEquals(1, boardController.getTurnController().getActionQueueSize());
    boardController.getTurnController().clearActionsQueue();
    player.addWeapon(weapon2);
    weapon2.setLoaded(false);
    weapon.setLoaded(false);
    boardController.getAttackController().update(event);
    assertEquals(2, boardController.getTurnController().getActionQueueSize());
  }
}