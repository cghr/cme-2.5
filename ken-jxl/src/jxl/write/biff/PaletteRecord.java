/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class PaletteRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public PaletteRecord(jxl.read.biff.PaletteRecord p)
/* 12:   */   {
/* 13:42 */     super(Type.PALETTE);
/* 14:   */     
/* 15:44 */     this.data = p.getData();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte[] getData()
/* 19:   */   {
/* 20:54 */     return this.data;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PaletteRecord
 * JD-Core Version:    0.7.0.1
 */