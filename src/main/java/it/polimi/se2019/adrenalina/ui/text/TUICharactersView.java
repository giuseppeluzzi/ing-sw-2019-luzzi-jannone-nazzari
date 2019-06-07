package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import it.polimi.se2019.adrenalina.view.BoardViewInterface;
import it.polimi.se2019.adrenalina.view.CharactersView;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TUICharactersView extends CharactersView {

  private static final long serialVersionUID = 6283060302704172669L;
  private final transient ClientInterface client;

  private final BoardViewInterface boardView;

  public TUICharactersView(ClientInterface client, BoardViewInterface boardView) {
    super((BoardView) boardView);
    this.boardView = boardView;
    this.client = client;
  }

  @Override
  public void showDeath(PlayerColor playerColor) {

    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(client.getPlayerColor());
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    } catch (InvalidPlayerException e) {
      Log.critical("Player not found!");
      return;
    }

    if (player.getColor() == playerColor) {
      Log.println("Sei morto!\n");
    } else {
      Log.println(player.getName() + " è morto\n");
    }
  }
}
