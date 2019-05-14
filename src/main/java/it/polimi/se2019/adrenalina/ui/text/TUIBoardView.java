package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.action.TargetType;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.util.List;

public class TUIBoardView extends BoardView {

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    Log.print(severity + ": " + title);
    Log.print(message);
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    switch (type) {
      case ATTACK_TARGET:
      case ATTACK_SQUARE:
        Log.print("Seleziona un bersaglio");
        break;
      case ATTACK_ROOM:
        Log.print("Seleziona una stanza");
        break;
      case MOVE_SQUARE:
        Log.print("Seleziona una destinazione");
        break;
    }
  }

  @Override
  public void showDirectionSelect() {

  }
}
