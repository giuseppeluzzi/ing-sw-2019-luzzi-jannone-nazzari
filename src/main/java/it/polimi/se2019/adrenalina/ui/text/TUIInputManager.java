package it.polimi.se2019.adrenalina.ui.text;

import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
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
  private boolean cancelled;
  private final Object lock = new Object();

  /**
   * Blocking method that waits for an Integer result to be available, then returns it.
   * @return the Integer result of the user prompt
   * @throws InputCancelledException thrown if the prompt thread is interrupted before the input
   * data is available
   */
  public int waitForIntResult() throws InputCancelledException {
    synchronized (lock) {
      while (intResult == null && ! cancelled) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      if (cancelled) {
        throw new InputCancelledException();
      }
      return intResult;
    }
  }

  /**
   * Blocking method that waits for a String result to be available, then returns it.
   * @return the String result of the user prompt
   * @throws InputCancelledException thrown if the prompt thread is interrupted before the input
   * data is available
   */
  public String waitForStringResult() throws InputCancelledException {
    synchronized (lock) {
      while (stringResult == null && ! cancelled) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      if (cancelled) {
        throw new InputCancelledException();
      }
      return stringResult;
    }
  }

  /**
   * Non-blocking method that prompts the user for a choice between a given set of options. Reads
   * and validates the user's input and stores the index of the selected choice in {@code intResult}.
   * @param prompt the prompt text
   * @param choices a list of possible choices
   */
  public void input(String prompt, List<String> choices) {
    intResult = null;
    stringResult = null;
    cancelled = false;
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
          synchronized (lock) {
            cancelled = true;
            lock.notifyAll();
            Thread.currentThread().interrupt();
            return;
          }
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
   * Non-blocking method that prompts the user for a string with length constrains. Reads
   * and validates the user's input and stores it in {@code stringResult}.
   * @param prompt the prompt text
   * @param minLength the minimum input string length
   * @param maxLength the maximum input string length
   */
  public void input(String prompt, int minLength, int maxLength) {
    intResult = null;
    stringResult = null;
    cancelled = false;
    thread = new Thread(() -> {
      Log.println(prompt);
      while (true) {
        Log.print("> ");
        String input;
        try {
          input = scanner.call().trim();
        } catch (InterruptedException e) {
          synchronized (lock) {
            cancelled = true;
            lock.notifyAll();
            Thread.currentThread().interrupt();
            return;
          }
        }
        if (input.length() >= minLength && input.length() <= maxLength) {
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
   * Non-blocking method that prompts the user for a non-empty string. Alias for
   * {@code input(prompt, 1, Integer.MAX_VALUE)}.
   * @param prompt the prompt text
   */
  public void input(String prompt) {
    input(prompt, 1, Integer.MAX_VALUE);
  }

  /**
   * Interrupts the user prompt.
   */
  public void cancel(String message) {
    if (message != null) {
      Log.println(message);
    }
    if (thread != null) {
      thread.interrupt();
    }
  }
}
