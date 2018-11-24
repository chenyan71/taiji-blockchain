package com.networknt.taiji.event;


import java.util.Map;
import java.util.Optional;

/**
 * value object class for Dispatched Event
 *
 */
public class DispatchedEvent<T extends Event> implements EventEnvelope<T> {

  private String entityId;
  private EventId eventId;
  private T event;
  private Integer swimlane;
  private final Long offset;
  private final EventContext eventContext;
  private Optional<Map<String, String>> eventMetadata;

  public DispatchedEvent(String entityId, EventId eventId, T event, Integer swimlane, Long offset, EventContext eventContext, Optional<Map<String, String>> eventMetadata) {
    this.entityId = entityId;
    this.eventId = eventId;
    this.event = event;
    this.swimlane = swimlane;
    this.offset = offset;
    this.eventContext = eventContext;
    this.eventMetadata = eventMetadata;
  }

  @Override
  public String toString() {
    return "DispatchedEvent{" +
            "entityId='" + entityId + '\'' +
            ", eventId=" + eventId +
            ", event=" + event +
            ", swimlane=" + swimlane +
            ", offset=" + offset +
            ", eventContext=" + eventContext +
            '}';
  }

  @Override
  public EventId getEventId() {
    return eventId;
  }

  @Override
  public Class<T> getEventType() {
    return (Class<T>) event.getClass();
  }

  @Override
  public T getEvent() {
    return event;
  }

  @Override
  public String getEntityId() {
    return entityId;
  }

  @Override
  public Integer getSwimlane() {
    return swimlane;
  }

  @Override
  public Long getOffset() {
    return offset;
  }

  @Override
  public EventContext getEventContext() {
    return eventContext;
  }

  @Override
  public Optional<Map<String, String>> getEventMetadata() {
    return eventMetadata;
  }
}
