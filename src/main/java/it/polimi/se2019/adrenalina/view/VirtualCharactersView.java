package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.ShowDeathInvocation;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;

import java.rmi.RemoteException;

/**
 * Virtual characters view, relays invocations over the network to the actual characters view.
 */
public class VirtualCharactersView extends Observable implements CharactersViewInterface {

  private static final long serialVersionUID = -6715889122608916050L;
  private final transient VirtualClientSocket clientSocket;

  public VirtualCharactersView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void showDeath(PlayerColor playerColor) {
    clientSocket.sendEvent(new ShowDeathInvocation(playerColor));
  }

  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        clientSocket.sendEvent(event);
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
