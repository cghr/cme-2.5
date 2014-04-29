/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ class SplitMenuColors
/*  4:   */   extends EscherAtom
/*  5:   */ {
/*  6:   */   private byte[] data;
/*  7:   */   
/*  8:   */   public SplitMenuColors(EscherRecordData erd)
/*  9:   */   {
/* 10:39 */     super(erd);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public SplitMenuColors()
/* 14:   */   {
/* 15:47 */     super(EscherRecordType.SPLIT_MENU_COLORS);
/* 16:48 */     setVersion(0);
/* 17:49 */     setInstance(4);
/* 18:   */     
/* 19:51 */     this.data = 
/* 20:52 */       new byte[] { 13, 0, 0, 8, 
/* 21:53 */       12, 0, 0, 8, 
/* 22:54 */       23, 0, 0, 8, 
/* 23:55 */       -9, 0, 0, 16 };
/* 24:   */   }
/* 25:   */   
/* 26:   */   byte[] getData()
/* 27:   */   {
/* 28:65 */     return setHeaderData(this.data);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.SplitMenuColors
 * JD-Core Version:    0.7.0.1
 */