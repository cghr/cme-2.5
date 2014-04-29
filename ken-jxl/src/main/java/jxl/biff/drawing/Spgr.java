/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ class Spgr
/*  4:   */   extends EscherAtom
/*  5:   */ {
/*  6:   */   private byte[] data;
/*  7:   */   
/*  8:   */   public Spgr(EscherRecordData erd)
/*  9:   */   {
/* 10:39 */     super(erd);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public Spgr()
/* 14:   */   {
/* 15:47 */     super(EscherRecordType.SPGR);
/* 16:48 */     setVersion(1);
/* 17:49 */     this.data = new byte[16];
/* 18:   */   }
/* 19:   */   
/* 20:   */   byte[] getData()
/* 21:   */   {
/* 22:59 */     return setHeaderData(this.data);
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Spgr
 * JD-Core Version:    0.7.0.1
 */