/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import java.text.DateFormat;
/*  4:   */ import java.util.Date;
/*  5:   */ import jxl.DateFormulaCell;
/*  6:   */ import jxl.biff.FormulaData;
/*  7:   */ 
/*  8:   */ class ReadDateFormulaRecord
/*  9:   */   extends ReadFormulaRecord
/* 10:   */   implements DateFormulaCell
/* 11:   */ {
/* 12:   */   public ReadDateFormulaRecord(FormulaData f)
/* 13:   */   {
/* 14:41 */     super(f);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Date getDate()
/* 18:   */   {
/* 19:51 */     return ((DateFormulaCell)getReadFormula()).getDate();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public boolean isTime()
/* 23:   */   {
/* 24:62 */     return ((DateFormulaCell)getReadFormula()).isTime();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public DateFormat getDateFormat()
/* 28:   */   {
/* 29:74 */     return ((DateFormulaCell)getReadFormula()).getDateFormat();
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadDateFormulaRecord
 * JD-Core Version:    0.7.0.1
 */