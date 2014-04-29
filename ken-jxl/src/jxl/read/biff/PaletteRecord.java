/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ 
/*  5:   */ public class PaletteRecord
/*  6:   */   extends RecordData
/*  7:   */ {
/*  8:   */   PaletteRecord(Record t)
/*  9:   */   {
/* 10:36 */     super(t);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public byte[] getData()
/* 14:   */   {
/* 15:46 */     return getRecord().getData();
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.PaletteRecord
 * JD-Core Version:    0.7.0.1
 */