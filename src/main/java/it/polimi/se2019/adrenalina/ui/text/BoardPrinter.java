package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.Configuration;
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
public final class BoardPrinter {

  private static final int SQUARE_WIDTH = Configuration.getInstance().getTuiSquareWidth();
  private static final int SQUARE_HEIGHT = Configuration.getInstance().getTuiSquareHeight();
  private static final int DOOR_HEIGHT = Configuration.getInstance().getTuiDoorHeight();
  private static final int DOOR_WIDTH = Configuration.getInstance().getTuiDoorWidth();
  private static final int DASHBOARD_WIDTH = Configuration.getInstance().getTuiDashboardWidth();
  private static final int DASHBOARD_HEIGHT = Configuration.getInstance().getTuiDashboardHeight();
  private static final String HORIZONTAL_LINE = Configuration.getInstance().getTuiHorizontalLine();
  private static final String VERTICAL_LINE = Configuration.getInstance().getTuiVerticalLine();
  private static final String PLAYER_ICON = Configuration.getInstance().getTuiPlayerIcon();
  private static final String SPAWN_POINT_ICON = Configuration.getInstance().getTuiSpawnPointIcon();
  private static final String TAG_ICON = Configuration.getInstance().getTuiTagIcon();
  private static final String DAMAGE_ICON = Configuration.getInstance().getTuiDamageIcon();

  private BoardPrinter() {
    throw new IllegalStateException("BoardPrinter cannot be instantiated");
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
      map[centerX][centerY] = square.getColor().getAnsiColor() + SPAWN_POINT_ICON;
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

  /**
   * Generates weapon icons and adds them to the print matrix. Returns the current X cursor position.
   * @param map the print matrix that will be updated
   * @param weapons the list of weapons to print - only includes unloaded weapons for enemies
   * @param playerColor the color of the player who owns the dashboard
   * @param weaponCount the total number of weapons to print
   * @param posX cursor X position
   * @param posY cursor Y position
   * @return the updated cursor X position
   */
  private static int printDashboardWeapons(String[][] map, List<Weapon> weapons, String playerColor, int weaponCount, int posX, int posY) {
    for (Weapon weapon : weapons) {
      map[posX][posY] = playerColor + weapon.getSymbol();
      posX++;
    }
    for (int i = 0; i < weaponCount - weapons.size(); i++) {
      map[posX][posY] = playerColor + "*";
      posX++;
    }
    for (int i = 0; i < 4 - weaponCount; i++) {
      map[posX][posY] = " ";
      posX++;
    }
    return posX;
  }

  /**
   * Generates powerUps icons and adds them to the print matrix. Returns the current X cursor position.
   * @param map the print matrix that will be updated
   * @param powerUps the list of powerUps to print - empty for enemies
   * @param playerColor the color of the player who owns the dashboard
   * @param powerUpCount the total number of powerUps to print
   * @param posX cursor X position
   * @param posY cursor Y position
   * @return the updated cursor X position
   */
  private static int printDashboardPowerUps(String[][] map, List<PowerUp> powerUps, String playerColor, int powerUpCount, int posX, int posY) {
    for (PowerUp powerUp : powerUps) {
      map[posX][posY] = powerUp.getColor().getAnsiColor() + powerUp.getSymbol();
      posX++;
    }
    for (int i = 0; i < powerUpCount - powerUps.size(); i++) {
      map[posX][posY] = playerColor + "+";
      posX++;
    }
    for (int i = 0; i < 4 - powerUpCount; i++) {
      map[posX][posY] = " ";
      posX++;
    }
    return posX;
  }

  private static void drawDashboard(String[][] map, int num, Board board, Player player) {
    boolean dim = board.getCurrentPlayer() != player.getColor();
    String playerColor = player.getColor().getAnsiColor().toString(dim);
    int baseX = 4 * SQUARE_WIDTH + 4;
    int posX = baseX;
    int posY = num * DASHBOARD_HEIGHT;

    // Top corners
    map[posX][posY] = playerColor + CornerType.TOP_LEFT_CORNER;
    map[baseX + DASHBOARD_WIDTH - 1][posY] = playerColor + CornerType.TOP_RIGHT_CORNER + ANSIColor.RESET;
    posX++;

    // Building player name & score (dashboard title)
    String title = " " + player.getName() + " (" + player.getScore() + ") ";

    // Left part of top edge
    for (int i = 0; i < (DASHBOARD_WIDTH - title.length()) / 2; i++) {
      map[posX][posY] = playerColor + HORIZONTAL_LINE;
      posX++;
    }

    // Player name & score (dashboard title)
    for (char c : title.toCharArray()) {
      map[posX][posY] = playerColor + c;
      posX++;
    }

    // Right part of top edge
    while (posX < baseX + DASHBOARD_WIDTH - 1) {
      map[posX][posY] = playerColor + HORIZONTAL_LINE;
      posX++;
    }

    posX = baseX;
    posY++;

    // Line 1 side edges
    map[posX][posY] = playerColor + VERTICAL_LINE;
    map[baseX + DASHBOARD_WIDTH - 1][posY] = playerColor + VERTICAL_LINE + ANSIColor.RESET;
    posX++;

    map[posX][posY] = " ";
    posX++;

    // Weapons
    posX = printDashboardWeapons(map, player.getWeapons(), playerColor, player.getWeaponCount(), posX, posY);

    map[posX][posY] = playerColor + "|";
    posX++;
    map[posX][posY] = " ";
    posX++;

    // Tags
    for (PlayerColor tagColor : player.getTags()) {
      map[posX][posY] = tagColor.getAnsiColor().toString(dim) + TAG_ICON;
      posX++;
    }

    // Tag padding
    for (int i = 0; i < DASHBOARD_WIDTH - 9 - player.getTags().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }

    posX = baseX;
    posY++;

    // Line 2 side edges
    map[posX][posY] = playerColor + VERTICAL_LINE;
    map[baseX + DASHBOARD_WIDTH - 1][posY] = playerColor + VERTICAL_LINE + ANSIColor.RESET;
    posX++;

    map[posX][posY] = " ";
    posX++;

    // PowerUps
    posX = printDashboardPowerUps(map, player.getPowerUps(), playerColor, player.getPowerUpCount(), posX, posY);

    map[posX][posY] = playerColor + "|";
    posX++;
    map[posX][posY] = " ";
    posX++;

    // Damages
    for (PlayerColor damageColor : player.getDamages()) {
      map[posX][posY] = damageColor.getAnsiColor().toString(dim) + DAMAGE_ICON;
      posX++;
    }

    // Damage padding
    for (int i = 0; i < DASHBOARD_WIDTH - 9 - player.getDamages().size(); i++) {
      map[posX][posY] = " ";
      posX++;
    }

    posX = baseX;
    posY++;

    // Bottom padding (unused space) if dashboard height > 4
    while (posY < (num + 1) * DASHBOARD_HEIGHT - 1) {
      map[baseX][posY] = playerColor + VERTICAL_LINE;
      map[baseX + DASHBOARD_WIDTH - 1][posY] = playerColor + VERTICAL_LINE + ANSIColor.RESET;
      posY++;
    }

    // Bottom corners
    map[posX][posY] = playerColor + CornerType.BOTTOM_LEFT_CORNER;
    posX++;
    map[posX][posY] = playerColor + HORIZONTAL_LINE;
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[baseX + DASHBOARD_WIDTH - 1][posY] = playerColor + CornerType.BOTTOM_RIGHT_CORNER + ANSIColor.RESET;
    map[baseX + DASHBOARD_WIDTH - 2][posY] = playerColor + HORIZONTAL_LINE;
    map[baseX + DASHBOARD_WIDTH - 3][posY] = " ";

    // Kill score
    for (char c : Integer.toString(player.getKillScore()).toCharArray()) {
      map[posX][posY] = playerColor + c;
      posX++;
    }

    map[posX][posY] = " ";
    posX++;

    // Bottom edge line
    for (int i = 0; i < DASHBOARD_WIDTH - 14 - player.getKillScore() / 10; i++) {
      map[posX][posY] = playerColor + HORIZONTAL_LINE;
      posX++;
    }

    map[posX][posY] = " ";
    posX++;

    // Ammo
    map[posX][posY] = ANSIColor.RED.toString(dim) + player.getAmmo(AmmoColor.RED);
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = ANSIColor.YELLOW.toString(dim) + player.getAmmo(AmmoColor.YELLOW);
    posX++;
    map[posX][posY] = " ";
    posX++;
    map[posX][posY] = ANSIColor.BLUE.toString(dim) + player.getAmmo(AmmoColor.BLUE);
  }

  /**
   * Generates a print matrix.
   * @param board the board with the game map to draw
   * @return a print matrix for the game board
   */
  static String[][] buildMap(Board board) {
    String[][] map = new String[4 * SQUARE_WIDTH + DASHBOARD_WIDTH + 5][3 * SQUARE_HEIGHT + 2];
    for (Square square : board.getSquares()) {
      drawSquare(map, square);
      drawPlayers(map, square);
    }
    drawCoordinates(map);
    int num = 0;
    for (Player player : board.getPlayers()) {
      drawDashboard(map, num, board, player);
      num++;
    }
    return map;
  }

  /**
   * Prints a game board.
   * @param board the board with the game map to print
   */
  static void print(Board board) {
    String[][] map = buildMap(board);
    for (int y = 0; y < 3 * SQUARE_HEIGHT + 2; y++) {
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
