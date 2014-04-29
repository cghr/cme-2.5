package org.springframework.core.type;

public abstract interface ClassMetadata
{
  public abstract String getClassName();
  
  public abstract boolean isInterface();
  
  public abstract boolean isAbstract();
  
  public abstract boolean isConcrete();
  
  public abstract boolean isFinal();
  
  public abstract boolean isIndependent();
  
  public abstract boolean hasEnclosingClass();
  
  public abstract String getEnclosingClassName();
  
  public abstract boolean hasSuperClass();
  
  public abstract String getSuperClassName();
  
  public abstract String[] getInterfaceNames();
  
  public abstract String[] getMemberClassNames();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.ClassMetadata
 * JD-Core Version:    0.7.0.1
 */