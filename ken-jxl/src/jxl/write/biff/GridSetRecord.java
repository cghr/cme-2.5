/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class GridSetRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   private boolean gridSet;
/* 11:   */   
/* 12:   */   public GridSetRecord(boolean gs)
/* 13:   */   {
/* 14:47 */     super(Type.GRIDSET);
/* 15:48 */     this.gridSet = gs;
/* 16:   */     
/* 17:50 */     this.data = new byte[2];
/* 18:52 */     if (this.gridSet) {
/* 19:54 */       this.data[0] = 1;
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public byte[] getData()
/* 24:   */   {
/* 25:65 */     return this.data;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.GridSetRecord
 * JD-Core Version:    0.7.0.1
 */