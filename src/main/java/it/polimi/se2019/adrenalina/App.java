package it.polimi.se2019.adrenalina;

import javafx.application.Application;
import org.fusesource.jansi.AnsiConsole;

/**
 * The main class of the application.
 */
public class App {

  public static void main(String... args) {
    if (!runningFromIntelliJ()) {
      AnsiConsole.systemInstall();
    }

    if (args.length > 0) {
      switch (args[0]) {
        case "--server":
          new AppServer();
          break;
        case "--tui":
          new AppClient(args);
          break;
        default:
          startGUI();
          break;
      }
    } else {
      startGUI();
    }

    if (!runningFromIntelliJ()) {
      AnsiConsole.systemUninstall();
    }
  }

  private static void startGUI() {
    new Thread(() -> Application.launch(AppGUI.class)).start();
  }

  public static boolean runningFromIntelliJ() {
    String classPath = System.getProperty("java.class.path");
    return classPath.contains("idea_rt.jar");
  }
}
