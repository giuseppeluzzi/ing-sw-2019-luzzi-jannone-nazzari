package it.polimi.se2019.adrenalina.utils;

import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * ANSI Console colors for TUI printing.
 */
public enum ANSIColor {
  BLACK(ansi().fgBlack().toString(), ansi().fgBrightBlack().toString(), "#393939"),
  RED(ansi().fgBrightRed().toString(), ansi().fgBrightRed().a(Attribute.INTENSITY_FAINT).toString(), "#e83a3a"),
  GREEN(ansi().fgBrightGreen().toString(), ansi().fgBrightGreen().a(Attribute.INTENSITY_FAINT).toString(), "#4CAF50"),
  YELLOW(ansi().fgBrightYellow().toString(), ansi().fgBrightYellow().a(Attribute.INTENSITY_FAINT).toString(), "#FF9800"),
  BLUE(ansi().fgBrightCyan().toString(), ansi().fgBrightCyan().a(Attribute.INTENSITY_FAINT).toString(), "#3F51B5"), // using cyan instead of blue for visibility
  MAGENTA(ansi().fgBrightMagenta().toString(), ansi().fgBrightMagenta().a(Attribute.INTENSITY_FAINT).toString(), "#9C27B0"),
  CYAN(ansi().fgBrightCyan().toString(), ansi().fgBrightCyan().a(Attribute.INTENSITY_FAINT).toString(), "#2d83bd"),
  WHITE(ansi().fg(Color.WHITE).toString(), ansi().fg(Color.WHITE).a(Attribute.INTENSITY_FAINT).toString(), "#606060"),
  RESET(ansi().reset().toString(), ansi().reset().toString(), "#ffffff");

  private final String normalColor;
  private final String dimColor;
  private final String hexColor;

  ANSIColor(String ansiColor, String dimColor, String hexColor) {
    normalColor = ansiColor;
    this.dimColor = dimColor;
    this.hexColor = hexColor;
  }

  public String getHexColor() {
    return hexColor;
  }

  @Override
  public String toString() {
    return normalColor;
  }

  public String toString(boolean dim) {
    if (dim) {
      return dimColor;
    } else {
      return normalColor;
    }
  }
}
