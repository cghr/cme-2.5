/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.CellType;
/*  4:   */ import jxl.ErrorCell;
/*  5:   */ import jxl.biff.FormattingRecords;
/*  6:   */ 
/*  7:   */ class ErrorRecord
/*  8:   */   extends CellValue
/*  9:   */   implements ErrorCell
/* 10:   */ {
/* 11:   */   private int errorCode;
/* 12:   */   
/* 13:   */   public ErrorRecord(Record t, FormattingRecords fr, SheetImpl si)
/* 14:   */   {
/* 15:46 */     super(t, fr, si);
/* 16:   */     
/* 17:48 */     byte[] data = getRecord().getData();
/* 18:   */     
/* 19:50 */     this.errorCode = data[6];
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getErrorCode()
/* 23:   */   {
/* 24:62 */     return this.errorCode;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getContents()
/* 28:   */   {
/* 29:72 */     return "ERROR " + this.errorCode;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public CellType getType()
/* 33:   */   {
/* 34:82 */     return CellType.ERROR;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ErrorRecord
 * JD-Core Version:    0.7.0.1
 */