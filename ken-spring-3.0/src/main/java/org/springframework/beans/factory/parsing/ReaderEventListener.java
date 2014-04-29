package org.springframework.beans.factory.parsing;

import java.util.EventListener;

public abstract interface ReaderEventListener
  extends EventListener
{
  public abstract void defaultsRegistered(DefaultsDefinition paramDefaultsDefinition);
  
  public abstract void componentRegistered(ComponentDefinition paramComponentDefinition);
  
  public abstract void aliasRegistered(AliasDefinition paramAliasDefinition);
  
  public abstract void importProcessed(ImportDefinition paramImportDefinition);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ReaderEventListener
 * JD-Core Version:    0.7.0.1
 */