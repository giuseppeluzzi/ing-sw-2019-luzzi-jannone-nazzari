package it.polimi.se2019.adrenalina.network;

import static java.lang.Thread.sleep;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.ui.text.TUIBoardView;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersView;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsView;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends Client {

  private static final long serialVersionUID = 5097938777989686167L;
  private volatile boolean running = true;

  private Player player;

  private BoardViewInterface boardView;
  private CharactersViewInterface charactersView;
  private PlayerDashboardsViewInterface playerDashboardsView;

  public ClientRMI(String name, boolean domination) {
    super(name, domination);

    try {
      Registry registry = LocateRegistry.getRegistry(Configuration.getInstance().getServerIP(),
          Configuration.getInstance().getRmiPort());
      ServerInterface server = (ServerInterface) registry.lookup("MyServer");

      boardView = new TUIBoardView(this);
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
          Log.severe("ClientRMI", "Pooling interrupted! Thread stopped.");
          Thread.currentThread().interrupt();
        }
      }
    });

    pooling.start();
  }

  @Override
  public void showMessage(String text) {
    Log.info(text);
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
    return obj != null && (obj == this || getClass() == obj.getClass() && ((Client) obj).getName()
        .equals(getName()));
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
