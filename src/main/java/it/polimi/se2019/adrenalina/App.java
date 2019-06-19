package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import org.fusesource.jansi.AnsiConsole;

/**
 * The main class of the application.
 */
public class App {

  public static void main(String... args) {
    if (!runningFromIntelliJ()) {
      AnsiConsole.systemInstall();
    }

    // Verify if the configuration exists
    Configuration.getInstance();

    if (args.length > 0 && "--server".equalsIgnoreCase(args[0])) {
      new AppServer();
    } else {
      new AppClient(args);
    }

    if (!runningFromIntelliJ()) {
      AnsiConsole.systemUninstall();
    }
  }

  private static boolean runningFromIntelliJ() {
    String classPath = System.getProperty("java.class.path");
    return classPath.contains("idea_rt.jar");
  }
}
