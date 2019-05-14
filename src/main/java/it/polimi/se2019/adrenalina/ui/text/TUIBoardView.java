package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.TargetType;
import it.polimi.se2019.adrenalina.controller.event.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class TUIBoardView extends BoardView {

  private final Scanner scanner = new Scanner(System.in, "utf-8");

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    Log.print(severity + ": " + title);
    Log.print(message);
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    switch (type) {
      case ATTACK_TARGET:
        Log.print("Seleziona un bersaglio");
        break;
      case ATTACK_SQUARE:
        Log.print("Seleziona un quadrato");
        break;
      case ATTACK_ROOM:
        EnumSet<SquareColor> squareColors = EnumSet.noneOf(SquareColor.class);

        for (Target target : targets) {
          squareColors.add(target.getSquare().getColor());
        }

        int colorIndex = 1;
        int choosenColor;
        do {
          Log.print("Seleziona una stanza");
          for (SquareColor color : squareColors) {
            Log.print("\t" + colorIndex + ") " + color);
            colorIndex++;
          }

          choosenColor = scanner.nextLine().charAt(0);
        } while (choosenColor == 0 || choosenColor >= colorIndex);

        //super.notifyObservers(new SelectPlayerEvent(super.getClient()));
        break;
      case MOVE_SQUARE:
        Log.print("Seleziona una destinazione");
        break;
    }
  }

  @Override
  public void showDirectionSelect() {
    // TODO
  }
}
