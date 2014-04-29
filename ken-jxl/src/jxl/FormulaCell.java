package jxl;

import jxl.biff.formula.FormulaException;

public abstract interface FormulaCell
  extends Cell
{
  public abstract String getFormula()
    throws FormulaException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.FormulaCell
 * JD-Core Version:    0.7.0.1
 */