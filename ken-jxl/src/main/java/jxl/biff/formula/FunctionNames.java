/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.ResourceBundle;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ public class FunctionNames
/*   9:    */ {
/*  10: 38 */   private static Logger logger = Logger.getLogger(FunctionNames.class);
/*  11:    */   private HashMap names;
/*  12:    */   private HashMap functions;
/*  13:    */   
/*  14:    */   public FunctionNames(Locale l)
/*  15:    */   {
/*  16: 59 */     ResourceBundle rb = ResourceBundle.getBundle("functions", l);
/*  17: 60 */     Function[] allfunctions = Function.getFunctions();
/*  18: 61 */     this.names = new HashMap(allfunctions.length);
/*  19: 62 */     this.functions = new HashMap(allfunctions.length);
/*  20:    */     
/*  21:    */ 
/*  22: 65 */     Function f = null;
/*  23: 66 */     String n = null;
/*  24: 67 */     String propname = null;
/*  25: 68 */     for (int i = 0; i < allfunctions.length; i++)
/*  26:    */     {
/*  27: 70 */       f = allfunctions[i];
/*  28: 71 */       propname = f.getPropertyName();
/*  29:    */       
/*  30: 73 */       n = propname.length() != 0 ? rb.getString(propname) : null;
/*  31: 75 */       if (n != null)
/*  32:    */       {
/*  33: 77 */         this.names.put(f, n);
/*  34: 78 */         this.functions.put(n, f);
/*  35:    */       }
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   Function getFunction(String s)
/*  40:    */   {
/*  41: 91 */     return (Function)this.functions.get(s);
/*  42:    */   }
/*  43:    */   
/*  44:    */   String getName(Function f)
/*  45:    */   {
/*  46:102 */     return (String)this.names.get(f);
/*  47:    */   }
/*  48:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.FunctionNames
 * JD-Core Version:    0.7.0.1
 */