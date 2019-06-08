package it.polimi.se2019.adrenalina.ui.text;

import static org.fusesource.jansi.Ansi.ansi;

import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.rmi.RemoteException;

public class TUITimer extends Timer {

  private static final long serialVersionUID = 3250410511772801896L;
  private final transient ClientInterface client;

  public TUITimer(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void tick() {
    try {
      client.showGameMessage(
          "La partita inizierà tra " + ansi().bold() + getRemainingSeconds() + ansi().boldOff()
              + " second" + (getRemainingSeconds() != 1 ? "i" : "o"));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
