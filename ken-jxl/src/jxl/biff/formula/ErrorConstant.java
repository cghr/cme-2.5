/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class ErrorConstant
/*  4:   */   extends Operand
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   private FormulaErrorCode error;
/*  8:   */   
/*  9:   */   public ErrorConstant() {}
/* 10:   */   
/* 11:   */   public ErrorConstant(String s)
/* 12:   */   {
/* 13:48 */     this.error = FormulaErrorCode.getErrorCode(s);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public int read(byte[] data, int pos)
/* 17:   */   {
/* 18:60 */     int code = data[pos];
/* 19:61 */     this.error = FormulaErrorCode.getErrorCode(code);
/* 20:62 */     return 1;
/* 21:   */   }
/* 22:   */   
/* 23:   */   byte[] getBytes()
/* 24:   */   {
/* 25:72 */     byte[] data = new byte[2];
/* 26:73 */     data[0] = Token.ERR.getCode();
/* 27:74 */     data[1] = ((byte)this.error.getCode());
/* 28:   */     
/* 29:76 */     return data;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void getString(StringBuffer buf)
/* 33:   */   {
/* 34:87 */     buf.append(this.error.getDescription());
/* 35:   */   }
/* 36:   */   
/* 37:   */   void handleImportedCellReferences() {}
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.ErrorConstant
 * JD-Core Version:    0.7.0.1
 */