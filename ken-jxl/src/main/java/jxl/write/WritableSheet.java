package jxl.write;

import jxl.CellView;
import jxl.Range;
import jxl.Sheet;
import jxl.format.CellFormat;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.write.biff.RowsExceededException;

public abstract interface WritableSheet
  extends Sheet
{
  public abstract void addCell(WritableCell paramWritableCell)
    throws WriteException, RowsExceededException;
  
  public abstract void setName(String paramString);
  
  /**
   * @deprecated
   */
  public abstract void setHidden(boolean paramBoolean);
  
  /**
   * @deprecated
   */
  public abstract void setProtected(boolean paramBoolean);
  
  public abstract void setColumnView(int paramInt1, int paramInt2);
  
  /**
   * @deprecated
   */
  public abstract void setColumnView(int paramInt1, int paramInt2, CellFormat paramCellFormat);
  
  public abstract void setColumnView(int paramInt, CellView paramCellView);
  
  public abstract void setRowView(int paramInt1, int paramInt2)
    throws RowsExceededException;
  
  public abstract void setRowView(int paramInt, boolean paramBoolean)
    throws RowsExceededException;
  
  public abstract void setRowView(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RowsExceededException;
  
  public abstract void setRowView(int paramInt, CellView paramCellView)
    throws RowsExceededException;
  
  public abstract WritableCell getWritableCell(int paramInt1, int paramInt2);
  
  public abstract WritableCell getWritableCell(String paramString);
  
  public abstract WritableHyperlink[] getWritableHyperlinks();
  
  public abstract void insertRow(int paramInt);
  
  public abstract void insertColumn(int paramInt);
  
  public abstract void removeColumn(int paramInt);
  
  public abstract void removeRow(int paramInt);
  
  public abstract Range mergeCells(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws WriteException, RowsExceededException;
  
  public abstract void setRowGroup(int paramInt1, int paramInt2, boolean paramBoolean)
    throws WriteException, RowsExceededException;
  
  public abstract void unsetRowGroup(int paramInt1, int paramInt2)
    throws WriteException, RowsExceededException;
  
  public abstract void setColumnGroup(int paramInt1, int paramInt2, boolean paramBoolean)
    throws WriteException, RowsExceededException;
  
  public abstract void unsetColumnGroup(int paramInt1, int paramInt2)
    throws WriteException, RowsExceededException;
  
  public abstract void unmergeCells(Range paramRange);
  
  public abstract void addHyperlink(WritableHyperlink paramWritableHyperlink)
    throws WriteException, RowsExceededException;
  
  public abstract void removeHyperlink(WritableHyperlink paramWritableHyperlink);
  
  public abstract void removeHyperlink(WritableHyperlink paramWritableHyperlink, boolean paramBoolean);
  
  /**
   * @deprecated
   */
  public abstract void setHeader(String paramString1, String paramString2, String paramString3);
  
  /**
   * @deprecated
   */
  public abstract void setFooter(String paramString1, String paramString2, String paramString3);
  
  public abstract void setPageSetup(PageOrientation paramPageOrientation);
  
  public abstract void setPageSetup(PageOrientation paramPageOrientation, double paramDouble1, double paramDouble2);
  
  public abstract void setPageSetup(PageOrientation paramPageOrientation, PaperSize paramPaperSize, double paramDouble1, double paramDouble2);
  
  public abstract void addRowPageBreak(int paramInt);
  
  public abstract void addColumnPageBreak(int paramInt);
  
  public abstract void addImage(WritableImage paramWritableImage);
  
  public abstract int getNumberOfImages();
  
  public abstract WritableImage getImage(int paramInt);
  
  public abstract void removeImage(WritableImage paramWritableImage);
  
  public abstract void applySharedDataValidation(WritableCell paramWritableCell, int paramInt1, int paramInt2)
    throws WriteException;
  
  public abstract void removeSharedDataValidation(WritableCell paramWritableCell)
    throws WriteException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableSheet
 * JD-Core Version:    0.7.0.1
 */