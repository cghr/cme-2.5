/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.LabelCell;
/*   5:    */ import jxl.WorkbookSettings;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.IntegerHelper;
/*   8:    */ import jxl.biff.StringHelper;
/*   9:    */ 
/*  10:    */ class LabelRecord
/*  11:    */   extends CellValue
/*  12:    */   implements LabelCell
/*  13:    */ {
/*  14:    */   private int length;
/*  15:    */   private String string;
/*  16: 48 */   public static Biff7 biff7 = new Biff7(null);
/*  17:    */   
/*  18:    */   public LabelRecord(Record t, FormattingRecords fr, SheetImpl si, WorkbookSettings ws)
/*  19:    */   {
/*  20: 61 */     super(t, fr, si);
/*  21: 62 */     byte[] data = getRecord().getData();
/*  22: 63 */     this.length = IntegerHelper.getInt(data[6], data[7]);
/*  23: 65 */     if (data[8] == 0) {
/*  24: 67 */       this.string = StringHelper.getString(data, this.length, 9, ws);
/*  25:    */     } else {
/*  26: 71 */       this.string = StringHelper.getUnicodeString(data, this.length, 9);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public LabelRecord(Record t, FormattingRecords fr, SheetImpl si, WorkbookSettings ws, Biff7 dummy)
/*  31:    */   {
/*  32: 87 */     super(t, fr, si);
/*  33: 88 */     byte[] data = getRecord().getData();
/*  34: 89 */     this.length = IntegerHelper.getInt(data[6], data[7]);
/*  35:    */     
/*  36: 91 */     this.string = StringHelper.getString(data, this.length, 8, ws);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getString()
/*  40:    */   {
/*  41:101 */     return this.string;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getContents()
/*  45:    */   {
/*  46:111 */     return this.string;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public CellType getType()
/*  50:    */   {
/*  51:121 */     return CellType.LABEL;
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static class Biff7 {}
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.LabelRecord
 * JD-Core Version:    0.7.0.1
 */