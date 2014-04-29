/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class CodepageRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:35 */   private static Logger logger = Logger.getLogger(CodepageRecord.class);
/* 11:   */   private int characterSet;
/* 12:   */   
/* 13:   */   public CodepageRecord(Record t)
/* 14:   */   {
/* 15:49 */     super(t);
/* 16:50 */     byte[] data = t.getData();
/* 17:51 */     this.characterSet = IntegerHelper.getInt(data[0], data[1]);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getCharacterSet()
/* 21:   */   {
/* 22:61 */     return this.characterSet;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CodepageRecord
 * JD-Core Version:    0.7.0.1
 */