package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersView;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {
  private static final long serialVersionUID = -2824559728518448567L;

  private final String name;
  private final boolean domination;
  private volatile boolean running = true;

  private transient Player player;

  private BoardViewInterface boardView;
  private CharactersViewInterface charactersView;
  private PlayerDashboardsViewInterface playerDashboardsView;

  public Client(String name, boolean domination) throws RemoteException {
    this.name = name;
    this.domination = domination;

    try {
      Registry registry = LocateRegistry.getRegistry(Configuration.getInstance().getServerIP(),
          Configuration.getInstance().getRmiPort());
      ServerInterface server = (ServerInterface) registry.lookup("MyServer");

      boardView = new BoardView();
      charactersView = new CharactersView();
      playerDashboardsView = new PlayerDashboardsView();

      UnicastRemoteObject.exportObject(boardView, 0);
      UnicastRemoteObject.exportObject(charactersView, 0);
      UnicastRemoteObject.exportObject(playerDashboardsView, 0);

      server.addClient(this);
    } catch (NotBoundException e) {
      Log.severe("RMI", "Object not bound");
      Log.critical("Network error");
      return;
    } catch (RemoteException e) {
      Log.exception(e);
      Log.critical("Network error");
      return;
    }

    final Thread pooling = new Thread(() -> {
      while (running) {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          Log.severe("Client", "Pooling interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
        }
      }
    });

    pooling.start();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void showMessage(String text) {
    Log.info(text);
  }

  @Override
  public void ping() {
    // useless
  }

  @Override
  public void disconnect() {
    running = false;
  }

  @Override
  public BoardViewInterface getBoardView() {
    return boardView;
  }

  @Override
  public CharactersViewInterface getCharactersView() {
    return charactersView;
  }

  @Override
  public PlayerDashboardsViewInterface getPlayerDashboardsView() {
    return playerDashboardsView;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && (obj == this || getClass() == obj.getClass() && ((Client) obj).name
        .equals(name));
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
