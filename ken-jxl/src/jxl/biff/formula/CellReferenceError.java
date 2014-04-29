/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class CellReferenceError
/*  6:   */   extends Operand
/*  7:   */   implements ParsedThing
/*  8:   */ {
/*  9:32 */   private static Logger logger = Logger.getLogger(CellReferenceError.class);
/* 10:   */   
/* 11:   */   public int read(byte[] data, int pos)
/* 12:   */   {
/* 13:52 */     return 4;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void getString(StringBuffer buf)
/* 17:   */   {
/* 18:62 */     buf.append(FormulaErrorCode.REF.getDescription());
/* 19:   */   }
/* 20:   */   
/* 21:   */   byte[] getBytes()
/* 22:   */   {
/* 23:72 */     byte[] data = new byte[5];
/* 24:73 */     data[0] = Token.REFERR.getCode();
/* 25:   */     
/* 26:   */ 
/* 27:   */ 
/* 28:77 */     return data;
/* 29:   */   }
/* 30:   */   
/* 31:   */   void handleImportedCellReferences() {}
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.CellReferenceError
 * JD-Core Version:    0.7.0.1
 */