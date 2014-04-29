package com.kentropy.workflow;

import java.util.Map;

public abstract interface WorkflowEventHandler
{
  public abstract void onEnter(Map paramMap);
  
  public abstract void onExit(Map paramMap);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.workflow.WorkflowEventHandler
 * JD-Core Version:    0.7.0.1
 */