/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class SelectionRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private PaneType pane;
/* 11:   */   private int column;
/* 12:   */   private int row;
/* 13:   */   
/* 14:   */   private static class PaneType
/* 15:   */   {
/* 16:   */     int val;
/* 17:   */     
/* 18:   */     PaneType(int v)
/* 19:   */     {
/* 20:52 */       this.val = v;
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:56 */   public static final PaneType lowerRight = new PaneType(0);
/* 25:57 */   public static final PaneType upperRight = new PaneType(1);
/* 26:58 */   public static final PaneType lowerLeft = new PaneType(2);
/* 27:59 */   public static final PaneType upperLeft = new PaneType(3);
/* 28:   */   
/* 29:   */   public SelectionRecord(PaneType pt, int col, int r)
/* 30:   */   {
/* 31:66 */     super(Type.SELECTION);
/* 32:67 */     this.column = col;
/* 33:68 */     this.row = r;
/* 34:69 */     this.pane = pt;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public byte[] getData()
/* 38:   */   {
/* 39:80 */     byte[] data = new byte[15];
/* 40:   */     
/* 41:82 */     data[0] = ((byte)this.pane.val);
/* 42:83 */     IntegerHelper.getTwoBytes(this.row, data, 1);
/* 43:84 */     IntegerHelper.getTwoBytes(this.column, data, 3);
/* 44:   */     
/* 45:86 */     data[7] = 1;
/* 46:   */     
/* 47:88 */     IntegerHelper.getTwoBytes(this.row, data, 9);
/* 48:89 */     IntegerHelper.getTwoBytes(this.row, data, 11);
/* 49:90 */     data[13] = ((byte)this.column);
/* 50:91 */     data[14] = ((byte)this.column);
/* 51:   */     
/* 52:93 */     return data;
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SelectionRecord
 * JD-Core Version:    0.7.0.1
 */