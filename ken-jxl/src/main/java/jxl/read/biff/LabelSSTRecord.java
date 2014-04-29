/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.CellType;
/*  4:   */ import jxl.LabelCell;
/*  5:   */ import jxl.biff.FormattingRecords;
/*  6:   */ import jxl.biff.IntegerHelper;
/*  7:   */ 
/*  8:   */ class LabelSSTRecord
/*  9:   */   extends CellValue
/* 10:   */   implements LabelCell
/* 11:   */ {
/* 12:   */   private int index;
/* 13:   */   private String string;
/* 14:   */   
/* 15:   */   public LabelSSTRecord(Record t, SSTRecord stringTable, FormattingRecords fr, SheetImpl si)
/* 16:   */   {
/* 17:53 */     super(t, fr, si);
/* 18:54 */     byte[] data = getRecord().getData();
/* 19:55 */     this.index = IntegerHelper.getInt(data[6], data[7], data[8], data[9]);
/* 20:56 */     this.string = stringTable.getString(this.index);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getString()
/* 24:   */   {
/* 25:66 */     return this.string;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getContents()
/* 29:   */   {
/* 30:76 */     return this.string;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public CellType getType()
/* 34:   */   {
/* 35:86 */     return CellType.LABEL;
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.LabelSSTRecord
 * JD-Core Version:    0.7.0.1
 */