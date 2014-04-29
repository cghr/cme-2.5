/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ import jxl.format.CellFormat;
/*   8:    */ 
/*   9:    */ public abstract class BlankRecord
/*  10:    */   extends CellValue
/*  11:    */ {
/*  12: 37 */   private static Logger logger = Logger.getLogger(BlankRecord.class);
/*  13:    */   
/*  14:    */   protected BlankRecord(int c, int r)
/*  15:    */   {
/*  16: 48 */     super(Type.BLANK, c, r);
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected BlankRecord(int c, int r, CellFormat st)
/*  20:    */   {
/*  21: 61 */     super(Type.BLANK, c, r, st);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected BlankRecord(Cell c)
/*  25:    */   {
/*  26: 72 */     super(Type.BLANK, c);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected BlankRecord(int c, int r, BlankRecord br)
/*  30:    */   {
/*  31: 84 */     super(Type.BLANK, c, r, br);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public CellType getType()
/*  35:    */   {
/*  36: 94 */     return CellType.EMPTY;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getContents()
/*  40:    */   {
/*  41:104 */     return "";
/*  42:    */   }
/*  43:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BlankRecord
 * JD-Core Version:    0.7.0.1
 */