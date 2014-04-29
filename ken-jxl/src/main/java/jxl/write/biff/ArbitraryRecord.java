/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class ArbitraryRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:35 */   private static Logger logger = Logger.getLogger(ArbitraryRecord.class);
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public ArbitraryRecord(int type, byte[] d)
/* 14:   */   {
/* 15:50 */     super(Type.createType(type));
/* 16:   */     
/* 17:52 */     this.data = d;
/* 18:53 */     logger.warn("ArbitraryRecord of type " + type + " created");
/* 19:   */   }
/* 20:   */   
/* 21:   */   public byte[] getData()
/* 22:   */   {
/* 23:63 */     return this.data;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ArbitraryRecord
 * JD-Core Version:    0.7.0.1
 */