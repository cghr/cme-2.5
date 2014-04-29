/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class Window1Record
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   private int selectedSheet;
/* 12:   */   
/* 13:   */   public Window1Record(int selSheet)
/* 14:   */   {
/* 15:46 */     super(Type.WINDOW1);
/* 16:   */     
/* 17:48 */     this.selectedSheet = selSheet;
/* 18:   */     
/* 19:   */ 
/* 20:51 */     this.data = 
/* 21:52 */       new byte[] { 104, 
/* 22:53 */       1, 
/* 23:54 */       14, 
/* 24:55 */       1, 
/* 25:56 */       92, 
/* 26:57 */       58, 
/* 27:58 */       -66, 
/* 28:59 */       35, 
/* 29:60 */       56, 
/* 30:   */       
/* 31:   */ 
/* 32:   */ 
/* 33:   */ 
/* 34:   */ 
/* 35:66 */       0, 0, 0, 0, 0, 1, 
/* 36:   */       
/* 37:68 */       0, 88, 
/* 38:69 */       2 };
/* 39:   */     
/* 40:71 */     IntegerHelper.getTwoBytes(this.selectedSheet, this.data, 10);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public byte[] getData()
/* 44:   */   {
/* 45:81 */     return this.data;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Window1Record
 * JD-Core Version:    0.7.0.1
 */