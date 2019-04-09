package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;

public interface PlayerDashboardsViewInterface {

  public void addPlayer(Player player);

  public List<Player> getPlayers();

  public void reset(Player player);

  public void switchToFinalFrenzy(Player player);

  public void update(PlayerUpdateEvent event);

  public void update(Event event);
}
