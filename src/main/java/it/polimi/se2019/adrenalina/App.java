package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.IOUtils;
import it.polimi.se2019.adrenalina.utils.Log;
import javafx.application.Application;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The main class of the application.
 */
public class App {

  public static void main(String... args) {
    if (!runningFromIntelliJ()) {
      AnsiConsole.systemInstall();
    }

    File extConfig = new File(Constants.CONFIG_FILE);
    if (! extConfig.isFile()) {
      byte[] intConfig = null;
      try {
        intConfig = IOUtils.readResourceFile(Constants.CONFIG_FILE).getBytes(StandardCharsets.UTF_8);
      } catch (IOException e) {
        Log.severe("Configuration file not found!");
        System.exit(1);
      }
      Path extFile = Paths.get(Constants.CONFIG_FILE);
      try {
        Files.write(extFile, intConfig);
      } catch (IOException e) {
        Log.severe("Unable to write configuration file: " + e);
        System.exit(1);
      }
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
