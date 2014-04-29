/*  1:   */ package jxl.write;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import jxl.Range;
/*  6:   */ import jxl.Sheet;
/*  7:   */ import jxl.Workbook;
/*  8:   */ import jxl.format.Colour;
/*  9:   */ import jxl.format.UnderlineStyle;
/* 10:   */ 
/* 11:   */ public abstract class WritableWorkbook
/* 12:   */ {
/* 13:41 */   public static final WritableFont ARIAL_10_PT = new WritableFont(WritableFont.ARIAL);
/* 14:47 */   public static final WritableFont HYPERLINK_FONT = new WritableFont(WritableFont.ARIAL, 
/* 15:48 */     10, 
/* 16:49 */     WritableFont.NO_BOLD, 
/* 17:50 */     false, 
/* 18:51 */     UnderlineStyle.SINGLE, 
/* 19:52 */     Colour.BLUE);
/* 20:58 */   public static final WritableCellFormat NORMAL_STYLE = new WritableCellFormat(ARIAL_10_PT, NumberFormats.DEFAULT);
/* 21:64 */   public static final WritableCellFormat HYPERLINK_STYLE = new WritableCellFormat(HYPERLINK_FONT);
/* 22:70 */   public static final WritableCellFormat HIDDEN_STYLE = new WritableCellFormat(new DateFormat(";;;"));
/* 23:   */   
/* 24:   */   public abstract WritableSheet[] getSheets();
/* 25:   */   
/* 26:   */   public abstract String[] getSheetNames();
/* 27:   */   
/* 28:   */   public abstract WritableSheet getSheet(int paramInt)
/* 29:   */     throws IndexOutOfBoundsException;
/* 30:   */   
/* 31:   */   public abstract WritableSheet getSheet(String paramString);
/* 32:   */   
/* 33:   */   public abstract WritableCell getWritableCell(String paramString);
/* 34:   */   
/* 35:   */   public abstract int getNumberOfSheets();
/* 36:   */   
/* 37:   */   public abstract void close()
/* 38:   */     throws IOException, WriteException;
/* 39:   */   
/* 40:   */   public abstract WritableSheet createSheet(String paramString, int paramInt);
/* 41:   */   
/* 42:   */   public abstract WritableSheet importSheet(String paramString, int paramInt, Sheet paramSheet);
/* 43:   */   
/* 44:   */   public abstract void copySheet(int paramInt1, String paramString, int paramInt2);
/* 45:   */   
/* 46:   */   public abstract void copySheet(String paramString1, String paramString2, int paramInt);
/* 47:   */   
/* 48:   */   public abstract void removeSheet(int paramInt);
/* 49:   */   
/* 50:   */   public abstract WritableSheet moveSheet(int paramInt1, int paramInt2);
/* 51:   */   
/* 52:   */   public abstract void write()
/* 53:   */     throws IOException;
/* 54:   */   
/* 55:   */   public abstract void setProtected(boolean paramBoolean);
/* 56:   */   
/* 57:   */   public abstract void setColourRGB(Colour paramColour, int paramInt1, int paramInt2, int paramInt3);
/* 58:   */   
/* 59:   */   /**
/* 60:   */    * @deprecated
/* 61:   */    */
/* 62:   */   public void copy(Workbook w) {}
/* 63:   */   
/* 64:   */   public abstract WritableCell findCellByName(String paramString);
/* 65:   */   
/* 66:   */   public abstract Range[] findByName(String paramString);
/* 67:   */   
/* 68:   */   public abstract String[] getRangeNames();
/* 69:   */   
/* 70:   */   public abstract void removeRangeName(String paramString);
/* 71:   */   
/* 72:   */   public abstract void addNameArea(String paramString, WritableSheet paramWritableSheet, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/* 73:   */   
/* 74:   */   public abstract void setOutputFile(File paramFile)
/* 75:   */     throws IOException;
/* 76:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableWorkbook
 * JD-Core Version:    0.7.0.1
 */