package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.List;

public class GUIBoardView extends BoardView {

  private static final long serialVersionUID = -5469323461908447838L;

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
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
  
  public void showBuyableWeapons(List<Weapon> weapons) {
    // TODOo
  }
}
