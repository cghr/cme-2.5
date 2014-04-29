/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ public class ExternalNameRecord
/*  10:    */   extends RecordData
/*  11:    */ {
/*  12: 38 */   private static Logger logger = Logger.getLogger(ExternalNameRecord.class);
/*  13:    */   private String name;
/*  14:    */   private boolean addInFunction;
/*  15:    */   
/*  16:    */   ExternalNameRecord(Record t, WorkbookSettings ws)
/*  17:    */   {
/*  18: 58 */     super(t);
/*  19:    */     
/*  20: 60 */     byte[] data = getRecord().getData();
/*  21: 61 */     int options = IntegerHelper.getInt(data[0], data[1]);
/*  22: 63 */     if (options == 0) {
/*  23: 65 */       this.addInFunction = true;
/*  24:    */     }
/*  25: 68 */     if (!this.addInFunction) {
/*  26: 70 */       return;
/*  27:    */     }
/*  28: 73 */     int length = data[6];
/*  29:    */     
/*  30: 75 */     boolean unicode = data[7] != 0;
/*  31: 77 */     if (unicode) {
/*  32: 79 */       this.name = StringHelper.getUnicodeString(data, length, 8);
/*  33:    */     } else {
/*  34: 83 */       this.name = StringHelper.getString(data, length, 8, ws);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isAddInFunction()
/*  39:    */   {
/*  40: 94 */     return this.addInFunction;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getName()
/*  44:    */   {
/*  45:104 */     return this.name;
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ExternalNameRecord
 * JD-Core Version:    0.7.0.1
 */