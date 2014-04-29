package jxl.biff.formula;

import jxl.read.biff.BOFRecord;

public abstract interface ExternalSheet
{
  public abstract String getExternalSheetName(int paramInt);
  
  public abstract int getExternalSheetIndex(String paramString);
  
  public abstract int getExternalSheetIndex(int paramInt);
  
  public abstract int getLastExternalSheetIndex(String paramString);
  
  public abstract int getLastExternalSheetIndex(int paramInt);
  
  public abstract BOFRecord getWorkbookBof();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.ExternalSheet
 * JD-Core Version:    0.7.0.1
 */