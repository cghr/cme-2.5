package org.springframework.scheduling.quartz;

import org.quartz.SchedulerContext;
import org.springframework.beans.factory.Aware;

public abstract interface SchedulerContextAware
  extends Aware
{
  public abstract void setSchedulerContext(SchedulerContext paramSchedulerContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SchedulerContextAware
 * JD-Core Version:    0.7.0.1
 */