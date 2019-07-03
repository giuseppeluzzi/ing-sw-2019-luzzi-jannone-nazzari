package it.polimi.se2019.adrenalina.utils;

/**
 * Internal constants.
 */
public class Constants {
  public static final String SERVER_CONFIG_FILE = "server_config.json";
  public static final String CLIENT_CONFIG_FILE = "client_config.json";

  public static final int PING_INTERVAL = 500;
  public static final int MAX_NAME_LENGTH = 12;

  public static final int TUI_DASHBOARD_WIDTH = 22;
  public static final int TUI_DASHBOARD_HEIGHT = 4;
  public static final int TUI_SQUARE_WIDTH = 13;
  public static final int TUI_SQUARE_HEIGHT = 6;
  public static final int TUI_DOOR_WIDTH = 5;
  public static final int TUI_DOOR_HEIGHT = 2;
  public static final int TUI_WEAPON_NAME_WIDTH = 26;
  public static final String TUI_EMPTY_DAMAGES = "(nessuno)";
  public static final String TUI_HORIZONTAL_LINE = "━";
  public static final String TUI_VERTICAL_LINE = "┃";
  public static final String TUI_PLAYER_ICON = "⚑";
  public static final String TUI_SPAWNPOINT_ICON = "⊡";
  public static final String TUI_TAG_ICON = "✦";
  public static final String TUI_DAMAGE_ICON = "✚";
  public static final String TUI_OVERKILL_ICON = "✠";
  public static final String TUI_SKULL_ICON = "△";

  private Constants() {
    throw new IllegalStateException("Utility class");
  }
}