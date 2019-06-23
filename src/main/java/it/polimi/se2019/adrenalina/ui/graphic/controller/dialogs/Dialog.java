package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.utils.Log;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Dialog {

  private final boolean closeable;
  private final String title;

  protected Dialog(String title, boolean closeable) {
    this.title = title;
    this.closeable = closeable;
  }

  public void show() {
    FXMLLoader loaderDialog = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/dialogs/" + getClass().getSimpleName() + ".fxml"));

    try {
      Scene scene = new Scene(loaderDialog.load());
      scene.getStylesheets().add(AppGUI.getCSS());

      Stage stage = new Stage();
      stage.initStyle(StageStyle.UTILITY);
      stage.setResizable(false);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(scene);
      stage.getIcons().clear();
      stage.setTitle(title);

      if (!closeable) {
        stage.setOnCloseRequest(event -> {
          event.consume();
          new Alert(Alert.AlertType.ERROR, "Devi prima fare una selezione").showAndWait();
        });
      }

      stage.showAndWait();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

}
