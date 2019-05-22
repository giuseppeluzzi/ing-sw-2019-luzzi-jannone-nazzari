package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.controller.event.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.controller.event.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.controller.event.SelectSquareEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class TUIBoardView extends BoardView {

  private static final long serialVersionUID = 7696019255617335385L;
  private final transient ClientInterface client;
  private final transient Scanner scanner = new Scanner(System.in, "utf-8");

  public TUIBoardView(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    Log.print(severity + ": " + title);
    Log.print(message);
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    Target chosenTarget;

    try {
      switch (type) {
        case ATTACK_TARGET:
          chosenTarget = selectAttackTarget(targets);
          if (chosenTarget.isPlayer()) {
            notifyObservers(new SelectPlayerEvent(client.getPlayerColor(),
                chosenTarget.getPlayer().getColor()));
          } else {
            notifyObservers(new SelectSquareEvent(client.getPlayerColor(),
                chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
        case MOVE_SQUARE:
        case ATTACK_SQUARE:
          chosenTarget = selectSquare(targets);
          notifyObservers(new SelectSquareEvent(client.getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          break;
        case ATTACK_ROOM:
          chosenTarget = selectRoom(targets);
          notifyObservers(new SelectSquareEvent(client.getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          break;
      }
    } catch (RemoteException e) {
      Log.exception(e);
    } catch (InvalidSquareException ignored) {
      //
    }
  }

  private Target selectRoom(List<Target> targets) {
    int targetIndex = 0;
    int chosenTarget = 0;

    EnumSet<SquareColor> squareColors = EnumSet.noneOf(SquareColor.class);

    for (Target target : targets) {
      squareColors.add(target.getSquare().getColor());
    }

    do {
      Log.print("Seleziona una stanza");
      for (SquareColor color : squareColors) {
        Log.print("\t" + targetIndex + ") " + color);
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    for (Target target : targets) {
      if (target.getSquare().getColor() == squareColors.toArray()[targetIndex]) {
        return target;
      }
    }

    throw new IllegalStateException();
  }

  private Target selectSquare(List<Target> targets) {
    int targetIndex = 0;
    int chosenTarget = 0;

    Log.print("Seleziona un quadrato");
    do {
      for (Target target : targets) {
        Log.print(
            String.format("\t%d) X: %d - Y: %d - Colore: %s",
                targetIndex,
                target.getSquare().getPosX(),
                target.getSquare().getPosY(),
                target.getSquare().getColor()));
        targetIndex++;
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    return targets.get(targetIndex - 1);
  }

  private Target selectAttackTarget(List<Target> targets) {
    int targetIndex;
    int chosenTarget = 0;

    Log.print("Seleziona un bersaglio");
    do {
      targetIndex = 0;
      for (Target target : targets) {
        try {
          if (target.isPlayer()) {
            Log.print(String.format("\t%d) %s", targetIndex, target.getPlayer().getName()));
          } else {
            Log.print(
                String
                    .format("\t%d) Spawnpoint %s", targetIndex, target.getSquare().getColor()));
          }
          targetIndex++;
        } catch (InvalidSquareException ignored) {
          //
        }
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    return targets.get(targetIndex - 1);
  }

  @Override
  public void showDirectionSelect() {
    int targetIndex = 0;
    int chosenTarget = 0;

    do {
      Log.print("Seleziona una direzione");
      for (Direction direction : Direction.values()) {
        Log.print("\t" + targetIndex + ") " + direction);
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(new SelectDirectionEvent(client.getPlayerColor(),
          Direction.values()[chosenTarget - 1]));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    Square square = (Square) selectSquare(targets);
    try {
      notifyObservers(new SelectSquareEvent(client.getPlayerColor(), square.getPosX(), square.getPosY()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) throws RemoteException {
    showWeapons(weapons, "Quale arma vuoi acquistare?", true);
  }

  public void showWeapons(List<Weapon> weapons, String prompt, boolean showCost) throws RemoteException {
    Log.print(prompt);

    int targetIndex = 0;
    int chosenTarget = 0;

    do {
      for (Weapon weapon : weapons) {
        if (showCost) {
          Log.print(
              String.format("\t%d) %s%n  Costo: %d rosso, %d blu, %d giallo",
                  targetIndex,
                  weapon.getName(),
                  weapon.getCost(AmmoColor.RED),
                  weapon.getCost(AmmoColor.BLUE),
                  weapon.getCost(AmmoColor.YELLOW)));
        } else {
          Log.print(String.format("\t%d) %s", targetIndex, weapon.getName()));
        }
        targetIndex++;
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    notifyObservers(
        new PlayerCollectWeaponEvent(client.getPlayerColor(),
            weapons.get(chosenTarget - 1).getName()));
  }
}
