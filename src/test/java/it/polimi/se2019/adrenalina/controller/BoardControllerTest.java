package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.assertEquals;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.List;
import org.junit.Test;

public class BoardControllerTest {

  @Test
  public void testGetMapByPlayerNumber() {
    try {
      BoardController boardController = new BoardController(false);
      boardController.addPlayer(new Player("P1", PlayerColor.GREEN, null));
      boardController.addPlayer(new Player("P2", PlayerColor.BLUE, null));
      boardController.addPlayer(new Player("P3", PlayerColor.YELLOW, null));

      List<GameMap> validMaps = boardController.getValidMaps(3);
      assertEquals(3, validMaps.size());
    } catch (Exception ignored) {
      //
    }
  }

  @Test
  public void testShowMap() throws RemoteException {
    BoardController boardController = new BoardController(false);
    GameMap gameMap = boardController.getValidMaps(3).get(0);

    final int MAX_VERT_TILES = 24; //rows.
    final int MAX_HORIZ_TILES = 80; //cols.

    final int START_X = 0;
    final int START_Y = 0;

    int currentX = 0;
    int currentY = 0;

    Square[][] squares = new Square[4][3];
    String[][] map = new String[MAX_HORIZ_TILES][MAX_VERT_TILES];

    for (Square square : gameMap.getSquares()) {
      squares[square.getPosX()][square.getPosY()] = square;
    }

    for (int x = 0; x < MAX_HORIZ_TILES; x++) {
      for (int y = 0; y < MAX_VERT_TILES; y++) {
        map[x][y] = " ";
      }
    }

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        if (squares[x][y] != null) {
          for (int tX = 1; tX < 10; tX++) {
            map[currentX+tX][currentY] = "━";
            map[currentX+tX][currentY+10] = "━";
            map[currentX][currentY+tX] = "┃";
            map[currentX+9][currentY+tX] = "┃";
          }
        }
      }
    }
    map[currentX][currentY] = "┏";
    map[currentX+11][currentY] = "┓";
    map[currentX][currentY+10] = "┗";
    map[currentX+10][currentY+10] = "┛";

    StringBuilder currentLine;
    for (int y = 0; y < MAX_VERT_TILES; y++) {
      currentLine = new StringBuilder(MAX_HORIZ_TILES);
      for (int x = 0; x < MAX_VERT_TILES; x++) {
        currentLine.append(map[x][y]);
      }
      Log.println(currentLine.toString());
    }

    /*Log.println("┏━━━━━━━━━┓\n"
        + "┃\n"
        + "┃\n"
        + "┃\n"
        + "┃\n"
        + "┃\n"
        + "┗");*/
  }
}
