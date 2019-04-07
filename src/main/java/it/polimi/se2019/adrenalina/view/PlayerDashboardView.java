package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class PlayerDashboardView extends Observable implements Observer {
  private Player player;

  public PlayerDashboardView(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public void reset() {
    // TODO: reset
  }

  public void switchToFinalFrenzy() {
    // TODO: change dashboard to final frenzy mode
  }

  public void update(PlayerUpdateEvent event) {
    // TODO:
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
