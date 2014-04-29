/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class StringParseItem
/*  4:   */   extends ParseItem
/*  5:   */ {
/*  6:   */   void getString(StringBuffer buf) {}
/*  7:   */   
/*  8:   */   byte[] getBytes()
/*  9:   */   {
/* 10:52 */     return new byte[0];
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust) {}
/* 14:   */   
/* 15:   */   void columnInserted(int sheetIndex, int col, boolean currentSheet) {}
/* 16:   */   
/* 17:   */   void columnRemoved(int sheetIndex, int col, boolean currentSheet) {}
/* 18:   */   
/* 19:   */   void rowInserted(int sheetIndex, int row, boolean currentSheet) {}
/* 20:   */   
/* 21:   */   void rowRemoved(int sheetIndex, int row, boolean currentSheet) {}
/* 22:   */   
/* 23:   */   void handleImportedCellReferences() {}
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.StringParseItem
 * JD-Core Version:    0.7.0.1
 */