package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.game.SpawnPointTrackSelection;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

import java.rmi.RemoteException;

public class DialogSpawnPointTrackSelection extends Dialog {

  @FXML
  private Button buttonNext;
  private ToggleGroup trackGroup;

  public DialogSpawnPointTrackSelection() {
    super("Seleziona un tracciato", false);
  }

  @Override
  public void build() {
    buttonNext.setOnAction(event -> {
      AmmoColor chosenAmmo = AmmoColor.valueOf(((Styleable) trackGroup.getSelectedToggle()).getId());
      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new SpawnPointDamageEvent(AppGUI.getClient().getPlayerColor(), chosenAmmo));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
   }
  }

