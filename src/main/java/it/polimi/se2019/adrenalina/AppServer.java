package it.polimi.se2019.adrenalina;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.event.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.model.AmmoColor;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PlayerColor;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AppServer {

  private static final int RMI_PORT = 1099;

  public static void main(String... args) {
    Log.setName("ServerRMI");

    Player p1  = new Player("ciao", PlayerColor.YELLOW);
    Player p2  = new Player("prova", PlayerColor.BLUE);
    Effect ef = new Effect("prova", new Weapon(1, 2, 3, AmmoColor.RED, "arma"),1, 2, 3 );

    PlayerAttackEvent ev = new PlayerAttackEvent(p1, p2, ef);

    Log.info(ev.serialize());

    Gson gson = new Gson();
    PlayerAttackEvent ciao = gson.fromJson(ev.serialize(), PlayerAttackEvent.class);
    Log.info(ciao.getPlayer().getColor().toString());

    try {
      Server server = new Server();

      LocateRegistry.createRegistry(RMI_PORT);
      Naming.rebind("//localhost/MyServer", server);

      ServerSocket serverSocket = new ServerSocket(server);
      serverSocket.run();
    } catch (MalformedURLException e) {
      Log.severe("RMI incorrect URL!");
    } catch (RemoteException e) {
      Log.severe("RMI connection error: " + e.getMessage());
    }
  }
}
