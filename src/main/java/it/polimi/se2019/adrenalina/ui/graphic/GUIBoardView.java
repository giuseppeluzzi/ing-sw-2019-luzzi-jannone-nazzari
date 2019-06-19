package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.List;
import javafx.application.Platform;

public class GUIBoardView extends BoardView {

  private static final long serialVersionUID = -5469323461908447838L;

  public GUIBoardView(Client client) {
    super(client, new GUITimer(client));
  }

  @Override
  public void endLoading(boolean masterPlayer) {
    Log.debug("AHUASDNASD");
    new Thread(() -> {
      try {
        Thread.sleep(Constants.PING_INTERVAL * 2);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      Platform.runLater(() -> {
        AppGUI.getLobbyFXController().endLoading(masterPlayer);
      });
    }).start();
  }

  @Override
  public void showBoard() {
    // TODO
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    // TODO
  }

  @Override
  public void showDirectionSelect() {
    // TODO
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    // TODO
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) {
    // TODO
  }

  @Override
  public void showSpawnPointTrackSelection() {
    // TODO
  }

  @Override
  public void showFinalRanks() {
    // TODO
  }
}
