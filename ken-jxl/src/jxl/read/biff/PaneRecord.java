/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class PaneRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:35 */   private static Logger logger = Logger.getLogger(PaneRecord.class);
/* 11:   */   private int rowsVisible;
/* 12:   */   private int columnsVisible;
/* 13:   */   
/* 14:   */   public PaneRecord(Record t)
/* 15:   */   {
/* 16:53 */     super(t);
/* 17:54 */     byte[] data = t.getData();
/* 18:   */     
/* 19:56 */     this.columnsVisible = IntegerHelper.getInt(data[0], data[1]);
/* 20:57 */     this.rowsVisible = IntegerHelper.getInt(data[2], data[3]);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public final int getRowsVisible()
/* 24:   */   {
/* 25:67 */     return this.rowsVisible;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public final int getColumnsVisible()
/* 29:   */   {
/* 30:77 */     return this.columnsVisible;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.PaneRecord
 * JD-Core Version:    0.7.0.1
 */