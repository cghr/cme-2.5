package org.springframework.scripting;

import java.io.IOException;

public abstract interface ScriptFactory
{
  public abstract String getScriptSourceLocator();
  
  public abstract Class[] getScriptInterfaces();
  
  public abstract boolean requiresConfigInterface();
  
  public abstract Object getScriptedObject(ScriptSource paramScriptSource, Class[] paramArrayOfClass)
    throws IOException, ScriptCompilationException;
  
  public abstract Class getScriptedObjectType(ScriptSource paramScriptSource)
    throws IOException, ScriptCompilationException;
  
  public abstract boolean requiresScriptedObjectRefresh(ScriptSource paramScriptSource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.ScriptFactory
 * JD-Core Version:    0.7.0.1
 */