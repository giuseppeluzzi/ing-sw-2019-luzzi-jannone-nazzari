package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.App;
import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.application.Platform;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogTurnActionSelection extends Dialog {

  private ToggleGroup actionSelectionGroup;
  private List<TurnAction> actions;

  @FXML
  private VBox optionsVBox;
  @FXML
  private Button buttonNext;

  public DialogTurnActionSelection() {
    super("Seleziona un'azione", false);
  }

  public void setActions(List<TurnAction> actions) {
    this.actions = new ArrayList<>(actions);
  }

  @Override
  public void build() {
    actionSelectionGroup = new ToggleGroup();
    int index = 0;
    for (TurnAction action : actions) {
      RadioButton button = new RadioButton(action.getDescription());
      button.setId(Integer.toString(index));
      button.setToggleGroup(actionSelectionGroup);
      if (index == 0) {
        button.setSelected(true);
      }
      optionsVBox.getChildren().add(button);
      index++;
    }

    buttonNext.setOnAction(event -> {
      TurnAction chosenAction = actions.get(Integer.parseInt(((Styleable) actionSelectionGroup.getSelectedToggle()).getId()));
      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerActionSelectionEvent(AppGUI.getClient().getPlayerColor(), chosenAction));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
   }
  }

