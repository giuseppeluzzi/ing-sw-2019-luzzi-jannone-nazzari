package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import java.util.List;

/**
 * Static class used to print a game map in TUI. It consists of several methods which contribute
 * to build a "print matrix". The print matrix is an array of rows; each row is an array of
 * characters. For characters, {@code String} is used instead of {@code char} in order to include
 * ANSI escape sequences for setting colors.
 */
public final class MapPrinter {

  private static final int SQUARE_WIDTH = 13;
  private static final int SQUARE_HEIGHT = 6;
  private static final int DOOR_HEIGHT = 2;
  private static final int DOOR_WIDTH = 5;
  private static final int DASHBOARD_WIDTH = 22;
  private static final int DASHBOARD_HEIGHT = 4;
  private static final String HORIZONTAL_LINE = "━";
  private static final String VERTICAL_LINE = "┃";
  private static final String PLAYER_ICON = "⚑";
  private static final String SPAWN_POINT = "⊡";
  private static final String TAG_SYMBOL = "✦";
  private static final String DAMAGE_SYMBOL = "✚";

  private MapPrinter() {
    throw new IllegalStateException("MapPrinter cannot be instantiated");
  }

  /**
   * Generates the corner of a square and adds it to the print matrix. The corner style depends
   * on the borders of the square:
   * <ul><li>no corner if there are no borders (BorderType is AIR);</li>
   * <li>a straight line if only one of the two borders exist;</li>
   * <li>an actual corner shape if both of the two borders exist.</li></ul>
   * @param map the print matrix that will be updated
   * @param square the square to consider
   * @param cornerType the type of corner to draw
   */
  private static void drawCorner(String[][] map, Square square, CornerType cornerType) {
    int x;
    int y;
    Direction horizontalSide;
    Direction verticalSide;
    switch (cornerType) {
      case TOP_LEFT_CORNER:
        x = square.getPosX() * SQUARE_WIDTH + 2;
        y = square.getPosY() * SQUARE_HEIGHT + 2;
        horizontalSide = Direction.WEST;
        verticalSide = Direction.NORTH;
        break;
      case TOP_RIGHT_CORNER:
        x = (square.getPosX() * SQUARE_WIDTH) + SQUARE_WIDTH + 1;
        y = square.getPosY() * SQUARE_HEIGHT + 2;
        horizontalSide = Direction.EAST;
        verticalSide = Direction.NORTH;
        break;
      case BOTTOM_LEFT_CORNER:
        x = square.getPosX() * SQUARE_WIDTH + 2;
        y = (square.getPosY() * SQUARE_HEIGHT) + SQUARE_HEIGHT + 1;
        horizontalSide = Direction.WEST;
        verticalSide = Direction.SOUTH;
        break;
      case BOTTOM_RIGHT_CORNER:
        x = (square.getPosX() * SQUARE_WIDTH) + SQUARE_WIDTH + 1;
        y = (square.getPosY() * SQUARE_HEIGHT) + SQUARE_HEIGHT + 1;
        horizontalSide = Direction.EAST;
        verticalSide = Direction.SOUTH;
        break;
      default:
        throw new IllegalStateException("Invalid corner");
    }
    if (square.getEdge(horizontalSide) == BorderType.AIR && square.getEdge(verticalSide) == BorderType.AIR) {
      map[x][y] = " ";
    } else if (square.getEdge(horizontalSide) == BorderType.AIR && square.getEdge(verticalSide) != BorderType.AIR) {
      map[x][y] = square.getColor().getAnsiColor() + HORIZONTAL_LINE;
    } else if (square.getEdge(horizontalSide) != BorderType.AIR && square.getEdge(verticalSide) == BorderType.AIR) {
      map[x][y] = square.getColor().getAnsiColor() + VERTICAL_LINE;
    } else {
      map[x][y] = square.getColor().getAnsiColor() + cornerType.toString();
    }
  }

  /**
   * Generates the edge of a square in a certain direction and adds it to the print matrix. If the
   * border of the square in that direction is a wall, the edge will be a straight line. If the
   * square has no border in that direction (BorderType is AIR), no visible edge will be added.
   * If the border of the square in that direction is a door, a special edge will be drawn
   * consisting of five parts:
   * <ul><li>a straight line as a continuation of the wall;</li>
   * <li>the first corner of the door;</li>
   * <li>the empty part of the door;</li>
   * <li>the second corner of the door;</li>
   * <li>a straight line as a continuation of the wall.</li></ul>
   * @param map the print matrix that will be updated
   * @param square the square to consider
   * @param direction the direction of the edge to draw
   * @param pos the current x or y position
   */
  private static void drawEdge(String[][] map, Square square, Direction direction, int pos) {
    int x;
    int y;
    int squareDimension;
    int doorDimension;
    String line;
    CornerType corner1;
    CornerType corner2;

    switch (direction) {
      case WEST:
        x = square.getPosX() * SQUARE_WIDTH + 2;
        y = square.getPosY() * SQUARE_HEIGHT + 2 + pos;
        squareDimension = SQUARE_HEIGHT;
        doorDimension = DOOR_HEIGHT;
        line = VERTICAL_LINE;
        corner1 = CornerType.BOTTOM_RIGHT_CORNER;
        corner2 = CornerType.TOP_RIGHT_CORNER;
        break;
      case EAST:
        x = square.getPosX() * SQUARE_WIDTH + SQUARE_WIDTH + 1;
        y = square.getPosY() * SQUARE_HEIGHT + 2 + pos;
        squareDimension = SQUARE_HEIGHT;
        doorDimension = DOOR_HEIGHT;
        line = VERTICAL_LINE;
        corner1 = CornerType.BOTTOM_LEFT_CORNER;
        corner2 = CornerType.TOP_LEFT_CORNER;
        break;
      case NORTH:
        x = square.getPosX() * SQUARE_WIDTH + 2 + pos;
        y = square.getPosY() * SQUARE_HEIGHT + 2;
        squareDimension = SQUARE_WIDTH;
        doorDimension = DOOR_WIDTH;
        line = HORIZONTAL_LINE;
        corner1 = CornerType.BOTTOM_RIGHT_CORNER;
        corner2 = CornerType.BOTTOM_LEFT_CORNER;
        break;
      case SOUTH:
        x = square.getPosX() * SQUARE_WIDTH + 2 + pos;
        y = square.getPosY() * SQUARE_HEIGHT + SQUARE_HEIGHT + 1;
        squareDimension = SQUARE_WIDTH;
        doorDimension = DOOR_WIDTH;
        line = HORIZONTAL_LINE;
        corner1 = CornerType.TOP_RIGHT_CORNER;
        corner2 = CornerType.TOP_LEFT_CORNER;
        break;
      default:
        throw new IllegalStateException("Invalid edge direction");
    }

    if (square.getEdge(direction) == BorderType.DOOR) {
      // Door drawing
      if (pos + 1 < (squareDimension - doorDimension) / 2) {
        // Wall part before the door
        map[x][y] = square.getColor().getAnsiColor() + line;
      } else if (pos + 1 == (squareDimension - doorDimension) / 2) {
        // The door edge
        map[x][y] = square.getColor().getAnsiColor() + corner1.toString();
      } else if (pos == ((squareDimension - doorDimension) / 2) + doorDimension) {
        // The other door edge
        map[x][y] = square.getColor().getAnsiColor() + corner2.toString();
      } else if (pos > ((squareDimension - doorDimension) / 2) + doorDimension) {
        // Wall part after the door
        map[x][y] = square.getColor().getAnsiColor() + line;
      } else {
        // The door itself
        map[x][y] = " ";
      }
    } else if (square.getEdge(direction) == BorderType.WALL) {
      // Wall drawing
      map[x][y] = square.getColor().getAnsiColor() + line;
    } else {
      // Empty edge drawing
      map[x][y] = " ";
    }
  }

  /**
   * Generates the attributes inside a square and adds it to the print matrix. An attribute can be
   * either a spawn point or an ammo card. The latter is represented with its three-letter code.
   * @see it.polimi.se2019.adrenalina.model.AmmoCard#toString
   * @param map the print matrix that will be updated
   * @param square the square to consider
   */
  private static void drawAttributes(String [][] map, Square square) {
    int centerX = (square.getPosX() * SQUARE_WIDTH) + 2 + (SQUARE_WIDTH - 1) / 2;
    int centerY = (square.getPosY() * SQUARE_HEIGHT) + SQUARE_HEIGHT;
    if (square.isSpawnPoint()) {
      map[centerX][centerY] = square.getColor().getAnsiColor() + SPAWN_POINT;
    } else if (square.getAmmoCard() != null) {
      map[centerX - 1][centerY] = ANSIColor.RESET.toString() + square.getAmmoCard().toString().substring(0, 1);
      map[centerX][centerY] = ANSIColor.RESET.toString() + square.getAmmoCard().toString().substring(1, 2);
      map[centerX + 1][centerY] = ANSIColor.RESET.toString() + square.getAmmoCard().toString().substring(2, 3);
    }
  }

  /**
   * Generates the characters that make up a square and adds them to the print matrix. It resorts
   * to helper methods in order to draw corners and edges.
   * @param map the print matrix that will be updated
   * @param square the square to consider
   * @param x the x coordinate of the character relative to the square
   * @param y the y coordinate of the character relative to the square
   */
  private static void drawSquareCharacter(String[][] map, Square square, int x, int y) {
    if (x == 0 && y == 0) {
      drawCorner(map, square, CornerType.TOP_LEFT_CORNER);
    } else if (x == SQUARE_WIDTH - 1 && y == 0) {
      drawCorner(map, square, CornerType.TOP_RIGHT_CORNER);
    } else if (x == 0 && y == SQUARE_HEIGHT - 1) {
      drawCorner(map, square, CornerType.BOTTOM_LEFT_CORNER);
    } else if (x == SQUARE_WIDTH - 1 && y == SQUARE_HEIGHT - 1) {
      drawCorner(map, square, CornerType.BOTTOM_RIGHT_CORNER);
    } else if (x == 0) {
      drawEdge(map, square, Direction.WEST, y);
    } else if (x == SQUARE_WIDTH - 1) {
      drawEdge(map, square, Direction.EAST, y);
    } else if (y == 0) {
      drawEdge(map, square, Direction.NORTH, x);
    } else if (y == SQUARE_HEIGHT - 1) {
      drawEdge(map, square, Direction.SOUTH, x);
    } else {
      // Inner whitespace fill-up
      map[square.getPosX() * SQUARE_WIDTH + 2 + x][square.getPosY() * SQUARE_HEIGHT + 2 + y] = " ";
    }
  }

  /**
   * Generates a square and adds it to the print matrix. It uses helper methods to draw each
   * character and the square's attributes.
   * @param map the print matrix that will be updated
   * @param square the square to draw
   */
  private static void drawSquare(String[][] map, Square square) {
    for (int x = 0; x < SQUARE_WIDTH; x++) {
      for (int y = 0; y < SQUARE_HEIGHT; y++) {
        drawSquareCharacter(map, square, x, y);
      }
    }
    drawAttributes(map, square);
  }

  /**
   * Generates player icons inside a square and adds them to the print matrix.
   * @param map the print matrix that will be updated
   * @param square the square to consider
   */
  private static void drawPlayers(String[][] map, Square square) {
    int x;
    int y;
    for (int i = 0; i < square.getPlayers().size(); i++) {
      if (i == 0 || i == 3) {
        x = (SQUARE_WIDTH - 1) / 4;
      } else if (i == 1 || i == 4) {
        x = SQUARE_WIDTH - (SQUARE_WIDTH - 1) / 4 - 1;
      } else {
        x = (SQUARE_WIDTH - 1) / 2;
      }
      if (i <= 1) {
        y = SQUARE_HEIGHT / 6;
      } else if (i == 2) {
        y = SQUARE_HEIGHT / 3;
      } else {
        y = SQUARE_HEIGHT / 2;
      }
      map[square.getPosX() * SQUARE_WIDTH + 2 + x][square.getPosY() * SQUARE_HEIGHT + 2 + y] = square.getPlayers().get(i).getColor().getAnsiColor() + PLAYER_ICON;
    }
  }

  /**
   * Generates coordinates numbers and adds them to the print matrix.
   * @param map the print matrix that will be updated
   */
  private static void drawCoordinates(String[][] map) {
    int initXOffset = 2 + (SQUARE_WIDTH - 1) / 2;
    int initYOffset = 2 + SQUARE_HEIGHT / 2;
    for (Integer i = 0; i < 4; i++) {
      map[initXOffset][0] = ANSIColor.RESET + i.toString();
      initXOffset += SQUARE_WIDTH;
    }
    for (Integer i = 0; i < 3; i++) {
      map[0][initYOffset] = ANSIColor.RESET + i.toString();
      initYOffset += SQUARE_HEIGHT;
    }
  }

  private static void drawDashboard(String[][] map, int num, Board board, Player player, PlayerColor ownerColor) {
    ANSIColor color;
    if (board.getCurrentPlayer() == player.getColor()) {
      color = player.getColor().getAnsiColor();
    } else {
      switch (player.getColor()) {
        case GREEN:
          color = ANSIColor.DIM_GREEN;
          break;
        case BLUE:
          color = ANSIColor.DIM_BLUE;
          break;
        case PURPLE:
          color = ANSIColor.DIM_MAGENTA;
          break;
        case GREY:
          color = ANSIColor.DIM_WHITE;
          break;
        case YELLOW:
          color = ANSIColor.DIM_YELLOW;
          break;
        default:
          throw new IllegalStateException("Invalid PlayerColor");
      }
    }
    int baseX = 4 * SQUARE_WIDTH + 4;
    int posX = baseX;
    int posY = num * DASHBOARD_HEIGHT;
    map[posX][posY] = color.toString() + CornerType.TOP_LEFT_CORNER;
    map[baseX + DASHBOARD_WIDTH - 1][posY] = color.toString() + CornerType.TOP_RIGHT_CORNER;
    posX++;
    String title = " " + player.getName() + " (" + player.getScore() + ") ";
    for (int i = 0; i < (DASHBOARD_WIDTH - title.length()) / 2; i++) {
      map[posX][posY] = color + HORIZONTAL_LINE;
      posX++;
    }
    for (char c : title.toCharArray()) {
      map[posX][posY] = color.toString() + c;
      posX++;
    }
    while (posX < baseX + DASHBOARD_WIDTH - 1) {
      map[posX][posY] = color + HORIZONTAL_LINE;
      posX++;
    }
    posX = baseX;
    posY++;
    map[posX][posY] = color + VERTICAL_LINE;
    posX++;
    map[posX][posY] = " ";
    posX++;
    for (Weapon weapon : player.getWeapons()) {
      if (! weapon.isLoaded() || player.getColor() == ownerColor) {
        map[posX][posY] = color + weapon.getSymbol();
      } else {
        map[posX][posY] = color + "*";
      }
      posX++;
    }
    for (int i = 0; i < 4 - player.getWeapons().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }
    map[posX][posY] = color + "|";
    posX++;
    map[posX][posY] = " ";
    posX++;
    for (PlayerColor tagColor : player.getTags()) {
      map[posX][posY] = tagColor + TAG_SYMBOL;
      posX++;
    }
    for (int i = 0; i < 13 - player.getTags().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }
    map[posX][posY] = color + VERTICAL_LINE;
    posX = baseX;
    posY++;
    map[posX][posY] = color + VERTICAL_LINE;
    posX++;
    map[posX][posY] = " ";
    posX++;
    for (PowerUp powerUp : player.getPowerUps()) {
      if (player.getColor() == ownerColor) {
        map[posX][posY] = color + powerUp.getSymbol();
      } else {
        map[posX][posY] = color + "P";
      }
      posX++;
    }
    for (int i = 0; i < 4 - player.getPowerUps().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }
    map[posX][posY] = color + "|";
    posX++;
    map[posX][posY] = " ";
    posX++;
    for (PlayerColor damageColor : player.getDamages()) {
      map[posX][posY] = damageColor + DAMAGE_SYMBOL;
      posX++;
    }
    for (int i = 0; i < 13 - player.getDamages().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }
    map[posX][posY] = color + VERTICAL_LINE;
    posX = baseX;
    posY++;
    map[posX][posY] = color.toString() + CornerType.BOTTOM_LEFT_CORNER;
    posX++;
    map[posX][posY] = color.toString() + HORIZONTAL_LINE;
    posX++;
    map[posX][posY] = " ";
    posX++;
    for (char c : Integer.toString(player.getKillScore()).toCharArray()) {
      map[posX][posY] = color.toString() + c;
      posX++;
    }
    map[posX][posY] = " ";
    posX++;
    for (int i = 0; i < DASHBOARD_WIDTH - 13 - player.getKillScore() / 10; i++) {
      map[posX][posY] = color.toString() + HORIZONTAL_LINE;
      posX++;
    }
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = ANSIColor.RED.toString() + player.getAmmo(AmmoColor.RED);
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = ANSIColor.YELLOW.toString() + player.getAmmo(AmmoColor.YELLOW);
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = ANSIColor.BLUE.toString() + player.getAmmo(AmmoColor.BLUE);
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = color.toString() + CornerType.BOTTOM_RIGHT_CORNER;

    /* TODO:
       - sistemare i colori (brillanti/spenti, soprattutto quando si devono printare cose con colori statici)
       - scorporare per complessità
       - rendere velori dipendenti dalle costanti
       - funzione che stampa tutte le dashboard
       - aggiornare i commenti
       - testing
     */
  }

  /**
   * Generates a print matrix.
   * @param board the board with the game map to draw
   * @param ownerColor the color of the player who will see the printed game board
   * @return a print matrix for the game board
   */
  static String[][] buildMap(Board board, PlayerColor ownerColor) {
    String[][] map = new String[4 * SQUARE_WIDTH + DASHBOARD_WIDTH + 5][3 * SQUARE_HEIGHT + DASHBOARD_HEIGHT + 5];
    for (Square square : board.getSquares()) {
      drawSquare(map, square);
      drawPlayers(map, square);
    }
    drawCoordinates(map);
    int num = 0;
    for (Player player : board.getPlayers()) {
      drawDashboard(map, num, board, player, ownerColor);
      num++;
    }
    return map;
  }

  /**
   * Prints a game board.
   * @param board the board with the game map to print
   * @param ownerColor the color of the player who will see the printed game board
   */
  static void print(Board board, PlayerColor ownerColor) {
    String[][] map = buildMap(board, ownerColor);
    for (int y = 0; y < 3 * SQUARE_HEIGHT + DASHBOARD_HEIGHT + 5; y++) {
      for (int x = 0; x < 4 * SQUARE_WIDTH + DASHBOARD_WIDTH + 5; x++) {
        if (map[x][y] != null) {
          Log.print(map[x][y]);
        } else {
          Log.print(" ");
        }
      }
      Log.print("\n");
    }
    Log.print(ANSIColor.RESET.toString());
  }
}
