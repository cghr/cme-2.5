package jxl.biff;

import jxl.Cell;
import jxl.biff.formula.FormulaException;

public abstract interface FormulaData
  extends Cell
{
  public abstract byte[] getFormulaData()
    throws FormulaException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.FormulaData
 * JD-Core Version:    0.7.0.1
 */