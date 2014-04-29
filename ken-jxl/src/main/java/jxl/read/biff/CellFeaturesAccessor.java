package jxl.read.biff;

import jxl.CellFeatures;

abstract interface CellFeaturesAccessor
{
  public abstract void setCellFeatures(CellFeatures paramCellFeatures);
  
  public abstract CellFeatures getCellFeatures();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CellFeaturesAccessor
 * JD-Core Version:    0.7.0.1
 */