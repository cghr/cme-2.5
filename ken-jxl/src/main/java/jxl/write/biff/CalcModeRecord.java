/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class CalcModeRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private CalcMode calculationMode;
/* 11:   */   
/* 12:   */   private static class CalcMode
/* 13:   */   {
/* 14:   */     int value;
/* 15:   */     
/* 16:   */     public CalcMode(int m)
/* 17:   */     {
/* 18:51 */       this.value = m;
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:58 */   static CalcMode manual = new CalcMode(0);
/* 23:62 */   static CalcMode automatic = new CalcMode(1);
/* 24:66 */   static CalcMode automaticNoTables = new CalcMode(-1);
/* 25:   */   
/* 26:   */   public CalcModeRecord(CalcMode cm)
/* 27:   */   {
/* 28:75 */     super(Type.CALCMODE);
/* 29:76 */     this.calculationMode = cm;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public byte[] getData()
/* 33:   */   {
/* 34:87 */     byte[] data = new byte[2];
/* 35:   */     
/* 36:89 */     IntegerHelper.getTwoBytes(this.calculationMode.value, data, 0);
/* 37:   */     
/* 38:91 */     return data;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.CalcModeRecord
 * JD-Core Version:    0.7.0.1
 */