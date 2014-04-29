/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import jxl.WorkbookSettings;
/*  4:   */ import jxl.common.Logger;
/*  5:   */ 
/*  6:   */ class StringFunction
/*  7:   */   extends StringParseItem
/*  8:   */ {
/*  9:35 */   private static Logger logger = Logger.getLogger(StringFunction.class);
/* 10:   */   private Function function;
/* 11:   */   private String functionString;
/* 12:   */   
/* 13:   */   StringFunction(String s)
/* 14:   */   {
/* 15:54 */     this.functionString = s.substring(0, s.length() - 1);
/* 16:   */   }
/* 17:   */   
/* 18:   */   Function getFunction(WorkbookSettings ws)
/* 19:   */   {
/* 20:65 */     if (this.function == null) {
/* 21:67 */       this.function = Function.getFunction(this.functionString, ws);
/* 22:   */     }
/* 23:69 */     return this.function;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.StringFunction
 * JD-Core Version:    0.7.0.1
 */