package com.kentropy.agents;

import com.kentropy.process.Process;
import com.kentropy.transformation.source.TransformationSource;

public abstract interface AgentTransformationSource
  extends TransformationSource
{
  public abstract void setProcess(Process paramProcess);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.AgentTransformationSource
 * JD-Core Version:    0.7.0.1
 */