package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.PlayerChatEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;

public interface ServerViewInterface {
  public void update(PlayerConnectEvent event);

  public void update(PlayerChatEvent event);
}
