/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.CellType;
/*  4:   */ import jxl.LabelCell;
/*  5:   */ import jxl.WorkbookSettings;
/*  6:   */ import jxl.biff.FormattingRecords;
/*  7:   */ import jxl.biff.IntegerHelper;
/*  8:   */ import jxl.biff.StringHelper;
/*  9:   */ 
/* 10:   */ class RStringRecord
/* 11:   */   extends CellValue
/* 12:   */   implements LabelCell
/* 13:   */ {
/* 14:   */   private int length;
/* 15:   */   private String string;
/* 16:47 */   public static Biff7 biff7 = new Biff7(null);
/* 17:   */   
/* 18:   */   public RStringRecord(Record t, FormattingRecords fr, SheetImpl si, WorkbookSettings ws, Biff7 dummy)
/* 19:   */   {
/* 20:61 */     super(t, fr, si);
/* 21:62 */     byte[] data = getRecord().getData();
/* 22:63 */     this.length = IntegerHelper.getInt(data[6], data[7]);
/* 23:   */     
/* 24:65 */     this.string = StringHelper.getString(data, this.length, 8, ws);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getString()
/* 28:   */   {
/* 29:75 */     return this.string;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getContents()
/* 33:   */   {
/* 34:85 */     return this.string;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public CellType getType()
/* 38:   */   {
/* 39:95 */     return CellType.LABEL;
/* 40:   */   }
/* 41:   */   
/* 42:   */   private static class Biff7 {}
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.RStringRecord
 * JD-Core Version:    0.7.0.1
 */