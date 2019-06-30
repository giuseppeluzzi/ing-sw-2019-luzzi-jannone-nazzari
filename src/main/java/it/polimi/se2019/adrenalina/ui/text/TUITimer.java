package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Text User Interface timer with a message printed each second.
 */
public class TUITimer extends Timer {

  private static final long serialVersionUID = 3250410511772801896L;
  private final transient ClientInterface client;

  public TUITimer(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void tick() {
    Log.print(
        "\rLa partita inizierà tra " + ansi().bold() + getRemainingSeconds() + ansi().boldOff()
            + " second" + (getRemainingSeconds() != 1 ? "i" : "o"));
    Log.println("");
  }
}
