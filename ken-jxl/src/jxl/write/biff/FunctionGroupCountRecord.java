/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class FunctionGroupCountRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   private int numFunctionGroups;
/* 12:   */   
/* 13:   */   public FunctionGroupCountRecord()
/* 14:   */   {
/* 15:46 */     super(Type.FNGROUPCOUNT);
/* 16:   */     
/* 17:48 */     this.numFunctionGroups = 14;
/* 18:   */     
/* 19:50 */     this.data = new byte[2];
/* 20:   */     
/* 21:52 */     IntegerHelper.getTwoBytes(this.numFunctionGroups, this.data, 0);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:62 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.FunctionGroupCountRecord
 * JD-Core Version:    0.7.0.1
 */