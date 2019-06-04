package it.polimi.se2019.adrenalina.ui.text;

import static java.nio.charset.StandardCharsets.UTF_8;

import it.polimi.se2019.adrenalina.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * This class implements a Scanner that can be interrupted. If that happens, an
 * {@code InterruptedException} is thrown.
 */
public class InterruptibleScanner implements Callable<String> {

  @Override
  public String call() throws InterruptedException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in, UTF_8));
    String input;
    try {
      while (!br.ready()) {
        Thread.sleep(200);
      }
      input = br.readLine();
    } catch (IOException e) {
      Log.critical(e.toString());
      return null;
    }
    return input;
  }
}