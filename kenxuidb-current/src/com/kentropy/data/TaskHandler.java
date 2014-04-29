package com.kentropy.data;

import com.kentropy.db.XTaskModel;

public abstract interface TaskHandler
{
  public abstract int getTaskChildCount(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6);
  
  public abstract void getTasks(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract void getTask(String paramString1, String paramString2, XTaskModel paramXTaskModel, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract void saveTask(XTaskModel paramXTaskModel)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.TaskHandler
 * JD-Core Version:    0.7.0.1
 */