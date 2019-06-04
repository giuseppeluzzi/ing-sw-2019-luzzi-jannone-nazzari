package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.utils.Log;
import java.util.List;

/**
 * This class implements an input manager for prompting and validating user input.
 */
public class TUIInputManager {

  private final InterruptibleScanner scanner = new InterruptibleScanner();
  private static final String INVALID_SELECTION_TEXT = "Selezione non valida, riprova.";
  private Integer intResult;
  private String stringResult;
  private Thread thread;
  private final Object lock = new Object();

  /**
   * Blocking method that waits for an Integer result to be available, then returns it.
   * @return the Integer result of the user prompt
   * @throws InterruptedException thrown if the prompt thread is interrupted before the input
   * data is available
   */
  public int waitForIntResult() throws InterruptedException {
    synchronized (lock) {
      while (intResult == null) {
        lock.wait();
      }
      Integer result = intResult;
      intResult = null; // cleanup for next use
      return result;
    }
  }

  /**
   * Blocking method that waits for a String result to be available, then returns it.
   * @return the String result of the user prompt
   * @throws InterruptedException thrown if the prompt thread is interrupted before the input
   * data is available
   */
  public String waitForStringResult() throws InterruptedException {
    synchronized (lock) {
      while (stringResult == null) {
        lock.wait();
      }
      String result = stringResult;
      stringResult = null; // cleanup for next use
      return result;
    }
  }

  /**
   * Non-blocking method that prompts the user for a choice between a given set of options. Reads
   * and validates the user's input and stores the index of the selected choice in {@code intResult}.
   * @param prompt the prompt text
   * @param choices a list of possible choices
   */
  public void input(String prompt, List<String> choices) {
      thread = new Thread(() -> {
        Log.println(prompt);
        for (int i = 0; i < choices.size(); i++) {
          Log.println(String.format("    [%2d] %s", i+1, choices.get(i)));
        }
        while (true) {
          Log.print("> ");
          String input;
          try {
            input = scanner.call().trim();
          } catch (InterruptedException e) {
            return;
          }
          if (input.matches("\\d+")) {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= choices.size()) {
              synchronized (lock) {
                intResult = choice - 1;
                lock.notifyAll();
              }
              return;
            } else {
              Log.println(INVALID_SELECTION_TEXT);
            }
          } else {
            Log.println(INVALID_SELECTION_TEXT);
          }
        }
      });
      thread.start();
  }

  /**
   * Non-blocking method that prompts the user for a string with a maximum length. Reads
   * and validates the user's input and stores it in {@code stringResult}.
   * @param prompt the prompt text
   * @param maxLength the maximum input string length
   */
  public void input(String prompt, int maxLength) {
      thread = new Thread(() -> {
        Log.println(prompt);
        while (true) {
          Log.print("> ");
          String input;
          try {
            input = scanner.call().trim();
          } catch (InterruptedException e) {
            return;
          }
          if (input.length() >= 1 && input.length() <= maxLength) {
            synchronized (lock) {
              stringResult = input;
              lock.notifyAll();
            }
            return;
          } else {
            Log.println(INVALID_SELECTION_TEXT);
          }
        }
      });
      thread.start();
  }

  /**
   * Non-blocking method that prompts the user for a string. Alias for
   * {@code input(prompt, Integer.MAX_VALUE)}.
   * @param prompt the prompt text
   */
  public void input(String prompt) {
    input(prompt, Integer.MAX_VALUE);
  }

  /**
   * Interrupts the user prompt.
   */
  public void cancel() {
    thread.interrupt();
  }
}
