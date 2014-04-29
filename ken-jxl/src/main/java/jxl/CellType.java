/*  1:   */ package jxl;
/*  2:   */ 
/*  3:   */ public final class CellType
/*  4:   */ {
/*  5:   */   private String description;
/*  6:   */   
/*  7:   */   private CellType(String desc)
/*  8:   */   {
/*  9:39 */     this.description = desc;
/* 10:   */   }
/* 11:   */   
/* 12:   */   public String toString()
/* 13:   */   {
/* 14:49 */     return this.description;
/* 15:   */   }
/* 16:   */   
/* 17:55 */   public static final CellType EMPTY = new CellType("Empty");
/* 18:58 */   public static final CellType LABEL = new CellType("Label");
/* 19:61 */   public static final CellType NUMBER = new CellType("Number");
/* 20:64 */   public static final CellType BOOLEAN = new CellType("Boolean");
/* 21:67 */   public static final CellType ERROR = new CellType("Error");
/* 22:71 */   public static final CellType NUMBER_FORMULA = new CellType("Numerical Formula");
/* 23:74 */   public static final CellType DATE_FORMULA = new CellType("Date Formula");
/* 24:77 */   public static final CellType STRING_FORMULA = new CellType("String Formula");
/* 25:81 */   public static final CellType BOOLEAN_FORMULA = new CellType("Boolean Formula");
/* 26:84 */   public static final CellType FORMULA_ERROR = new CellType("Formula Error");
/* 27:87 */   public static final CellType DATE = new CellType("Date");
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.CellType
 * JD-Core Version:    0.7.0.1
 */