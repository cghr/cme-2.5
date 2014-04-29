package com.kentropy.data;

import net.xoetrope.xui.data.XModel;

public abstract interface DataHandler
{
  public abstract XModel getData(String paramString1, String paramString2, XModel paramXModel, String paramString3)
    throws Exception;
  
  public abstract XModel getChildren(String paramString1, String paramString2, XModel paramXModel, String paramString3, String paramString4)
    throws Exception;
  
  public abstract void saveData(String paramString1, String paramString2, XModel paramXModel1, XModel paramXModel2)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.DataHandler
 * JD-Core Version:    0.7.0.1
 */