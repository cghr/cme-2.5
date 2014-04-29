/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class MissingArg
/*  4:   */   extends Operand
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public int read(byte[] data, int pos)
/*  8:   */   {
/*  9:44 */     return 0;
/* 10:   */   }
/* 11:   */   
/* 12:   */   byte[] getBytes()
/* 13:   */   {
/* 14:54 */     byte[] data = new byte[1];
/* 15:55 */     data[0] = Token.MISSING_ARG.getCode();
/* 16:   */     
/* 17:57 */     return data;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void getString(StringBuffer buf) {}
/* 21:   */   
/* 22:   */   void handleImportedCellReferences() {}
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.MissingArg
 * JD-Core Version:    0.7.0.1
 */