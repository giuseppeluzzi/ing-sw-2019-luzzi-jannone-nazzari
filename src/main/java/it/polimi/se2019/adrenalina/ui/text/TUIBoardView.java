package it.polimi.se2019.adrenalina.ui.text;

import static org.fusesource.jansi.Ansi.ansi;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Direction;
import it.polimi.se2019.adrenalina.model.Kill;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Text User Interface board view
 */
public class TUIBoardView extends BoardView {

  private static final long serialVersionUID = 7696019255617335385L;
  private static final String WAIT_TIMEOUT_MSG = "Tempo di attesa scaduto! Salti il turno!";

  private final transient TUIInputManager inputManager = new TUIInputManager();
  private final Timer timer = new Timer();

  public TUIBoardView(Client client) {
    super(client, new TUITimer(client));
  }

  @Override
  public void endLoading(boolean masterPlayer) {

  }

  /**
   * Print the textual game board.
   */
  @Override
  public void showBoard() {
    Log.print(ansi().eraseScreen().toString());
    BoardPrinter.print(getBoard());
  }

  /**
   * Show target selection prompt to the user.
   * @param type the target type
   * @param targets a list of targets to show
   */
  @Override
  public void showTargetSelect(TargetType type, List<Target> targets, boolean skippable) {
    Target chosenTarget;

    showBoard();
    try {
      switch (type) {
        case ATTACK_TARGET:
          chosenTarget = selectAttackTarget(targets, skippable);
          if (chosenTarget == null) {
            notifyObservers(new SkipSelectionEvent());
          } else if (chosenTarget.isPlayer()) {
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
    } catch (InvalidSquareException | InputCancelledException e) {
      //
    }
  }

  /**
   * Show room selection prompt to the user.
   * @param targets a list of targets to show
   */
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
    timer.start(Configuration.getInstance().getTurnTimeout(), () -> inputManager.cancel(
        WAIT_TIMEOUT_MSG));
    int inputResult = inputManager.waitForIntResult();
    timer.stop();
    for (Target target : targets) {
      if (target.getSquare().getColor() == squareColors.toArray()[inputResult]) {
        return target;
      }
    }

    throw new IllegalStateException("");
  }

  /**
   * Show square selection prompt to the user.
   * @param targets a list of targets to show
   * @param fetch true if ammoCards on each square should be shown, false otherwise
   */
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
    timer.start(Configuration.getInstance().getTurnTimeout(),
        () -> inputManager.cancel(WAIT_TIMEOUT_MSG));
    Target result = targets.get(inputManager.waitForIntResult());
    timer.stop();
    return result;
  }

  /**
   * Show target selection prompt to the user for attacking.
   * @param targets a list of targets to show
   */
  private Target selectAttackTarget(List<Target> targets, boolean skippable) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Target target : targets) {
      try {
        if (target.isPlayer()) {
          choices.add(String.format("%s%s%s", target.getPlayer().getColor().getAnsiColor(),
              target.getPlayer().getName(), ANSIColor.RESET));
        } else {
          choices.add(
              String
                  .format("%sx: %d y:%d (Spawnpoint)%s",
                      target.getSquare().getColor().getAnsiColor(),
                      target.getSquare().getPosX(),
                      target.getSquare().getPosY(),
                      ANSIColor.RESET));
        }
        if (skippable) {
          choices.add("Salta (non colpire nessuno)");
        }
      } catch (InvalidSquareException ignored) {
        //
      }
    }
    inputManager.input("Seleziona un bersaglio:", choices);
    timer.start(Configuration.getInstance().getTurnTimeout(),
        () -> inputManager.cancel(WAIT_TIMEOUT_MSG));
    int selectionResult = inputManager.waitForIntResult();
    if (skippable && selectionResult == targets.size()) {
      return null;
    }
    Target result = targets.get(selectionResult);
    timer.stop();
    return result;
  }

  /**
   * Show direction selection prompt to the user.
   */
  @Override
  public void showDirectionSelect() {
    List<String> choices = new ArrayList<>();
    for (Direction direction : Direction.values()) {
      choices.add(direction.getName());
    }
    inputManager.input("Seleziona una direzione:", choices);
    timer.start(Configuration.getInstance().getTurnTimeout(),
        () -> inputManager.cancel(WAIT_TIMEOUT_MSG));
    try {
      notifyObservers(new SelectDirectionEvent(getClient().getPlayerColor(),
          Direction.values()[inputManager.waitForIntResult()]));
      timer.stop();
    } catch (RemoteException e) {
      Log.exception(e);
    } catch (InputCancelledException e) {
      // return
    }
  }

  /**
   * Show square selection prompt to the user.
   * @param targets a list of targets to show
   */
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

  /**
   * Show a list of buyable weapons to the user.
   * @param weapons the list of weapons to show
   */
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

  /**
   * Show spawn point track selection prompt to the user.
   */
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

  private int getFirstKillshotIndex(List<Kill> kills, PlayerColor playerColor) {
    for (int i = 0; i < kills.size(); i++) {
      if (kills.get(i).getPlayerColor() == playerColor) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Print final game ranks.
   */
  @Override
  public void showFinalRanks() {
    Log.println("Classifica finale\n");
    List<Player> players = new ArrayList<>(getBoard().getPlayers());
    Collections.sort(players, (p1, p2) -> {
      if (p1.getScore() == p2.getScore()) {
        int p1Index = getFirstKillshotIndex(getBoard().getKillShots(), p1.getColor());
        int p2Index = getFirstKillshotIndex(getBoard().getKillShots(), p2.getColor());
        if (p1Index == p2Index) {
          return 0;
        }
        return p1Index < p2Index ? -1 : 1;
      }
      return p1.getScore() > p2.getScore() ? -1 : 1;
    });
    for (int i = 0; i < players.size(); i++) {
      Log.println(String.format("    %s#%d %s (%d punti)%s",
          players.get(i).getColor().getAnsiColor(),
          i + 1,
          players.get(i).getName(),
          players.get(i).getScore(),
          ANSIColor.RESET));
    }
  }
}
