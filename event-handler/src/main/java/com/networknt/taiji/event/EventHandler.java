package com.networknt.taiji.event;

import java.util.concurrent.CompletableFuture;

/**
 * interface to indicator Event Handler
 *
 */
public interface EventHandler {

  /**
   * get event type which handled by the event handler
    * @return Class the class of the event
   */
  Class<Event> getEventType();

  /**
   * dispatch an event
   * @param de dispatched event
   * @return CompletableFuture
   */
  CompletableFuture<?> dispatch(DispatchedEvent<Event> de);
}
