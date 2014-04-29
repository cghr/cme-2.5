package org.springframework.jmx.export.notification;

import javax.management.Notification;

public abstract interface NotificationPublisher
{
  public abstract void sendNotification(Notification paramNotification)
    throws UnableToSendNotificationException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.notification.NotificationPublisher
 * JD-Core Version:    0.7.0.1
 */