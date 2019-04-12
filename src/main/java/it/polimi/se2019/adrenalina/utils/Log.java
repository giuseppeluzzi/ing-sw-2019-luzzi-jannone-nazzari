package it.polimi.se2019.adrenalina.utils;

import java.util.Locale;
import java.util.logging.Logger;

public class Log {
  private static Logger logger = Logger.getLogger("Adrenalina");

  public static void setName(String name) {
    logger = Logger.getLogger(name);
  }

  public static void info(String tag, String message) {
    logger.info(tag.toUpperCase(Locale.ENGLISH) + ": " + message);
  }

  public static void warn(String tag, String message) {
    logger.warning(tag.toUpperCase(Locale.ENGLISH) + ": " + message);
  }

  public static void severe(String tag, String message) {
    logger.severe(tag.toUpperCase(Locale.ENGLISH) + ": " + message);
  }

  public static void info(String message) {
    logger.info(message);
  }

  public static void warn(String message) {
    logger.warning(message);
  }

  public static void severe(String message) {
    logger.severe(message);
  }

}
