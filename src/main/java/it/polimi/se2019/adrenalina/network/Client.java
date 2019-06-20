package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.App;
import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.ui.graphic.GUIBoardView;
import it.polimi.se2019.adrenalina.ui.graphic.GUICharactersView;
import it.polimi.se2019.adrenalina.ui.graphic.GUIPlayerDashboardsView;
import it.polimi.se2019.adrenalina.ui.text.TUIBoardView;
import it.polimi.se2019.adrenalina.ui.text.TUICharactersView;
import it.polimi.se2019.adrenalina.ui.text.TUIPlayerDashboardsView;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * The client object, one for each player.
 */
public abstract class Client implements ClientInterface, Serializable {

  private static final long serialVersionUID = -8182516938240148955L;
  private final String playerName;
  private PlayerColor playerColor = null;
  private boolean domination;
  private Long lastPing;

  private final BoardViewInterface boardView;
  private final CharactersViewInterface charactersView;
  private final PlayerDashboardsViewInterface playerDashboardsView;

  protected Client(String playerName, boolean domination, boolean tui) {
    this.playerName = playerName;
    this.domination = domination;

    if (tui) {
      boardView = new TUIBoardView(this);
      charactersView = new TUICharactersView(this, boardView);
      playerDashboardsView = new TUIPlayerDashboardsView(this, boardView);
    } else {
      boardView = new GUIBoardView(this);
      charactersView = new GUICharactersView(boardView);
      playerDashboardsView = new GUIPlayerDashboardsView(boardView);
    }
  }

  @Override
  public String getName() {
    return playerName;
  }

  @Override
  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void setPlayerColor(PlayerColor playerColor) {
    if (this.playerColor == null) {
      showGameMessage(
          String.format(
              "Il tuo personaggio è %s%s%s!",
              playerColor.getAnsiColor(),
              playerColor.getCharacterName(),
              ANSIColor.RESET
          ));
    }
    this.playerColor = playerColor;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void setDomination(boolean domination) {
    this.domination = domination;
  }

  @Override
  public void ping() {
    lastPing = System.currentTimeMillis();
  }

  @Override
  public Long getLastPing() {
    return lastPing;
  }

  /**
   * Shows a message with a given title and severity.
   * @param severity the severity of the message
   * @param title the title of the message
   * @param message the body of the message
   */
  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    switch (severity) {
      case GAME:
        Log.println(message);
        break;
      default:
        if (!"".equalsIgnoreCase(title)) {
          Log.println(String.format("%s: %s", severity, title));
        }
        Log.println(String.format("%s: %s", severity, message));
        break;
    }
  }

  /**
   * @see #showMessage(MessageSeverity, String, String)
   */
  @Override
  public void showMessage(MessageSeverity severity, String message) {
    showMessage(severity, "", message);
  }

  /**
   * @see #showMessage(MessageSeverity, String, String)
   */
  @Override
  public void showGameMessage(String message) {
    showMessage(MessageSeverity.GAME, "", message);
  }


  @Override
  public final BoardViewInterface getBoardView() {
    return boardView;
  }

  @Override
  public final CharactersViewInterface getCharactersView() {
    return charactersView;
  }

  @Override
  public final PlayerDashboardsViewInterface getPlayerDashboardsView() {
    return playerDashboardsView;
  }

  @Override
  public void disconnect(String message) {
    if (!message.isEmpty()) {
      showMessage(MessageSeverity.ERROR, "Disconnessione", message);
    }
    System.exit(0);
  }
}
