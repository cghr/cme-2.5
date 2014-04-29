package org.springframework.jmx.export.notification;

import org.springframework.beans.factory.Aware;

public abstract interface NotificationPublisherAware
  extends Aware
{
  public abstract void setNotificationPublisher(NotificationPublisher paramNotificationPublisher);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.notification.NotificationPublisherAware
 * JD-Core Version:    0.7.0.1
 */