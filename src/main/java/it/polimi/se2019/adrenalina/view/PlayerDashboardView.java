package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.controller.events.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class PlayerDashboardView extends Observable implements Observer {
  private Player player;

  public PlayerDashboardView(Player player) {
    this.player = player;
  }

  public void reset(Player player) {

  }

  public void switchToFinalFrenzy() {

  }

  public void update(PlayerUpdateEvent event) {

  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
