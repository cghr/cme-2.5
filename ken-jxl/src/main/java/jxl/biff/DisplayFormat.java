package jxl.biff;

public abstract interface DisplayFormat
{
  public abstract int getFormatIndex();
  
  public abstract boolean isInitialized();
  
  public abstract void initialize(int paramInt);
  
  public abstract boolean isBuiltIn();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DisplayFormat
 * JD-Core Version:    0.7.0.1
 */