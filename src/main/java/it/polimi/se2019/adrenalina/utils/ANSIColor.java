package it.polimi.se2019.adrenalina.utils;

import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

public enum ANSIColor {
  BLACK(ansi().fgBlack().toString(), ansi().fgBrightBlack().toString()),
  RED(ansi().fgBrightRed().toString(), ansi().fgBrightRed().a(Attribute.INTENSITY_FAINT).toString()),
  GREEN(ansi().fgBrightGreen().toString(), ansi().fgBrightGreen().a(Attribute.INTENSITY_FAINT).toString()),
  YELLOW(ansi().fgBrightYellow().toString(), ansi().fgBrightYellow().a(Attribute.INTENSITY_FAINT).toString()),
  BLUE(ansi().fgBrightCyan().toString(), ansi().fgBrightCyan().a(Attribute.INTENSITY_FAINT).toString()), // using cyan instead of blue for visibility
  MAGENTA(ansi().fgBrightMagenta().toString(), ansi().fgBrightMagenta().a(Attribute.INTENSITY_FAINT).toString()),
  CYAN(ansi().fgBrightCyan().toString(), ansi().fgBrightCyan().a(Attribute.INTENSITY_FAINT).toString()),
  WHITE(ansi().fg(Color.WHITE).toString(), ansi().fg(Color.WHITE).a(Attribute.INTENSITY_FAINT).toString()),
  RESET(ansi().reset().toString(), ansi().reset().toString());

  private final String normalColor;
  private final String dimColor;

  ANSIColor(String ansiColor, String dimColor) {
    normalColor = ansiColor;
    this.dimColor = dimColor;
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
