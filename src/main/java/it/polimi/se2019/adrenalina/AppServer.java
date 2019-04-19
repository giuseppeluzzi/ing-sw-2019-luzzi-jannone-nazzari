package it.polimi.se2019.adrenalina;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.SelectAction;
import it.polimi.se2019.adrenalina.controller.ShootAction;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Server;
import it.polimi.se2019.adrenalina.network.ServerSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AppServer {
  public static void main(String... args) {
    Log.setName("ServerRMI");

    Weapon weapon = new Weapon(0, 1, 0, AmmoColor.BLUE, "Distruttore");
    Effect base = new Effect("Effetto base", weapon, 0, 0, 0);
    base.addAction(new SelectAction(0, 1, 0, 0, 0, true));
    base.addAction(new ShootAction(1, 2, 1));
    weapon.addEffect(base);
    Effect bis = new Effect("Secondo aggancio", weapon, 1, 0, 0);
    bis.addAction(new SelectAction(0, 2, 0, 0, 1, true));
    bis.addAction(new ShootAction(2, 0, 1));
    base.addSubEffect(bis);

    Log.info(weapon.serialize());


    Log.info(""+Weapon.deserialize(weapon.serialize()).getEffects().get(0).getSubEffects().size());

    // Verify if the configuration exists
    Configuration.getInstance();

    try {
      Server server = new Server();

      LocateRegistry.createRegistry(Configuration.getInstance().getRmiPort());
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
