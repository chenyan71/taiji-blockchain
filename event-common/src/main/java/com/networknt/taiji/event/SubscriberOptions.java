package com.networknt.taiji.event;

public class SubscriberOptions {

  private SubscriberDurability durability;
  private SubscriberInitialPosition readFrom;
  private boolean progressNotifications;

  public static SubscriberOptions DEFAULTS = new SubscriberOptions(SubscriberDurability.DURABLE, SubscriberInitialPosition.BEGINNING, false);

  public SubscriberOptions() {
  }

  public SubscriberOptions(SubscriberDurability durability, SubscriberInitialPosition readFrom, boolean progressNotifications) {
    this.durability = durability;
    this.readFrom = readFrom;
    this.progressNotifications = progressNotifications;
  }

  public SubscriberDurability getDurability() {
    return durability;
  }

  public SubscriberInitialPosition getReadFrom() {
    return readFrom;
  }

  public boolean isProgressNotifications() {
    return progressNotifications;
  }
}


