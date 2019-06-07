package it.polimi.se2019.adrenalina.ui.text;

import static org.fusesource.jansi.Ansi.ansi;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectSquareEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class TUIBoardView extends BoardView {

  private static final long serialVersionUID = 7696019255617335385L;

  private final transient TUIInputManager inputManager = TUIUtils.getInputManager();

  public TUIBoardView(ClientInterface client) {
    super(client);
  }

  @Override
  public void showBoard() {
    Log.print(ansi().eraseScreen().toString());
    BoardPrinter.print(getBoard());
  }

  public TUIInputManager getInputManager() {
    return inputManager;
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
    } catch (InputCancelledException e) {
      // return
    }
  }

  private Target selectRoom(List<Target> targets) throws InputCancelledException {
    EnumSet<SquareColor> squareColors = EnumSet.noneOf(SquareColor.class);

    for (Target target : targets) {
      squareColors.add(target.getSquare().getColor());
    }

    showBoard();
    List<String> choices = new ArrayList<>();

    for (SquareColor color : squareColors) {
      choices.add(
          String.format("%s%s%s",
              color.getAnsiColor(),
              color,
              ANSIColor.RESET)
      );
    }
    inputManager.input("Seleziona una stanza:", choices);
    int inputResult = inputManager.waitForIntResult();
    for (Target target : targets) {
      if (target.getSquare().getColor() == squareColors.toArray()[inputResult]) {
        return target;
      }
    }

    throw new IllegalStateException("");
  }

  private Target selectSquare(List<Target> targets, boolean fetch) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Target target : targets) {
      String fetchHelper = "";
      if (target.getSquare().isSpawnPoint()) {
        fetchHelper = "(Spawnpoint)";
      } else if (fetch) {
        fetchHelper =
            "(" + ANSIColor.WHITE + target.getSquare().getAmmoCard() + ANSIColor.RESET
                + ")";
      }
      choices.add(
          String.format("%sx: %d y:%d %s%s%s",
              target.getSquare().getColor().getAnsiColor(),
              target.getSquare().getPosX(),
              target.getSquare().getPosY(),
              ANSIColor.RESET,
              fetchHelper,
              ANSIColor.RESET));
    }
    inputManager.input("Seleziona un quadrato:", choices);
    return targets.get(inputManager.waitForIntResult());
  }

  private Target selectAttackTarget(List<Target> targets) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Target target : targets) {
      try {
        if (target.isPlayer()) {
          choices.add(String.format("%s", target.getPlayer().getName()));
        } else {
          choices.add(
              String
                  .format("%sx: %d y:%d (Spawnpoint)%s",
                      target.getSquare().getColor().getAnsiColor(),
                      target.getSquare().getPosX(),
                      target.getSquare().getPosY(),
                      ANSIColor.RESET));
        }
      } catch (InvalidSquareException ignored) {
        //
      }
    }
    inputManager.input("Seleziona un bersaglio:", choices);
    return targets.get(inputManager.waitForIntResult());
  }

  @Override
  public void showDirectionSelect() {
    List<String> choices = new ArrayList<>();
    for (Direction direction : Direction.values()) {
      choices.add(direction.toString());
    }
    inputManager.input("Seleziona una direzione:", choices);
    try {
      notifyObservers(new SelectDirectionEvent(getClient().getPlayerColor(),
          Direction.values()[inputManager.waitForIntResult()]));
    } catch (RemoteException e) {
      Log.exception(e);
    } catch (InputCancelledException e) {
      // return
    }
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    Square square = null;
    try {
      square = (Square) selectSquare(targets, true);
    } catch (InputCancelledException e) {
      return;
    }
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
    String weapon = null;
    try {
      weapon = TUIUtils.selectWeapon(weapons, "Quale arma vuoi acquistare?", true);
    } catch (InputCancelledException e) {
      return;
    }
    notifyObservers(new PlayerCollectWeaponEvent(getClient().getPlayerColor(), weapon));
  }

  @Override
  public void showSpawnPointTrackSelection() {
    AmmoColor chosen = null;
    try {
      chosen = TUIUtils.showAmmoColorSelection(false);
    } catch (InputCancelledException e) {
      return;
    }
    try {
      notifyObservers(new SpawnPointDamageEvent(getClient().getPlayerColor(), chosen));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
