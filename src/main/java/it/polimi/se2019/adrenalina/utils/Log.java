package it.polimi.se2019.adrenalina.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Logging and printing manager.
 */
public class Log {
  private static final String LOG_FORMAT = "{0}: {1}";
  private static Logger logger = Logger.getLogger("Adrenalina");

  private Log() {
    // private constructor
  }

  public static void setName(String name) {
    try {
      InputStream input = Log.class.getResourceAsStream("/logging.properties");
      LogManager.getLogManager().readConfiguration(input);
    } catch (IOException ignored) {
      // do nothing
    }
    logger = Logger.getLogger(name);
  }

  public static void info(String tag, String message) {
    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, LOG_FORMAT , new Object[] {tag.toLowerCase(Locale.ENGLISH), message});
    }
  }

  public static void warn(String tag, String message) {
    if (logger.isLoggable(Level.WARNING)) {
      logger.log(Level.WARNING, LOG_FORMAT, new Object[] {tag.toLowerCase(Locale.ENGLISH), message});
    }
  }

  public static void severe(String tag, String message) {
    if (logger.isLoggable(Level.SEVERE)) {
      logger.log(Level.SEVERE, LOG_FORMAT, new Object[] {tag.toLowerCase(Locale.ENGLISH), message});
    }
  }

  public static void debug(String tag, String message) {
    if (logger.isLoggable(Level.WARNING)) {
      logger.log(Level.WARNING, LOG_FORMAT, new Object[] {tag.toLowerCase(Locale.ENGLISH), message});
    }
  }

  public static void critical(String message) {
    logger.log(Level.SEVERE, message);
    System.exit(0);
  }

  public static void debug(String message) {
    logger.warning(message);
  }

  public static void println(String message) {
    System.out.println(message);
    /* Sonar will complaint about this, but System.out is the correct thing to use
     * when directly printing text for the user. This function has been wrapped here
     * in order to limit the amount of Sonar warnings.
     */
  }

  public static void print(String message) {
    System.out.print(message);
    /* Sonar will complaint about this, but System.out is the correct thing to use
     * when directly printing text for the user. This function has been wrapped here
     * in order to limit the amount of Sonar warnings.
     */
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

  public static void exception(RemoteException exception) {
    severe("RMI", exception.getMessage());
  }

  public static void exception(IOException exception) {
    severe("IO", exception.getMessage());
  }

  public static void exception(InvocationTargetException exception) {
    exception.getCause().printStackTrace();
  }
}
