/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class PaneRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private int rowsVisible;
/*  11:    */   private int columnsVisible;
/*  12:    */   private static final int topLeftPane = 3;
/*  13:    */   private static final int bottomLeftPane = 2;
/*  14:    */   private static final int topRightPane = 1;
/*  15:    */   private static final int bottomRightPane = 0;
/*  16:    */   
/*  17:    */   public PaneRecord(int cols, int rows)
/*  18:    */   {
/*  19: 56 */     super(Type.PANE);
/*  20:    */     
/*  21: 58 */     this.rowsVisible = rows;
/*  22: 59 */     this.columnsVisible = cols;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public byte[] getData()
/*  26:    */   {
/*  27: 69 */     byte[] data = new byte[10];
/*  28:    */     
/*  29:    */ 
/*  30: 72 */     IntegerHelper.getTwoBytes(this.columnsVisible, data, 0);
/*  31:    */     
/*  32:    */ 
/*  33: 75 */     IntegerHelper.getTwoBytes(this.rowsVisible, data, 2);
/*  34: 78 */     if (this.rowsVisible > 0) {
/*  35: 80 */       IntegerHelper.getTwoBytes(this.rowsVisible, data, 4);
/*  36:    */     }
/*  37: 84 */     if (this.columnsVisible > 0) {
/*  38: 86 */       IntegerHelper.getTwoBytes(this.columnsVisible, data, 6);
/*  39:    */     }
/*  40: 90 */     int activePane = 3;
/*  41: 92 */     if ((this.rowsVisible > 0) && (this.columnsVisible == 0)) {
/*  42: 94 */       activePane = 2;
/*  43: 96 */     } else if ((this.rowsVisible == 0) && (this.columnsVisible > 0)) {
/*  44: 98 */       activePane = 1;
/*  45:100 */     } else if ((this.rowsVisible > 0) && (this.columnsVisible > 0)) {
/*  46:102 */       activePane = 0;
/*  47:    */     }
/*  48:105 */     IntegerHelper.getTwoBytes(activePane, data, 8);
/*  49:    */     
/*  50:107 */     return data;
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PaneRecord
 * JD-Core Version:    0.7.0.1
 */