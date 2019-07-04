package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

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
      AmmoColor chosenAmmo = AmmoColor
          .valueOf(((Styleable) trackGroup.getSelectedToggle()).getId());

      AppGUI.getBoardFXController().stopTurnTimer();

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

