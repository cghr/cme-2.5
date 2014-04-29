/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.SheetSettings;
/*   4:    */ import jxl.biff.DoubleHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.biff.WritableRecordData;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ import jxl.format.PageOrder;
/*  10:    */ import jxl.format.PageOrientation;
/*  11:    */ import jxl.format.PaperSize;
/*  12:    */ 
/*  13:    */ class SetupRecord
/*  14:    */   extends WritableRecordData
/*  15:    */ {
/*  16: 41 */   Logger logger = Logger.getLogger(SetupRecord.class);
/*  17:    */   private byte[] data;
/*  18:    */   private double headerMargin;
/*  19:    */   private double footerMargin;
/*  20:    */   private PageOrientation orientation;
/*  21:    */   private PageOrder order;
/*  22:    */   private int paperSize;
/*  23:    */   private int scaleFactor;
/*  24:    */   private int pageStart;
/*  25:    */   private int fitWidth;
/*  26:    */   private int fitHeight;
/*  27:    */   private int horizontalPrintResolution;
/*  28:    */   private int verticalPrintResolution;
/*  29:    */   private int copies;
/*  30:    */   private boolean initialized;
/*  31:    */   
/*  32:    */   public SetupRecord(SheetSettings s)
/*  33:    */   {
/*  34:122 */     super(Type.SETUP);
/*  35:    */     
/*  36:124 */     this.orientation = s.getOrientation();
/*  37:125 */     this.order = s.getPageOrder();
/*  38:126 */     this.headerMargin = s.getHeaderMargin();
/*  39:127 */     this.footerMargin = s.getFooterMargin();
/*  40:128 */     this.paperSize = s.getPaperSize().getValue();
/*  41:129 */     this.horizontalPrintResolution = s.getHorizontalPrintResolution();
/*  42:130 */     this.verticalPrintResolution = s.getVerticalPrintResolution();
/*  43:131 */     this.fitWidth = s.getFitWidth();
/*  44:132 */     this.fitHeight = s.getFitHeight();
/*  45:133 */     this.pageStart = s.getPageStart();
/*  46:134 */     this.scaleFactor = s.getScaleFactor();
/*  47:135 */     this.copies = s.getCopies();
/*  48:136 */     this.initialized = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setOrientation(PageOrientation o)
/*  52:    */   {
/*  53:146 */     this.orientation = o;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setOrder(PageOrder o)
/*  57:    */   {
/*  58:156 */     this.order = o;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setMargins(double hm, double fm)
/*  62:    */   {
/*  63:167 */     this.headerMargin = hm;
/*  64:168 */     this.footerMargin = fm;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setPaperSize(PaperSize ps)
/*  68:    */   {
/*  69:178 */     this.paperSize = ps.getValue();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public byte[] getData()
/*  73:    */   {
/*  74:188 */     this.data = new byte[34];
/*  75:    */     
/*  76:    */ 
/*  77:191 */     IntegerHelper.getTwoBytes(this.paperSize, this.data, 0);
/*  78:    */     
/*  79:    */ 
/*  80:194 */     IntegerHelper.getTwoBytes(this.scaleFactor, this.data, 2);
/*  81:    */     
/*  82:    */ 
/*  83:197 */     IntegerHelper.getTwoBytes(this.pageStart, this.data, 4);
/*  84:    */     
/*  85:    */ 
/*  86:200 */     IntegerHelper.getTwoBytes(this.fitWidth, this.data, 6);
/*  87:    */     
/*  88:    */ 
/*  89:203 */     IntegerHelper.getTwoBytes(this.fitHeight, this.data, 8);
/*  90:    */     
/*  91:    */ 
/*  92:206 */     int options = 0;
/*  93:207 */     if (this.order == PageOrder.RIGHT_THEN_DOWN) {
/*  94:209 */       options |= 0x1;
/*  95:    */     }
/*  96:212 */     if (this.orientation == PageOrientation.PORTRAIT) {
/*  97:214 */       options |= 0x2;
/*  98:    */     }
/*  99:217 */     if (this.pageStart != 0) {
/* 100:219 */       options |= 0x80;
/* 101:    */     }
/* 102:222 */     if (!this.initialized) {
/* 103:224 */       options |= 0x4;
/* 104:    */     }
/* 105:227 */     IntegerHelper.getTwoBytes(options, this.data, 10);
/* 106:    */     
/* 107:    */ 
/* 108:230 */     IntegerHelper.getTwoBytes(this.horizontalPrintResolution, this.data, 12);
/* 109:    */     
/* 110:    */ 
/* 111:233 */     IntegerHelper.getTwoBytes(this.verticalPrintResolution, this.data, 14);
/* 112:    */     
/* 113:    */ 
/* 114:236 */     DoubleHelper.getIEEEBytes(this.headerMargin, this.data, 16);
/* 115:    */     
/* 116:    */ 
/* 117:239 */     DoubleHelper.getIEEEBytes(this.footerMargin, this.data, 24);
/* 118:    */     
/* 119:    */ 
/* 120:242 */     IntegerHelper.getTwoBytes(this.copies, this.data, 32);
/* 121:    */     
/* 122:244 */     return this.data;
/* 123:    */   }
/* 124:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SetupRecord
 * JD-Core Version:    0.7.0.1
 */