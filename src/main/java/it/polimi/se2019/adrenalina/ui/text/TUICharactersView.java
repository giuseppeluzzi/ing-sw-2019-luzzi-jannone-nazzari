package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.CharactersView;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TUICharactersView extends CharactersView {

  private static final long serialVersionUID = 6283060302704172669L;
  private final transient ClientInterface client;
  private final transient Scanner scanner = new Scanner(System.in, "utf-8");

  public TUICharactersView(ClientInterface client) {
    this.client = client;
  }

  @Override
  public void showDeath(PlayerColor playerColor) {

    Player player;
    try {
      player = getPlayerByColor(client.getPlayerColor());
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    if (player.getColor() == playerColor) {
      Log.print("Sei morto!\n");
    } else {
      Log.print(player.getName() + " è morto\n");
    }
  }
}
