package it.polimi.se2019.adrenalina.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.BoardController;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.event.PlayerSetColorEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersViewInterface;
import it.polimi.se2019.adrenalina.view.PlayerDashboardsViewInterface;
import it.polimi.se2019.adrenalina.view.VirtualBoardView;
import it.polimi.se2019.adrenalina.view.VirtualCharactersView;
import it.polimi.se2019.adrenalina.view.VirtualPlayerDashboardsView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VirtualClientSocket implements ClientInterface, Runnable {

  private final Socket clientSocket;
  private final Server server;

  private String name;
  private PlayerColor playerColor;
  private boolean domination;
  private Long lastPing;

  private PrintWriter printWriter;
  private BufferedReader bufferedReader;

  private BoardController game;

  private BoardViewInterface boardView;
  private CharactersViewInterface charactersView;
  private PlayerDashboardsViewInterface playerDashboardsView;

  public VirtualClientSocket(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;

    boardView = new VirtualBoardView(this);
    charactersView = new VirtualCharactersView(this);
    playerDashboardsView = new VirtualPlayerDashboardsView(this);

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
          StandardCharsets.UTF_8));
      OutputStream outputStream = clientSocket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
          StandardCharsets.UTF_8));
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  @Override
  public void run() {
    try {
      while (clientSocket.isConnected()) {
        String message = bufferedReader.readLine();

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(message, JsonObject.class);

        EventType eventType = EventType.valueOf(json.get("eventType").getAsString());
        Event event = gson.fromJson(message, eventType.getEventClass());

        if (eventType == EventType.PLAYER_CONNECT_EVENT) {
          PlayerConnectEvent connectEvent = gson.fromJson(message, PlayerConnectEvent.class);
          name = connectEvent.getPlayerName();
          domination = connectEvent.isDomination();
          server.addClient(this);
          game = server.getGameByClient(this);
        } else {
          game.update(event);
          game.getAttackController().update(event);
          game.getPlayerController().update(event);
        }
      }
    } catch (InvalidPlayerException | IOException ignored) {
      server.clientDisconnect(this);
    }
  }

  @Override
  public String getName() {
    // TODO: check if connected
    return name;
  }

  @Override
  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void setPlayerColor(PlayerColor color) {
    playerColor = color;
    sendEvent(new PlayerSetColorEvent(color));
  }

  @Override
  public boolean isDomination() {
    // TODO: check if connected
    return domination;
  }

  @Override
  public void setDomination(boolean domination) {
    this.domination = domination;
  }

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    // TODO
  }

  @Override
  public void ping() {
    if (clientSocket.isClosed() || !clientSocket.isConnected()) {
      server.clientDisconnect(this);
    }
    lastPing = System.currentTimeMillis();
  }

  @Override
  public Long getLastPing() {
    return lastPing;
  }

  @Override
  public void disconnect() {
    try {
      bufferedReader.close();
      printWriter.close();
      clientSocket.close();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  public void setGame(BoardController game) {
    this.game = game;
  }

  public void setBoardView(BoardViewInterface boardView) {
    this.boardView = boardView;
  }

  public void setCharactersView(CharactersViewInterface charactersView) {
    this.charactersView = charactersView;
  }

  public void setPlayerDashboardsView(
      PlayerDashboardsViewInterface playerDashboardsView) {
    this.playerDashboardsView = playerDashboardsView;
  }

  @Override
  public BoardViewInterface getBoardView() {
    return boardView;
  }

  @Override
  public CharactersViewInterface getCharactersView() {
    return charactersView;
  }

  @Override
  public PlayerDashboardsViewInterface getPlayerDashboardsView() {
    return playerDashboardsView;
  }

  public void sendEvent(Event event) {
    printWriter.println(event.serialize());
    printWriter.flush();
  }
}
