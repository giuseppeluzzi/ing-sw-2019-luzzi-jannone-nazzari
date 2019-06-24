package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class DialogSelectDirection extends Dialog {

  @FXML
  private HBox directionHbox;
  @FXML
  private RadioButton north;
  @FXML
  private RadioButton east;
  @FXML
  private RadioButton south;
  @FXML
  private RadioButton west;

  public DialogSelectDirection() {
    super("Scegli una direzione", false);
  }

  public void initialize() {
    north.setSelected(true);
  }

  public void next(ActionEvent actionEvent) {
    Direction selectedDirection = null;
    if (north.isSelected()) {
      selectedDirection = Direction.NORTH;
    } else if (east.isSelected()) {
      selectedDirection = Direction.EAST;
    } else if (south.isSelected()) {
      selectedDirection = Direction.SOUTH;
    } else if (west.isSelected()) {
      selectedDirection = Direction.WEST;
    }

    try {
      PlayerColor playerColor = AppGUI.getClient().getPlayerColor();
      ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(new SelectDirectionEvent(playerColor, selectedDirection));
    } catch (RemoteException e) {
      Log.exception(e);
    }

    close();
  }
}
