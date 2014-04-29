/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class ButtonPropertySetRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public ButtonPropertySetRecord(jxl.read.biff.ButtonPropertySetRecord bps)
/* 12:   */   {
/* 13:40 */     super(Type.BUTTONPROPERTYSET);
/* 14:   */     
/* 15:42 */     this.data = bps.getData();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public ButtonPropertySetRecord(ButtonPropertySetRecord bps)
/* 19:   */   {
/* 20:50 */     super(Type.BUTTONPROPERTYSET);
/* 21:   */     
/* 22:52 */     this.data = bps.getData();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public byte[] getData()
/* 26:   */   {
/* 27:62 */     return this.data;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ButtonPropertySetRecord
 * JD-Core Version:    0.7.0.1
 */