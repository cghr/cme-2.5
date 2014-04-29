package com.kentropy.process;

public abstract interface StateMachine
{
  public abstract String getCurrentState();
  
  public abstract String transition();
  
  public abstract void onTaskStatusUpdate(String paramString1, String paramString2);
  
  public abstract void deserialize(String paramString);
  
  public abstract void setPid(String paramString);
  
  public abstract void init();
  
  public abstract void runAgents(Process paramProcess);
  
  public abstract void rollback();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.StateMachine
 * JD-Core Version:    0.7.0.1
 */