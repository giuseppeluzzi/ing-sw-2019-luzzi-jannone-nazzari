package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation to print a message with a certain severity on the client.
 */
public class ShowMessageInvocation implements Invocation {

  private static final long serialVersionUID = 7814951540331867139L;
  private final MessageSeverity severity;
  private final String title;
  private final String message;

  public ShowMessageInvocation(MessageSeverity severity, String title, String message) {
    this.severity = severity;
    this.title = title;
    this.message = message;
  }

  public MessageSeverity getSeverity() {
    return severity;
  }

  public String getTitle() {
    return title;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_MESSAGE_INVOCATION;
  }
}
