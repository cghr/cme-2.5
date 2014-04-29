/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class Name
/*  4:   */   extends Operand
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public int read(byte[] data, int pos)
/*  8:   */   {
/*  9:44 */     return 6;
/* 10:   */   }
/* 11:   */   
/* 12:   */   byte[] getBytes()
/* 13:   */   {
/* 14:54 */     byte[] data = new byte[6];
/* 15:   */     
/* 16:56 */     return data;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void getString(StringBuffer buf)
/* 20:   */   {
/* 21:67 */     buf.append("[Name record not implemented]");
/* 22:   */   }
/* 23:   */   
/* 24:   */   void handleImportedCellReferences()
/* 25:   */   {
/* 26:77 */     setInvalid();
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Name
 * JD-Core Version:    0.7.0.1
 */