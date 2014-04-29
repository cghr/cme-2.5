/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ public class HeaderRecord
/*  10:    */   extends RecordData
/*  11:    */ {
/*  12: 37 */   private static Logger logger = Logger.getLogger(HeaderRecord.class);
/*  13:    */   private String header;
/*  14: 48 */   public static Biff7 biff7 = new Biff7(null);
/*  15:    */   
/*  16:    */   HeaderRecord(Record t, WorkbookSettings ws)
/*  17:    */   {
/*  18: 58 */     super(t);
/*  19: 59 */     byte[] data = getRecord().getData();
/*  20: 61 */     if (data.length == 0) {
/*  21: 63 */       return;
/*  22:    */     }
/*  23: 66 */     int chars = IntegerHelper.getInt(data[0], data[1]);
/*  24:    */     
/*  25: 68 */     boolean unicode = data[2] == 1;
/*  26: 70 */     if (unicode) {
/*  27: 72 */       this.header = StringHelper.getUnicodeString(data, chars, 3);
/*  28:    */     } else {
/*  29: 76 */       this.header = StringHelper.getString(data, chars, 3, ws);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   HeaderRecord(Record t, WorkbookSettings ws, Biff7 dummy)
/*  34:    */   {
/*  35: 89 */     super(t);
/*  36: 90 */     byte[] data = getRecord().getData();
/*  37: 92 */     if (data.length == 0) {
/*  38: 94 */       return;
/*  39:    */     }
/*  40: 97 */     int chars = data[0];
/*  41: 98 */     this.header = StringHelper.getString(data, chars, 1, ws);
/*  42:    */   }
/*  43:    */   
/*  44:    */   String getHeader()
/*  45:    */   {
/*  46:108 */     return this.header;
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static class Biff7 {}
/*  50:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.HeaderRecord
 * JD-Core Version:    0.7.0.1
 */