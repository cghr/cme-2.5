/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.DoubleHelper;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ public class SetupRecord
/*  10:    */   extends RecordData
/*  11:    */ {
/*  12: 35 */   private static Logger logger = Logger.getLogger(SetupRecord.class);
/*  13:    */   private byte[] data;
/*  14:    */   private boolean portraitOrientation;
/*  15:    */   private boolean pageOrder;
/*  16:    */   private double headerMargin;
/*  17:    */   private double footerMargin;
/*  18:    */   private int paperSize;
/*  19:    */   private int scaleFactor;
/*  20:    */   private int pageStart;
/*  21:    */   private int fitWidth;
/*  22:    */   private int fitHeight;
/*  23:    */   private int horizontalPrintResolution;
/*  24:    */   private int verticalPrintResolution;
/*  25:    */   private int copies;
/*  26:    */   private boolean initialized;
/*  27:    */   
/*  28:    */   SetupRecord(Record t)
/*  29:    */   {
/*  30:115 */     super(Type.SETUP);
/*  31:    */     
/*  32:117 */     this.data = t.getData();
/*  33:    */     
/*  34:119 */     this.paperSize = IntegerHelper.getInt(this.data[0], this.data[1]);
/*  35:120 */     this.scaleFactor = IntegerHelper.getInt(this.data[2], this.data[3]);
/*  36:121 */     this.pageStart = IntegerHelper.getInt(this.data[4], this.data[5]);
/*  37:122 */     this.fitWidth = IntegerHelper.getInt(this.data[6], this.data[7]);
/*  38:123 */     this.fitHeight = IntegerHelper.getInt(this.data[8], this.data[9]);
/*  39:124 */     this.horizontalPrintResolution = IntegerHelper.getInt(this.data[12], this.data[13]);
/*  40:125 */     this.verticalPrintResolution = IntegerHelper.getInt(this.data[14], this.data[15]);
/*  41:126 */     this.copies = IntegerHelper.getInt(this.data[32], this.data[33]);
/*  42:    */     
/*  43:128 */     this.headerMargin = DoubleHelper.getIEEEDouble(this.data, 16);
/*  44:129 */     this.footerMargin = DoubleHelper.getIEEEDouble(this.data, 24);
/*  45:    */     
/*  46:    */ 
/*  47:    */ 
/*  48:133 */     int grbit = IntegerHelper.getInt(this.data[10], this.data[11]);
/*  49:134 */     this.pageOrder = ((grbit & 0x1) != 0);
/*  50:135 */     this.portraitOrientation = ((grbit & 0x2) != 0);
/*  51:136 */     this.initialized = ((grbit & 0x4) == 0);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isPortrait()
/*  55:    */   {
/*  56:146 */     return this.portraitOrientation;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isRightDown()
/*  60:    */   {
/*  61:158 */     return this.pageOrder;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public double getHeaderMargin()
/*  65:    */   {
/*  66:168 */     return this.headerMargin;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public double getFooterMargin()
/*  70:    */   {
/*  71:178 */     return this.footerMargin;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getPaperSize()
/*  75:    */   {
/*  76:188 */     return this.paperSize;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getScaleFactor()
/*  80:    */   {
/*  81:198 */     return this.scaleFactor;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getPageStart()
/*  85:    */   {
/*  86:208 */     return this.pageStart;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getFitWidth()
/*  90:    */   {
/*  91:218 */     return this.fitWidth;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getFitHeight()
/*  95:    */   {
/*  96:228 */     return this.fitHeight;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getHorizontalPrintResolution()
/* 100:    */   {
/* 101:238 */     return this.horizontalPrintResolution;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getVerticalPrintResolution()
/* 105:    */   {
/* 106:248 */     return this.verticalPrintResolution;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getCopies()
/* 110:    */   {
/* 111:258 */     return this.copies;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean getInitialized()
/* 115:    */   {
/* 116:269 */     return this.initialized;
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SetupRecord
 * JD-Core Version:    0.7.0.1
 */