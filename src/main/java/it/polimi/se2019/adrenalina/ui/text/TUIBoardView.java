package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.*;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;
import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
import it.polimi.se2019.adrenalina.view.BoardView;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.se2019.adrenalina.ui.UIUtils.getFirstKillshotIndex;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Text User Interface board view
 */
public class TUIBoardView extends BoardView {

  private static final long serialVersionUID = 7696019255617335385L;
  private static final String WAIT_TIMEOUT_MSG = "Tempo di attesa scaduto! Salti il turno!";

  private final transient TUIInputManager inputManager = new TUIInputManager();
  private final transient TUIInputManager preGameInputManager = new TUIInputManager();
  private transient Thread endLoadingThread;
  private final Timer timer = new Timer();

  public TUIBoardView(Client client) {
    super(client, new TUITimer(client));
  }

  @Override
  public void endLoading(boolean masterPlayer) {
    endLoadingThread = new Thread(() -> {
      showChangePlayerColor();
      if (masterPlayer) {
        showGameMapSelection();
        showSkullsSelection();
        showFinalFrenzySelection();
      }
      Log.println("È tutto pronto!");
    });
    endLoadingThread.start();
  }

  @Override
  public void cancelInput() {
    if (endLoadingThread.isAlive()) {
      Log.println("Tempo scaduto! È ora di iniziare la partita.");
    }
    endLoadingThread.interrupt();
    preGameInputManager.cancel(null);
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
            notifyObservers(new SkipSelectionEvent(getClient().getPlayerColor()));
          } else if (chosenTarget.isPlayer()) {
            notifyObservers(new SelectPlayerEvent(getClient().getPlayerColor(),
                chosenTarget.getPlayer().getColor()));
          } else {
            notifyObservers(
                new SelectSquareEvent(getClient().getPlayerColor(),
                chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
        case MOVE_SQUARE:
          chosenTarget = selectSquare(targets, true, skippable);
          if (chosenTarget == null) {
            notifyObservers(new SkipSelectionEvent(getClient().getPlayerColor()));
          } else {
          notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
        case ATTACK_SQUARE:
          chosenTarget = selectSquare(targets, false, skippable);
          if (chosenTarget == null) {
            notifyObservers(new SkipSelectionEvent(getClient().getPlayerColor()));
          } else {
            notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
                chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
        case ATTACK_ROOM:
          chosenTarget = selectRoom(targets, skippable);
          if (chosenTarget == null) {
            notifyObservers(new SkipSelectionEvent(getClient().getPlayerColor()));
          } else {
          notifyObservers(new SelectSquareEvent(getClient().getPlayerColor(),
              chosenTarget.getSquare().getPosX(), chosenTarget.getSquare().getPosY()));
          }
          break;
      }
    } catch (InvalidSquareException | InputCancelledException | RemoteException e) {
      //
    }
  }

  /**
   * Show room selection prompt to the user.
   * @param targets a list of targets to show
   */
  private Target selectRoom(List<Target> targets, boolean skippable) throws InputCancelledException {
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
    if (skippable) {
      choices.add("Salta azione");
    }
    inputManager.input("Seleziona una stanza:", choices);
    timer.start(Configuration.getInstance().getTurnTimeout(), () -> inputManager.cancel(
        WAIT_TIMEOUT_MSG));
    int inputResult = inputManager.waitForIntResult();
    timer.stop();
    if (skippable && inputResult == targets.size()) {
      return null;
    }
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
  private Target selectSquare(List<Target> targets, boolean fetch, boolean skippable) throws InputCancelledException {
    List<String> choices = new ArrayList<>();
    for (Target target : targets) {
      String fetchHelper = "";
      if (target.getSquare().isSpawnPoint()) {
        fetchHelper = "(punto di generazione)";
      } else if (fetch) {
        if (target.getSquare().getAmmoCard() != null) {
          fetchHelper =
              "(tessera munizioni " + ANSIColor.WHITE + target.getSquare().getAmmoCard() + ANSIColor.RESET
                  + ")";
        }
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
    if (skippable) {
      choices.add("Salta azione");
    }
    inputManager.input("Seleziona un quadrato:", choices);
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
                          .format("%sx: %d y:%d (punto di generazione)%s",
                                  target.getSquare().getColor().getAnsiColor(),
                                  target.getSquare().getPosX(),
                                  target.getSquare().getPosY(),
                                  ANSIColor.RESET));
        }
      } catch (InvalidSquareException ignored) {
        //
      }
    }
    if (skippable) {
      choices.add("Salta (non colpire nessuno)");
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
      square = (Square) selectSquare(targets, true, false);
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
  public void showSpawnPointTrackSelection(Map<AmmoColor, Integer> damages) {
    AmmoColor chosen = null;
    try {
      chosen = TUIUtils.showAmmoColorSelection(false, damages);
    } catch (InputCancelledException e) {
      return;
    }
    try {
      notifyObservers(new SpawnPointDamageEvent(getClient().getPlayerColor(), chosen));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  /**
   * Asks user if they whish to change their player color once connected.
   */
  private void showChangePlayerColor() {
    try {
      while (true) {
        List<PlayerColor> availableColors = new ArrayList<>(getBoard().getFreePlayerColors());
        if (! availableColors.isEmpty()) {
          List<String> choices = new ArrayList<>();
          for (PlayerColor color : availableColors) {
            choices.add(color.getAnsiColor() + color.getCharacterName() + ANSIColor.RESET);
          }
          choices.add(String.format("Mantieni il personaggio attuale (%s%s%s)",
                  getClient().getPlayerColor().getAnsiColor(),
                  getClient().getPlayerColor().getCharacterName(),
                  ANSIColor.RESET));
          getClient().suspendOutput(true);
          preGameInputManager.input("Se vuoi cambiare personaggio, sceglilo ora:", choices);
          int result = preGameInputManager.waitForIntResult();
          getClient().suspendOutput(false);
          if (result == availableColors.size()) {
            return;
          }
          if (getBoard().getFreePlayerColors().contains(availableColors.get(result))) {
            Log.println("Il tuo personaggio è ora " + availableColors.get(result).getAnsiColor() + availableColors.get(result).getCharacterName() + ANSIColor.RESET);
            sendEvent(new PlayerColorSelectionEvent(getClient().getPlayerColor(), availableColors.get(result)));
            return;
          } else {
            Log.println("Troppo tardi, quel personaggio è già stato preso da un altro giocatore!");
          }
        }
      }
    } catch (RemoteException | InputCancelledException e) {
      getClient().suspendOutput(false);
    }
  }

  /**
   * Asks master player to choose a game map.
   */
  private void showGameMapSelection() {
    List<String> mapNames = new ArrayList<>(
        Arrays.asList(
            "Mappa 1 (3 - 4 giocatori)",
            "Mappa 2",
            "Mappa 3 (4 - 5 giocatori)",
            "Mappa 4"));
    preGameInputManager.input("Scegli la mappa da usare in questa partita", mapNames);
    try {
      getClient().suspendOutput(true);
      int mapId = preGameInputManager.waitForIntResult() + 1;
      getClient().suspendOutput(false);
      sendEvent(new MapSelectionEvent(mapId));
    } catch (InputCancelledException | RemoteException e) {
      getClient().suspendOutput(false);
    }
  }

  /**
   * Asks master player to choose the number of skulls to play with.
   */
  private void showSkullsSelection() {
    getClient().suspendOutput(true);
    while (true) {
      preGameInputManager.input("Scegli il numero di teschi da usare (1-8):");
      String input;
      try {
        input = preGameInputManager.waitForStringResult().trim();
        getClient().suspendOutput(false);
      } catch (InputCancelledException e) {
        getClient().suspendOutput(false);
        return;
      }
      if (input.matches("^[1-8]$")) {
        try {
          sendEvent(new BoardSkullsUpdate(Integer.parseInt(input)));
          getClient().suspendOutput(false);
          return;
        } catch (RemoteException e) {
          Log.exception(e);
          getClient().suspendOutput(false);
          return;
        }
      }
      Log.println("Selezione non valida!");
    }
  }

  /**
   * Asks master player to choose whether to use Final Frenzy at the end of the game.
   */
  private void showFinalFrenzySelection() {
    getClient().suspendOutput(true);
    while (true) {
      preGameInputManager.input("Vuoi attivare la frenesia finale al termine della partita? [s/n]");
      String input;
      try {
        input = preGameInputManager.waitForStringResult().trim().toLowerCase();
        getClient().suspendOutput(false);
      } catch (InputCancelledException e) {
        getClient().suspendOutput(false);
        return;
      }
      if (input.matches("^[sn]$")) {
        try {
          sendEvent(new FinalFrenzyToggleEvent("s".equals(input)));
          getClient().suspendOutput(false);
          return;
        } catch (RemoteException e) {
          Log.exception(e);
          getClient().suspendOutput(false);
          return;
        }
      }
      Log.println("Selezione non valida!");
    }
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
