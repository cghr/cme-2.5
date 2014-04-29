/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ 
/*  5:   */ class MemArea
/*  6:   */   extends SubExpression
/*  7:   */ {
/*  8:   */   public void getString(StringBuffer buf)
/*  9:   */   {
/* 10:38 */     ParseItem[] subExpression = getSubExpression();
/* 11:40 */     if (subExpression.length == 1)
/* 12:   */     {
/* 13:42 */       subExpression[0].getString(buf);
/* 14:   */     }
/* 15:44 */     else if (subExpression.length == 2)
/* 16:   */     {
/* 17:46 */       subExpression[1].getString(buf);
/* 18:47 */       buf.append(':');
/* 19:48 */       subExpression[0].getString(buf);
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int read(byte[] data, int pos)
/* 24:   */   {
/* 25:62 */     setLength(IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]));
/* 26:63 */     return 6;
/* 27:   */   }
/* 28:   */   
/* 29:   */   void handleImportedCellReferences() {}
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.MemArea
 * JD-Core Version:    0.7.0.1
 */