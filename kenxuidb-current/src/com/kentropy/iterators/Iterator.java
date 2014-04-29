package com.kentropy.iterators;

import net.xoetrope.xui.data.XModel;

public abstract interface Iterator
{
  public abstract boolean first();
  
  public abstract boolean next();
  
  public abstract boolean find(String paramString);
  
  public abstract void setSource(XModel paramXModel);
  
  public abstract void setContext(XModel paramXModel);
  
  public abstract void setNextContextType(String paramString);
  
  public abstract void setAutoUpdate(boolean paramBoolean);
  
  public abstract void setConstraints(String paramString);
  
  public abstract boolean last();
  
  public abstract void init();
  
  public abstract void setFields(String paramString);
  
  public abstract XModel getData();
  
  public abstract XModel getHeaders();
  
  public abstract void setParentContext(XModel paramXModel);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.iterators.Iterator
 * JD-Core Version:    0.7.0.1
 */