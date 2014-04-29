package org.springframework.scheduling;

import java.util.Date;

public abstract interface TriggerContext
{
  public abstract Date lastScheduledExecutionTime();
  
  public abstract Date lastActualExecutionTime();
  
  public abstract Date lastCompletionTime();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.TriggerContext
 * JD-Core Version:    0.7.0.1
 */