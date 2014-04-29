/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ public class FormulaErrorCode
/*   4:    */ {
/*   5:    */   private int errorCode;
/*   6:    */   private String description;
/*   7: 40 */   private static FormulaErrorCode[] codes = new FormulaErrorCode[0];
/*   8:    */   
/*   9:    */   FormulaErrorCode(int code, String desc)
/*  10:    */   {
/*  11: 50 */     this.errorCode = code;
/*  12: 51 */     this.description = desc;
/*  13: 52 */     FormulaErrorCode[] newcodes = new FormulaErrorCode[codes.length + 1];
/*  14: 53 */     System.arraycopy(codes, 0, newcodes, 0, codes.length);
/*  15: 54 */     newcodes[codes.length] = this;
/*  16: 55 */     codes = newcodes;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public int getCode()
/*  20:    */   {
/*  21: 65 */     return this.errorCode;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String getDescription()
/*  25:    */   {
/*  26: 75 */     return this.description;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static FormulaErrorCode getErrorCode(int code)
/*  30:    */   {
/*  31: 86 */     boolean found = false;
/*  32: 87 */     FormulaErrorCode ec = UNKNOWN;
/*  33: 88 */     for (int i = 0; (i < codes.length) && (!found); i++) {
/*  34: 90 */       if (codes[i].errorCode == code)
/*  35:    */       {
/*  36: 92 */         found = true;
/*  37: 93 */         ec = codes[i];
/*  38:    */       }
/*  39:    */     }
/*  40: 96 */     return ec;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static FormulaErrorCode getErrorCode(String code)
/*  44:    */   {
/*  45:107 */     boolean found = false;
/*  46:108 */     FormulaErrorCode ec = UNKNOWN;
/*  47:110 */     if ((code == null) || (code.length() == 0)) {
/*  48:112 */       return ec;
/*  49:    */     }
/*  50:115 */     for (int i = 0; (i < codes.length) && (!found); i++) {
/*  51:117 */       if (codes[i].description.equals(code))
/*  52:    */       {
/*  53:119 */         found = true;
/*  54:120 */         ec = codes[i];
/*  55:    */       }
/*  56:    */     }
/*  57:123 */     return ec;
/*  58:    */   }
/*  59:    */   
/*  60:127 */   public static final FormulaErrorCode UNKNOWN = new FormulaErrorCode(255, "?");
/*  61:129 */   public static final FormulaErrorCode NULL = new FormulaErrorCode(0, "#NULL!");
/*  62:131 */   public static final FormulaErrorCode DIV0 = new FormulaErrorCode(7, "#DIV/0!");
/*  63:133 */   public static final FormulaErrorCode VALUE = new FormulaErrorCode(15, "#VALUE!");
/*  64:135 */   public static final FormulaErrorCode REF = new FormulaErrorCode(23, "#REF!");
/*  65:137 */   public static final FormulaErrorCode NAME = new FormulaErrorCode(29, "#NAME?");
/*  66:138 */   public static final FormulaErrorCode NUM = new FormulaErrorCode(36, 
/*  67:139 */     "#NUM!");
/*  68:140 */   public static final FormulaErrorCode NA = new FormulaErrorCode(42, 
/*  69:141 */     "#N/A!");
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.FormulaErrorCode
 * JD-Core Version:    0.7.0.1
 */