package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersView;
import javafx.application.Platform;

public class GUICharactersView extends CharactersView {

  private static final long serialVersionUID = -2544028250019433844L;

  public GUICharactersView(BoardViewInterface boardView) {
    super((BoardView) boardView);
  }

  @Override
  public void showDeath(PlayerColor playerColor) {

  }

  @Override
  public void update(PlayerPositionUpdate event) {
    Platform.runLater(() -> AppGUI.getBoardFXController().setPlayerPosition(event.getPosX(), event.getPosY(), event.getPlayerColor()));
  }
}
