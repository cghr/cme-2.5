/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class MMSRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte numMenuItemsAdded;
/* 10:   */   private byte numMenuItemsDeleted;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public MMSRecord(int menuItemsAdded, int menuItemsDeleted)
/* 14:   */   {
/* 15:51 */     super(Type.MMS);
/* 16:   */     
/* 17:53 */     this.numMenuItemsAdded = ((byte)menuItemsAdded);
/* 18:54 */     this.numMenuItemsDeleted = ((byte)menuItemsDeleted);
/* 19:   */     
/* 20:56 */     this.data = new byte[2];
/* 21:   */     
/* 22:58 */     this.data[0] = this.numMenuItemsAdded;
/* 23:59 */     this.data[1] = this.numMenuItemsDeleted;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public byte[] getData()
/* 27:   */   {
/* 28:69 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MMSRecord
 * JD-Core Version:    0.7.0.1
 */