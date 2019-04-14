package it.polimi.se2019.adrenalina.network;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerConnectEvent;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket implements ClientInterface, Runnable {
  private final String name;
  private final boolean domination;

  private Socket socket;

  private PrintWriter printWriter = null;
  private BufferedReader bufferedReader;

  public ClientSocket(String name, boolean domination) {
    this.name = name;
    this.domination = domination;

    try {
      socket = new Socket(Configuration.getInstance().getServerIP(), Configuration.getInstance().getSocketPort());
      OutputStream outputStream = socket.getOutputStream();
      printWriter = new PrintWriter(outputStream, true);
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
    } catch (IOException e) {
      Log.exception(e);
      return ;
    }

    sendEvent(new PlayerConnectEvent(name, domination));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDomination() {
    return domination;
  }

  @Override
  public void showMessage(String text) {
    Log.info(text);
  }

  @Override
  public void ping() {
    // useless
  }

  @Override
  public void disconnect()  {
    try {
      bufferedReader.close();
      printWriter.close();
      socket.close();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  public final void sendEvent(Event event) {
    printWriter.println(event.serialize());
    printWriter.flush();
  }

  @Override
  public void run() {
    while (socket.isConnected()) {
      String message = null;

      try {
        message = bufferedReader.readLine();
      } catch (IOException e) {
        Log.exception(e);
      }

      Log.info(message);
    }
  }
}
