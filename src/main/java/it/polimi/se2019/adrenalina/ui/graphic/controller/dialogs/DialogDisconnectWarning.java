package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DialogDisconnectWarning extends Dialog {

  @FXML
  private Button buttonNext;

  public DialogDisconnectWarning() {
    super("Disconnessione", true);
  }

  @Override
  public void build() {
    buttonNext.setOnAction(event -> System.exit(0));
    stage.setOnCloseRequest(event -> System.exit(0));
    }
  }

