package jxl.biff;

import jxl.Sheet;

public abstract interface WorkbookMethods
{
  public abstract Sheet getReadSheet(int paramInt);
  
  public abstract String getName(int paramInt)
    throws NameRangeException;
  
  public abstract int getNameIndex(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.WorkbookMethods
 * JD-Core Version:    0.7.0.1
 */