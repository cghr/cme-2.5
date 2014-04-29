package jxl;

import java.text.NumberFormat;

public abstract interface NumberCell
  extends Cell
{
  public abstract double getValue();
  
  public abstract NumberFormat getNumberFormat();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.NumberCell
 * JD-Core Version:    0.7.0.1
 */