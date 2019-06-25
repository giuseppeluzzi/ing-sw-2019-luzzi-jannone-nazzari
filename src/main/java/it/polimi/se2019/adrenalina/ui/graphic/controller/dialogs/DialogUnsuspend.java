package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerUnsuspendEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

import java.rmi.RemoteException;

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
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerUnsuspendEvent(AppGUI.getClient().getPlayerColor()));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
   }
  }

