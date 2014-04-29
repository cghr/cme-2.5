package org.springframework.scripting;

import java.io.IOException;

public abstract interface ScriptSource
{
  public abstract String getScriptAsString()
    throws IOException;
  
  public abstract boolean isModified();
  
  public abstract String suggestedClassName();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.ScriptSource
 * JD-Core Version:    0.7.0.1
 */