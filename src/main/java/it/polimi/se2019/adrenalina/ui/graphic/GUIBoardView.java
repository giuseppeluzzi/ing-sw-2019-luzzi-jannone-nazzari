package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.util.List;

public class GUIBoardView extends BoardView {

  private static final long serialVersionUID = -5469323461908447838L;

  public GUIBoardView(ClientInterface client) {
    super(client, new GUITimer(client));
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
}
