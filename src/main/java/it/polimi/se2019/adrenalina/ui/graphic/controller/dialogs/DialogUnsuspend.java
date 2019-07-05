package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerUnsuspendEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DialogUnsuspend extends Dialog {

  @FXML
  private Button buttonNext;

  public DialogUnsuspend() {
    super("Giocatore sospeso", false);
  }

  @Override
  public void build() {
    buttonNext.setOnAction(event -> {
      try {
        AppGUI.getClient().getBoardView().sendEvent(
            new PlayerUnsuspendEvent(AppGUI.getClient().getPlayerColor()));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
  }
}

