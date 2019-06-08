package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Timer;

public class GUITimer extends Timer {

  private static final long serialVersionUID = 8917769173064302339L;
  private final transient ClientInterface client;

  public GUITimer(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void tick() {
    // TODO
  }
}
