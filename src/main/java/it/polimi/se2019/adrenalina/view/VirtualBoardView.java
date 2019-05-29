package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.ShowBoardInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDirectionSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSpawnPointTrackSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasAmmoCardsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSetSquareUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VirtualBoardView extends Observable implements BoardViewInterface {

  private static final long serialVersionUID = -4988173252714241460L;
  private transient Board board;
  private final transient VirtualClientSocket clientSocket;

  public VirtualBoardView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public Board getBoard() {
    return board;
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
    startTimer(0);
  }

  @Override
  public void showBoard() {
    clientSocket.sendEvent(new ShowBoardInvocation());
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    clientSocket.sendEvent(new ShowTargetSelectInvocation(type, targets));
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
  public void showSpawnPointTrackSelection() {
    clientSocket.sendEvent(new ShowSpawnPointTrackSelectionInvocation());
  }

  @Override
  public void update(BoardStatusUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(BoardSetSquareUpdate event) throws RemoteException {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(BoardHasWeaponsUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(BoardHasAmmoCardsUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(BoardKillShotsUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(DominationBoardDamagesUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(SquareAmmoCardUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(SquareWeaponUpdate event) {
    clientSocket.sendEvent(event);
  }

  @Override
  public void update(TimerSetEvent event) {
    // Not expected
  }

  @Override
  public void update(Event event) {
    // do nothing
  }
}
