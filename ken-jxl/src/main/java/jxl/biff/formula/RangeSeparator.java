/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ 
/*  5:   */ class RangeSeparator
/*  6:   */   extends BinaryOperator
/*  7:   */   implements ParsedThing
/*  8:   */ {
/*  9:   */   public String getSymbol()
/* 10:   */   {
/* 11:40 */     return ":";
/* 12:   */   }
/* 13:   */   
/* 14:   */   Token getToken()
/* 15:   */   {
/* 16:50 */     return Token.RANGE;
/* 17:   */   }
/* 18:   */   
/* 19:   */   int getPrecedence()
/* 20:   */   {
/* 21:61 */     return 1;
/* 22:   */   }
/* 23:   */   
/* 24:   */   byte[] getBytes()
/* 25:   */   {
/* 26:72 */     setVolatile();
/* 27:73 */     setOperandAlternateCode();
/* 28:   */     
/* 29:75 */     byte[] funcBytes = super.getBytes();
/* 30:   */     
/* 31:77 */     byte[] bytes = new byte[funcBytes.length + 3];
/* 32:78 */     System.arraycopy(funcBytes, 0, bytes, 3, funcBytes.length);
/* 33:   */     
/* 34:   */ 
/* 35:81 */     bytes[0] = Token.MEM_FUNC.getCode();
/* 36:82 */     IntegerHelper.getTwoBytes(funcBytes.length, bytes, 1);
/* 37:   */     
/* 38:84 */     return bytes;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.RangeSeparator
 * JD-Core Version:    0.7.0.1
 */