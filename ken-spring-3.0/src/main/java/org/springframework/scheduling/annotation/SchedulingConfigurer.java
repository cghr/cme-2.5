package org.springframework.scheduling.annotation;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public abstract interface SchedulingConfigurer
{
  public abstract void configureTasks(ScheduledTaskRegistrar paramScheduledTaskRegistrar);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.SchedulingConfigurer
 * JD-Core Version:    0.7.0.1
 */