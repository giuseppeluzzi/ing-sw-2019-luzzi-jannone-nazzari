package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.utils.Timer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GUITimer extends Timer {

  private static final long serialVersionUID = 8917769173064302339L;

  private final transient IntegerProperty seconds = new SimpleIntegerProperty(0);

  @Override
  public void tick() {
    seconds.setValue(getRemainingSeconds());
  }

  public IntegerProperty getSeconds() {
    return seconds;
  }
}
