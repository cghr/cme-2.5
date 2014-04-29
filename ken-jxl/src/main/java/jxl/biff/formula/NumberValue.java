/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ abstract class NumberValue
/*  4:   */   extends Operand
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public abstract double getValue();
/*  8:   */   
/*  9:   */   public void getString(StringBuffer buf)
/* 10:   */   {
/* 11:35 */     buf.append(Double.toString(getValue()));
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.NumberValue
 * JD-Core Version:    0.7.0.1
 */