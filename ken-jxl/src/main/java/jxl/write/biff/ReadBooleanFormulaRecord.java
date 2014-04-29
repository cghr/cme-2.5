/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.BooleanFormulaCell;
/*  4:   */ import jxl.biff.FormulaData;
/*  5:   */ 
/*  6:   */ class ReadBooleanFormulaRecord
/*  7:   */   extends ReadFormulaRecord
/*  8:   */   implements BooleanFormulaCell
/*  9:   */ {
/* 10:   */   public ReadBooleanFormulaRecord(FormulaData f)
/* 11:   */   {
/* 12:38 */     super(f);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public boolean getValue()
/* 16:   */   {
/* 17:48 */     return ((BooleanFormulaCell)getReadFormula()).getValue();
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadBooleanFormulaRecord
 * JD-Core Version:    0.7.0.1
 */