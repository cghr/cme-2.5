/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import jxl.DateCell;
/*   5:    */ import jxl.format.CellFormat;
/*   6:    */ import jxl.write.biff.DateRecord;
/*   7:    */ import jxl.write.biff.DateRecord.GMTDate;
/*   8:    */ 
/*   9:    */ public class DateTime
/*  10:    */   extends DateRecord
/*  11:    */   implements WritableCell, DateCell
/*  12:    */ {
/*  13: 46 */   public static final DateRecord.GMTDate GMT = new DateRecord.GMTDate();
/*  14:    */   
/*  15:    */   public DateTime(int c, int r, Date d)
/*  16:    */   {
/*  17: 58 */     super(c, r, d);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public DateTime(int c, int r, Date d, DateRecord.GMTDate a)
/*  21:    */   {
/*  22: 73 */     super(c, r, d, a);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public DateTime(int c, int r, Date d, CellFormat st)
/*  26:    */   {
/*  27: 86 */     super(c, r, d, st);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DateTime(int c, int r, Date d, CellFormat st, DateRecord.GMTDate a)
/*  31:    */   {
/*  32:101 */     super(c, r, d, st, a);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DateTime(int c, int r, Date d, CellFormat st, boolean tim)
/*  36:    */   {
/*  37:118 */     super(c, r, d, st, tim);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public DateTime(DateCell dc)
/*  41:    */   {
/*  42:129 */     super(dc);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected DateTime(int col, int row, DateTime dt)
/*  46:    */   {
/*  47:141 */     super(col, row, dt);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setDate(Date d)
/*  51:    */   {
/*  52:152 */     super.setDate(d);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setDate(Date d, DateRecord.GMTDate a)
/*  56:    */   {
/*  57:163 */     super.setDate(d, a);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public WritableCell copyTo(int col, int row)
/*  61:    */   {
/*  62:175 */     return new DateTime(col, row, this);
/*  63:    */   }
/*  64:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.DateTime
 * JD-Core Version:    0.7.0.1
 */