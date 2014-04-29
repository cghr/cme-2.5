/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ public class RowRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(RowRecord.class);
/*  11:    */   private int rowNumber;
/*  12:    */   private int rowHeight;
/*  13:    */   private boolean collapsed;
/*  14:    */   private boolean defaultFormat;
/*  15:    */   private boolean matchesDefFontHeight;
/*  16:    */   private int xfIndex;
/*  17:    */   private int outlineLevel;
/*  18:    */   private boolean groupStart;
/*  19:    */   private static final int defaultHeightIndicator = 255;
/*  20:    */   
/*  21:    */   RowRecord(Record t)
/*  22:    */   {
/*  23: 84 */     super(t);
/*  24:    */     
/*  25: 86 */     byte[] data = getRecord().getData();
/*  26: 87 */     this.rowNumber = IntegerHelper.getInt(data[0], data[1]);
/*  27: 88 */     this.rowHeight = IntegerHelper.getInt(data[6], data[7]);
/*  28:    */     
/*  29: 90 */     int options = IntegerHelper.getInt(data[12], data[13], 
/*  30: 91 */       data[14], data[15]);
/*  31: 92 */     this.outlineLevel = (options & 0x7);
/*  32: 93 */     this.groupStart = ((options & 0x10) != 0);
/*  33: 94 */     this.collapsed = ((options & 0x20) != 0);
/*  34: 95 */     this.matchesDefFontHeight = ((options & 0x40) == 0);
/*  35: 96 */     this.defaultFormat = ((options & 0x80) != 0);
/*  36: 97 */     this.xfIndex = ((options & 0xFFF0000) >> 16);
/*  37:    */   }
/*  38:    */   
/*  39:    */   boolean isDefaultHeight()
/*  40:    */   {
/*  41:107 */     return this.rowHeight == 255;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean matchesDefaultFontHeight()
/*  45:    */   {
/*  46:117 */     return this.matchesDefFontHeight;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getRowNumber()
/*  50:    */   {
/*  51:127 */     return this.rowNumber;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getOutlineLevel()
/*  55:    */   {
/*  56:137 */     return this.outlineLevel;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean getGroupStart()
/*  60:    */   {
/*  61:147 */     return this.groupStart;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getRowHeight()
/*  65:    */   {
/*  66:157 */     return this.rowHeight;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean isCollapsed()
/*  70:    */   {
/*  71:167 */     return this.collapsed;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getXFIndex()
/*  75:    */   {
/*  76:177 */     return this.xfIndex;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public boolean hasDefaultFormat()
/*  80:    */   {
/*  81:187 */     return this.defaultFormat;
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.RowRecord
 * JD-Core Version:    0.7.0.1
 */