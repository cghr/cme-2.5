/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ 
/*   7:    */ public class ColumnInfoRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10:    */   private byte[] data;
/*  11:    */   private int startColumn;
/*  12:    */   private int endColumn;
/*  13:    */   private int xfIndex;
/*  14:    */   private int width;
/*  15:    */   private boolean hidden;
/*  16:    */   private int outlineLevel;
/*  17:    */   private boolean collapsed;
/*  18:    */   
/*  19:    */   ColumnInfoRecord(Record t)
/*  20:    */   {
/*  21: 78 */     super(Type.COLINFO);
/*  22:    */     
/*  23: 80 */     this.data = t.getData();
/*  24:    */     
/*  25: 82 */     this.startColumn = IntegerHelper.getInt(this.data[0], this.data[1]);
/*  26: 83 */     this.endColumn = IntegerHelper.getInt(this.data[2], this.data[3]);
/*  27: 84 */     this.width = IntegerHelper.getInt(this.data[4], this.data[5]);
/*  28: 85 */     this.xfIndex = IntegerHelper.getInt(this.data[6], this.data[7]);
/*  29:    */     
/*  30: 87 */     int options = IntegerHelper.getInt(this.data[8], this.data[9]);
/*  31: 88 */     this.hidden = ((options & 0x1) != 0);
/*  32: 89 */     this.outlineLevel = ((options & 0x700) >> 8);
/*  33: 90 */     this.collapsed = ((options & 0x1000) != 0);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getStartColumn()
/*  37:    */   {
/*  38:100 */     return this.startColumn;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getEndColumn()
/*  42:    */   {
/*  43:110 */     return this.endColumn;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getXFIndex()
/*  47:    */   {
/*  48:120 */     return this.xfIndex;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getOutlineLevel()
/*  52:    */   {
/*  53:130 */     return this.outlineLevel;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean getCollapsed()
/*  57:    */   {
/*  58:140 */     return this.collapsed;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getWidth()
/*  62:    */   {
/*  63:150 */     return this.width;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean getHidden()
/*  67:    */   {
/*  68:160 */     return this.hidden;
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ColumnInfoRecord
 * JD-Core Version:    0.7.0.1
 */