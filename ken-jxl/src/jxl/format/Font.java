package jxl.format;

public abstract interface Font
{
  public abstract String getName();
  
  public abstract int getPointSize();
  
  public abstract int getBoldWeight();
  
  public abstract boolean isItalic();
  
  public abstract boolean isStruckout();
  
  public abstract UnderlineStyle getUnderlineStyle();
  
  public abstract Colour getColour();
  
  public abstract ScriptStyle getScriptStyle();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Font
 * JD-Core Version:    0.7.0.1
 */