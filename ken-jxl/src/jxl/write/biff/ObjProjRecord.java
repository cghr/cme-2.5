/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class ObjProjRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public ObjProjRecord()
/* 12:   */   {
/* 13:40 */     super(Type.OBJPROJ);
/* 14:   */     
/* 15:42 */     this.data = new byte[4];
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte[] getData()
/* 19:   */   {
/* 20:52 */     return this.data;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ObjProjRecord
 * JD-Core Version:    0.7.0.1
 */