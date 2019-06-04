package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.network.ClientRMI;
import it.polimi.se2019.adrenalina.network.ClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import org.fusesource.jansi.AnsiConsole;

public class App {
  public static void main(String... args) {
    AnsiConsole.systemInstall();

    // Verify if the configuration exists
    Configuration.getInstance();

    if (args.length > 0 && "--server".equalsIgnoreCase(args[0])) {
      new AppServer();
    } else {
      new AppClient(args);
    }

    AnsiConsole.systemUninstall();
  }
}
