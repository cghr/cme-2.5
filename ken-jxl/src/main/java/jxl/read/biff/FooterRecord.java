/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ 
/*   8:    */ public class FooterRecord
/*   9:    */   extends RecordData
/*  10:    */ {
/*  11:    */   private String footer;
/*  12: 41 */   public static Biff7 biff7 = new Biff7(null);
/*  13:    */   
/*  14:    */   FooterRecord(Record t, WorkbookSettings ws)
/*  15:    */   {
/*  16: 51 */     super(t);
/*  17: 52 */     byte[] data = getRecord().getData();
/*  18: 54 */     if (data.length == 0) {
/*  19: 56 */       return;
/*  20:    */     }
/*  21: 59 */     int chars = IntegerHelper.getInt(data[0], data[1]);
/*  22:    */     
/*  23: 61 */     boolean unicode = data[2] == 1;
/*  24: 63 */     if (unicode) {
/*  25: 65 */       this.footer = StringHelper.getUnicodeString(data, chars, 3);
/*  26:    */     } else {
/*  27: 69 */       this.footer = StringHelper.getString(data, chars, 3, ws);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   FooterRecord(Record t, WorkbookSettings ws, Biff7 dummy)
/*  32:    */   {
/*  33: 82 */     super(t);
/*  34: 83 */     byte[] data = getRecord().getData();
/*  35: 85 */     if (data.length == 0) {
/*  36: 87 */       return;
/*  37:    */     }
/*  38: 90 */     int chars = data[0];
/*  39: 91 */     this.footer = StringHelper.getString(data, chars, 1, ws);
/*  40:    */   }
/*  41:    */   
/*  42:    */   String getFooter()
/*  43:    */   {
/*  44:101 */     return this.footer;
/*  45:    */   }
/*  46:    */   
/*  47:    */   private static class Biff7 {}
/*  48:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.FooterRecord
 * JD-Core Version:    0.7.0.1
 */