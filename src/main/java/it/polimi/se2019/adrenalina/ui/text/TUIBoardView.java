package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectSquareEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class TUIBoardView extends BoardView {

  private static final long serialVersionUID = 7696019255617335385L;
  private final transient Scanner scanner = TUIUtils.getScanner();

  public TUIBoardView(ClientInterface client) {
    super(client);
  }

  @Override
  public void showBoard() {
    try {
      MapPrinter.print(getBoard(), getClient().getPlayerColor());
    } catch (RemoteException e) {
      // ignore exception
    }
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    Target chosenTarget;

    showBoard();
    try {
      switch (type) {
        case ATTACK_TARGET:
          chosenTarget = selectAttackTarget(targets);
          if (chosenTarget.isPlayer()) {
            notifyObservers(new SelectPlayerEvent(getClient().getPlayerColor(),
                chosenTarget.getPlayer().getColor()));
          } else {
            notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
                chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
        case MOVE_SQUARE:
          chosenTarget = selectSquare(targets, true);
          notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          break;
        case ATTACK_SQUARE:
          chosenTarget = selectSquare(targets, false);
          notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          break;
        case ATTACK_ROOM:
          chosenTarget = selectRoom(targets);
          notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
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
    int targetIndex;
    int chosenTarget;

    EnumSet<SquareColor> squareColors = EnumSet.noneOf(SquareColor.class);

    for (Target target : targets) {
      squareColors.add(target.getSquare().getColor());
    }

    showBoard();
    do {
      targetIndex = 1;
      Log.println("Seleziona una stanza");
      for (SquareColor color : squareColors) {
        Log.println(
            String.format("\t%d) %s%s%s",
                targetIndex,
                color.getAnsiColor(),
                color,
                ANSIColor.RESET)
        );
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

  private Target selectSquare(List<Target> targets, boolean fetch) {
    int targetIndex;
    int chosenTarget;

    Log.println("Seleziona un quadrato");
    do {
      targetIndex = 1;
      for (Target target : targets) {
        String fetchHelper = "";
        if (target.getSquare().isSpawnPoint()) {
          fetchHelper = "(Spawnpoint)";
        } else if (fetch) {
          fetchHelper =
              "(" + ANSIColor.WHITE + target.getSquare().getAmmoCard() + ANSIColor.RESET
                  + ")";
        }
        Log.println(
            String.format("\t%d) %sx: %d y:%d %s%s%s",
                targetIndex,
                target.getSquare().getColor().getAnsiColor(),
                target.getSquare().getPosX(),
                target.getSquare().getPosY(),
                ANSIColor.RESET,
                fetchHelper,
                ANSIColor.RESET));
        targetIndex++;
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    return targets.get(chosenTarget - 1);
  }

  private Target selectAttackTarget(List<Target> targets) {
    int targetIndex;
    int chosenTarget;

    Log.println("Seleziona un bersaglio");
    do {
      targetIndex = 1;
      for (Target target : targets) {
        try {
          if (target.isPlayer()) {
            Log.println(String.format("\t%d) %s", targetIndex, target.getPlayer().getName()));
          } else {
            Log.println(
                String
                    .format("\t%d) %sx: %d y:%d (Spawnpoint)%s",
                        targetIndex,
                        target.getSquare().getColor().getAnsiColor(),
                        target.getSquare().getPosX(),
                        target.getSquare().getPosY(),
                        ANSIColor.RESET));
          }
          targetIndex++;
        } catch (InvalidSquareException ignored) {
          //
        }
      }
      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget < 1 || chosenTarget >= targetIndex);

    return targets.get(chosenTarget - 1);
  }

  @Override
  public void showDirectionSelect() {
    int targetIndex;
    int chosenTarget;

    do {
      targetIndex = 1;
      Log.println("Seleziona una direzione");
      for (Direction direction : Direction.values()) {
        Log.println("\t" + targetIndex + ") " + direction);
        targetIndex++;
      }

      chosenTarget = Character.getNumericValue(scanner.nextLine().charAt(0));
    } while (chosenTarget == 0 || chosenTarget >= targetIndex);

    try {
      notifyObservers(new SelectDirectionEvent(getClient().getPlayerColor(),
          Direction.values()[chosenTarget - 1]));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    Square square = (Square) selectSquare(targets, true);
    try {
      notifyObservers(new SquareMoveSelectionEvent(getClient().getPlayerColor(),
          square.getPosX(),
          square.getPosY()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) throws RemoteException {
    String weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi acquistare?", true);
    notifyObservers(new PlayerCollectWeaponEvent(getClient().getPlayerColor(), weapon));
  }

  @Override
  public void showSpawnPointTrackSelection() {
    AmmoColor chosen = TUIUtils.showAmmoColorSelection(false);
    try {
      notifyObservers(new SpawnPointDamageEvent(getClient().getPlayerColor(), chosen));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
