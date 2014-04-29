package jxl;

import java.util.regex.Pattern;
import jxl.format.CellFormat;

public abstract interface Sheet
{
  public abstract Cell getCell(int paramInt1, int paramInt2);
  
  public abstract Cell getCell(String paramString);
  
  public abstract int getRows();
  
  public abstract int getColumns();
  
  public abstract Cell[] getRow(int paramInt);
  
  public abstract Cell[] getColumn(int paramInt);
  
  public abstract String getName();
  
  /**
   * @deprecated
   */
  public abstract boolean isHidden();
  
  /**
   * @deprecated
   */
  public abstract boolean isProtected();
  
  public abstract Cell findCell(String paramString);
  
  public abstract Cell findCell(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  public abstract Cell findCell(Pattern paramPattern, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  public abstract LabelCell findLabelCell(String paramString);
  
  public abstract Hyperlink[] getHyperlinks();
  
  public abstract Range[] getMergedCells();
  
  public abstract SheetSettings getSettings();
  
  /**
   * @deprecated
   */
  public abstract CellFormat getColumnFormat(int paramInt);
  
  /**
   * @deprecated
   */
  public abstract int getColumnWidth(int paramInt);
  
  public abstract CellView getColumnView(int paramInt);
  
  /**
   * @deprecated
   */
  public abstract int getRowHeight(int paramInt);
  
  public abstract CellView getRowView(int paramInt);
  
  public abstract int getNumberOfImages();
  
  public abstract Image getDrawing(int paramInt);
  
  public abstract int[] getRowPageBreaks();
  
  public abstract int[] getColumnPageBreaks();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.Sheet
 * JD-Core Version:    0.7.0.1
 */