package org.springframework.scheduling.quartz;

import org.quartz.JobDetail;

public abstract interface JobDetailAwareTrigger
{
  public abstract JobDetail getJobDetail();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.JobDetailAwareTrigger
 * JD-Core Version:    0.7.0.1
 */