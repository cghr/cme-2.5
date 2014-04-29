package org.springframework.beans.factory.parsing;

public class EmptyReaderEventListener
  implements ReaderEventListener
{
  public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {}
  
  public void componentRegistered(ComponentDefinition componentDefinition) {}
  
  public void aliasRegistered(AliasDefinition aliasDefinition) {}
  
  public void importProcessed(ImportDefinition importDefinition) {}
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.EmptyReaderEventListener
 * JD-Core Version:    0.7.0.1
 */