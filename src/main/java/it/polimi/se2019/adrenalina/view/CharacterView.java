package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.controller.events.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.events.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class CharacterView extends Observable implements Observer {
  private Player player;

  public CharacterView(Player player) {
    this.player = player;
  }

  public void setSelected(boolean selected) {

  }

  public void update(PlayerDeathEvent event) {

  }

  public void update(PlayerSpawnEvent event) {

  }

  public void update(PlayerMoveEvent event) {

  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
