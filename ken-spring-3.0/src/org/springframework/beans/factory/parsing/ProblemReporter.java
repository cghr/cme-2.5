package org.springframework.beans.factory.parsing;

public abstract interface ProblemReporter
{
  public abstract void fatal(Problem paramProblem);
  
  public abstract void error(Problem paramProblem);
  
  public abstract void warning(Problem paramProblem);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ProblemReporter
 * JD-Core Version:    0.7.0.1
 */