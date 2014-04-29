package org.springframework.scheduling;

import java.util.Date;

public abstract interface Trigger
{
  public abstract Date nextExecutionTime(TriggerContext paramTriggerContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.Trigger
 * JD-Core Version:    0.7.0.1
 */