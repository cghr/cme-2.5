/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ 
/*  6:   */ public class GuttersRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:   */   private int width;
/* 10:   */   private int height;
/* 11:   */   private int rowOutlineLevel;
/* 12:   */   private int columnOutlineLevel;
/* 13:   */   
/* 14:   */   public GuttersRecord(Record r)
/* 15:   */   {
/* 16:42 */     super(r);
/* 17:   */     
/* 18:44 */     byte[] data = getRecord().getData();
/* 19:45 */     this.width = IntegerHelper.getInt(data[0], data[1]);
/* 20:46 */     this.height = IntegerHelper.getInt(data[2], data[3]);
/* 21:47 */     this.rowOutlineLevel = IntegerHelper.getInt(data[4], data[5]);
/* 22:48 */     this.columnOutlineLevel = IntegerHelper.getInt(data[6], data[7]);
/* 23:   */   }
/* 24:   */   
/* 25:   */   int getRowOutlineLevel()
/* 26:   */   {
/* 27:53 */     return this.rowOutlineLevel;
/* 28:   */   }
/* 29:   */   
/* 30:   */   int getColumnOutlineLevel()
/* 31:   */   {
/* 32:58 */     return this.columnOutlineLevel;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.GuttersRecord
 * JD-Core Version:    0.7.0.1
 */