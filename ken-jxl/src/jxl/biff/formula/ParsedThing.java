package jxl.biff.formula;

abstract interface ParsedThing
{
  public abstract int read(byte[] paramArrayOfByte, int paramInt)
    throws FormulaException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.ParsedThing
 * JD-Core Version:    0.7.0.1
 */