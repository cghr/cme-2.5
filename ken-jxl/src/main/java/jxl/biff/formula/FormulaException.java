/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.JXLException;
/*   4:    */ 
/*   5:    */ public class FormulaException
/*   6:    */   extends JXLException
/*   7:    */ {
/*   8:    */   private static class FormulaMessage
/*   9:    */   {
/*  10:    */     private String message;
/*  11:    */     
/*  12:    */     FormulaMessage(String m)
/*  13:    */     {
/*  14: 46 */       this.message = m;
/*  15:    */     }
/*  16:    */     
/*  17:    */     public String getMessage()
/*  18:    */     {
/*  19: 56 */       return this.message;
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23: 63 */   static final FormulaMessage UNRECOGNIZED_TOKEN = new FormulaMessage("Unrecognized token");
/*  24: 68 */   static final FormulaMessage UNRECOGNIZED_FUNCTION = new FormulaMessage("Unrecognized function");
/*  25: 73 */   public static final FormulaMessage BIFF8_SUPPORTED = new FormulaMessage("Only biff8 formulas are supported");
/*  26: 78 */   static final FormulaMessage LEXICAL_ERROR = new FormulaMessage("Lexical error:  ");
/*  27: 83 */   static final FormulaMessage INCORRECT_ARGUMENTS = new FormulaMessage("Incorrect arguments supplied to function");
/*  28: 88 */   static final FormulaMessage SHEET_REF_NOT_FOUND = new FormulaMessage("Could not find sheet");
/*  29: 93 */   static final FormulaMessage CELL_NAME_NOT_FOUND = new FormulaMessage("Could not find named cell");
/*  30:    */   
/*  31:    */   public FormulaException(FormulaMessage m)
/*  32:    */   {
/*  33:103 */     super(m.message);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public FormulaException(FormulaMessage m, int val)
/*  37:    */   {
/*  38:114 */     super(m.message + " " + val);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public FormulaException(FormulaMessage m, String val)
/*  42:    */   {
/*  43:125 */     super(m.message + " " + val);
/*  44:    */   }
/*  45:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.FormulaException
 * JD-Core Version:    0.7.0.1
 */