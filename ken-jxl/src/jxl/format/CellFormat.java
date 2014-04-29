package jxl.format;

public abstract interface CellFormat
{
  public abstract Format getFormat();
  
  public abstract Font getFont();
  
  public abstract boolean getWrap();
  
  public abstract Alignment getAlignment();
  
  public abstract VerticalAlignment getVerticalAlignment();
  
  public abstract Orientation getOrientation();
  
  public abstract BorderLineStyle getBorder(Border paramBorder);
  
  public abstract BorderLineStyle getBorderLine(Border paramBorder);
  
  public abstract Colour getBorderColour(Border paramBorder);
  
  public abstract boolean hasBorders();
  
  public abstract Colour getBackgroundColour();
  
  public abstract Pattern getPattern();
  
  public abstract int getIndentation();
  
  public abstract boolean isShrinkToFit();
  
  public abstract boolean isLocked();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.CellFormat
 * JD-Core Version:    0.7.0.1
 */