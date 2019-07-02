package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Timer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;

public class GUITimer extends Timer {

  private static final long serialVersionUID = 8917769173064302339L;
  private final transient ClientInterface client;

  private final transient IntegerProperty seconds = new SimpleIntegerProperty(0);

  public GUITimer(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void tick() {
    seconds.setValue(getRemainingSeconds());
  }

  public IntegerProperty getSeconds() {
    return seconds;
  }
}
