package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DialogReloadWeaponSelection extends Dialog {

  private List<Weapon> weapons;

  @FXML
  private HBox weaponsHBox;

  @FXML
  private Button buttonNext;

  public DialogReloadWeaponSelection() {
    super("Ricarica arma arma", true);
  }

  public void setWeapons(List<Weapon> weapons) {
    this.weapons = new ArrayList<>(weapons);
  }

  @Override
  public void build() {
    ToggleGroup weaponToggleGroup = new ToggleGroup();
    int index = 0;
    for (Weapon weapon : weapons) {
      VBox vBox = new VBox();
      vBox.setAlignment(Pos.CENTER);
      vBox.setSpacing(25);
      vBox.setPrefHeight(200);
      vBox.setPrefWidth(100);
      ImageView weaponImg = new ImageView("gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
      weaponImg.setFitHeight(200);
      weaponImg.setPreserveRatio(true);
      RadioButton radioButton = new RadioButton();
      radioButton.setId(Integer.toString(index));
      radioButton.setToggleGroup(weaponToggleGroup);
      if (index == 0) {
        radioButton.setSelected(true);
      }
      vBox.getChildren().add(weaponImg);
      vBox.getChildren().add(radioButton);
      weaponsHBox.getChildren().add(vBox);
      index++;
    }

    buttonNext.setOnAction(event -> {
      Weapon weapon = weapons.get(Integer.parseInt(((Styleable) weaponToggleGroup.getSelectedToggle()).getId()));
      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerReloadEvent(AppGUI.getClient().getPlayerColor(), weapon.getName()));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
  }
}
