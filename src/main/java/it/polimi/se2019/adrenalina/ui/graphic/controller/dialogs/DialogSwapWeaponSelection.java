package it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSwapWeaponEvent;
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

public class DialogSwapWeaponSelection extends Dialog {

  private List<Weapon> pickableWeapons;
  private List<Weapon> swappableWeapons;

  @FXML
  private HBox pickableHBox;
  @FXML
  private HBox swappableHBox;

  @FXML
  private Button buttonNext;

  public DialogSwapWeaponSelection() {
    super("Scambio arma", true);
  }

  public void setPickableWeapons(List<Weapon> pickableWeapons) {
    this.pickableWeapons = new ArrayList<>(pickableWeapons);
  }

  public void setSwappableWeapons(List<Weapon> swappableWeapons) {
    this.swappableWeapons = new ArrayList<>(swappableWeapons);
  }

  private void addWeaponsToHbox(HBox weaponsHBox, List<Weapon> weapons, ToggleGroup group) {
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
      radioButton.setToggleGroup(group);
      if (index == 0) {
        radioButton.setSelected(true);
      }
      vBox.getChildren().add(weaponImg);
      vBox.getChildren().add(radioButton);
      weaponsHBox.getChildren().add(vBox);
      index++;
    }
  }

  @Override
  public void build() {
    ToggleGroup pickWeaponToggleGroup = new ToggleGroup();
    addWeaponsToHbox(pickableHBox, pickableWeapons, pickWeaponToggleGroup);
    ToggleGroup swapWeaponToggleGroup = new ToggleGroup();
    addWeaponsToHbox(swappableHBox, swappableWeapons, swapWeaponToggleGroup);

    buttonNext.setOnAction(event -> {
      AppGUI.getBoardFXController().stopTurnTimer();
      Weapon pickedWeapon = pickableWeapons.get(Integer.parseInt(((Styleable) pickWeaponToggleGroup.getSelectedToggle()).getId()));
      Weapon swappedWeapon = swappableWeapons.get(Integer.parseInt(((Styleable) swapWeaponToggleGroup.getSelectedToggle()).getId()));
      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerSwapWeaponEvent(AppGUI.getClient().getPlayerColor(), swappedWeapon.getName(), pickedWeapon.getName()));
      } catch (RemoteException e) {
        Log.exception(e);
      }
      close();
    });
  }
}
