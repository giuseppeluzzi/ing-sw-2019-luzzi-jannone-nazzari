package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.ServerConfig;
import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.IOUtils;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Main class of the application running as server.
 */
public class AppServer {
  public AppServer() {
    File extConfig = new File(Constants.SERVER_CONFIG_FILE);
    if (! extConfig.isFile()) {
      byte[] intConfig = null;
      try {
        intConfig = IOUtils.readResourceFile(Constants.SERVER_CONFIG_FILE).getBytes(StandardCharsets.UTF_8);
      } catch (IOException e) {
        Log.severe("Internal configuration file not found!");
        System.exit(1);
      }
      Path extFile = Paths.get(Constants.SERVER_CONFIG_FILE);
      try {
        Files.write(extFile, intConfig);
      } catch (IOException e) {
        Log.severe("Unable to write configuration file: " + e);
        System.exit(1);
      }
    }

    Log.setName("ServerRMI");
    Log.info("Server started!");

    try {
      Server server = new Server();

      Registry registry = LocateRegistry.createRegistry(ServerConfig.getInstance().getRmiPort());
      registry.rebind("MyServer", server);

      ServerSocket serverSocket = new ServerSocket(server);
      serverSocket.run();
    } catch (RemoteException e) {
      Log.severe("RMI connection error: " + e.getMessage());
    }
  }
}
