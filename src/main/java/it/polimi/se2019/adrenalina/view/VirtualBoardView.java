package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.ShowBoardInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDirectionSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowFinalRanksInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSpawnPointTrackSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Virtual board view, relays invocations over the network to the actual board view.
 */
public class VirtualBoardView extends Observable implements BoardViewInterface {

  private static final long serialVersionUID = -4988173252714241460L;
  private transient Board board;
  private transient VirtualClientSocket clientSocket;

  public VirtualBoardView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void setClient(VirtualClientSocket client) {
    this.clientSocket = client;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void sendEvent(Event event) {
    // not needed here
  }

  @Override
  public void setBoard(Board board) {
    if (this.board != null) {
      board.removeObserver(this);
    }
    this.board = board;
    if (this.board != null) {
      board.addObserver(this);
      for (Square square : board.getSquares()) {
        square.addObserver(this);
      }
    }
  }

  @Override
  public void startTimer(int time) {
    clientSocket.sendEvent(new TimerSetEvent(time));
  }

  @Override
  public void hideTimer() {
    clientSocket.sendEvent(new TimerSetEvent(0));
  }

  @Override
  public Timer getTimer() {
    // not needed here
    return null;
  }

  @Override
  public void endLoading(boolean masterPlayer) {
    // not needed here
  }

  @Override
  public void cancelInput() {
    // do nothing
  }

  @Override
  public void showBoard() {
    clientSocket.sendEvent(new ShowBoardInvocation());
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets, boolean skippable) {
    clientSocket.sendEvent(new ShowTargetSelectInvocation(type, targets, skippable));
  }

  @Override
  public void showDirectionSelect() {
    clientSocket.sendEvent(new ShowDirectionSelectInvocation());
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    List<Square> squareList = new ArrayList<>();
    for (Target target : targets) {
      squareList.add(target.getSquare());
    }
    clientSocket.sendEvent(new ShowSquareSelectInvocation(squareList));
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) {
    clientSocket.sendEvent(new ShowBuyableWeaponsInvocation(weapons));
  }

  @Override
  public void showSpawnPointTrackSelection(Map<AmmoColor, Integer> damages) {
    clientSocket.sendEvent(new ShowSpawnPointTrackSelectionInvocation(damages));
  }

  @Override
  public void showFinalRanks() {
    clientSocket.sendEvent(new ShowFinalRanksInvocation());
  }

  @Override
  public void showDisconnectWarning() {
    // not needed here
  }

  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        clientSocket.sendEvent(event);
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
