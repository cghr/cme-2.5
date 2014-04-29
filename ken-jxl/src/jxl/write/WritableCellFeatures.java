/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import jxl.CellFeatures;
/*   5:    */ import jxl.biff.BaseCellFeatures;
/*   6:    */ import jxl.biff.BaseCellFeatures.ValidationCondition;
/*   7:    */ 
/*   8:    */ public class WritableCellFeatures
/*   9:    */   extends CellFeatures
/*  10:    */ {
/*  11: 34 */   public static final BaseCellFeatures.ValidationCondition BETWEEN = BaseCellFeatures.BETWEEN;
/*  12: 36 */   public static final BaseCellFeatures.ValidationCondition NOT_BETWEEN = BaseCellFeatures.NOT_BETWEEN;
/*  13: 37 */   public static final BaseCellFeatures.ValidationCondition EQUAL = BaseCellFeatures.EQUAL;
/*  14: 39 */   public static final BaseCellFeatures.ValidationCondition NOT_EQUAL = BaseCellFeatures.NOT_EQUAL;
/*  15: 41 */   public static final BaseCellFeatures.ValidationCondition GREATER_THAN = BaseCellFeatures.GREATER_THAN;
/*  16: 43 */   public static final BaseCellFeatures.ValidationCondition LESS_THAN = BaseCellFeatures.LESS_THAN;
/*  17: 45 */   public static final BaseCellFeatures.ValidationCondition GREATER_EQUAL = BaseCellFeatures.GREATER_EQUAL;
/*  18: 47 */   public static final BaseCellFeatures.ValidationCondition LESS_EQUAL = BaseCellFeatures.LESS_EQUAL;
/*  19:    */   
/*  20:    */   public WritableCellFeatures() {}
/*  21:    */   
/*  22:    */   public WritableCellFeatures(CellFeatures cf)
/*  23:    */   {
/*  24: 64 */     super(cf);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setComment(String s)
/*  28:    */   {
/*  29: 74 */     super.setComment(s);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setComment(String s, double width, double height)
/*  33:    */   {
/*  34: 87 */     super.setComment(s, width, height);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void removeComment()
/*  38:    */   {
/*  39: 95 */     super.removeComment();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void removeDataValidation()
/*  43:    */   {
/*  44:104 */     super.removeDataValidation();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setDataValidationList(Collection c)
/*  48:    */   {
/*  49:116 */     super.setDataValidationList(c);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setDataValidationRange(int col1, int row1, int col2, int row2)
/*  53:    */   {
/*  54:129 */     super.setDataValidationRange(col1, row1, col2, row2);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setDataValidationRange(String namedRange)
/*  58:    */   {
/*  59:141 */     super.setDataValidationRange(namedRange);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setNumberValidation(double val, BaseCellFeatures.ValidationCondition c)
/*  63:    */   {
/*  64:153 */     super.setNumberValidation(val, c);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setNumberValidation(double val1, double val2, BaseCellFeatures.ValidationCondition c)
/*  68:    */   {
/*  69:167 */     super.setNumberValidation(val1, val2, c);
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableCellFeatures
 * JD-Core Version:    0.7.0.1
 */