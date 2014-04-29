/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.XFRecord;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ import jxl.write.DateFormat;
/*   6:    */ import jxl.write.DateFormats;
/*   7:    */ import jxl.write.NumberFormats;
/*   8:    */ import jxl.write.WritableCellFormat;
/*   9:    */ import jxl.write.WritableFont;
/*  10:    */ import jxl.write.WritableWorkbook;
/*  11:    */ 
/*  12:    */ class Styles
/*  13:    */ {
/*  14: 42 */   private static Logger logger = Logger.getLogger(Styles.class);
/*  15:    */   private WritableFont arial10pt;
/*  16:    */   private WritableFont hyperlinkFont;
/*  17:    */   private WritableCellFormat normalStyle;
/*  18:    */   private WritableCellFormat hyperlinkStyle;
/*  19:    */   private WritableCellFormat hiddenStyle;
/*  20:    */   private WritableCellFormat defaultDateFormat;
/*  21:    */   
/*  22:    */   public Styles()
/*  23:    */   {
/*  24: 79 */     this.arial10pt = null;
/*  25: 80 */     this.hyperlinkFont = null;
/*  26: 81 */     this.normalStyle = null;
/*  27: 82 */     this.hyperlinkStyle = null;
/*  28: 83 */     this.hiddenStyle = null;
/*  29:    */   }
/*  30:    */   
/*  31:    */   private synchronized void initNormalStyle()
/*  32:    */   {
/*  33: 88 */     this.normalStyle = new WritableCellFormat(getArial10Pt(), 
/*  34: 89 */       NumberFormats.DEFAULT);
/*  35: 90 */     this.normalStyle.setFont(getArial10Pt());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public WritableCellFormat getNormalStyle()
/*  39:    */   {
/*  40: 95 */     if (this.normalStyle == null) {
/*  41: 97 */       initNormalStyle();
/*  42:    */     }
/*  43:100 */     return this.normalStyle;
/*  44:    */   }
/*  45:    */   
/*  46:    */   private synchronized void initHiddenStyle()
/*  47:    */   {
/*  48:105 */     this.hiddenStyle = new WritableCellFormat(
/*  49:106 */       getArial10Pt(), new DateFormat(";;;"));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public WritableCellFormat getHiddenStyle()
/*  53:    */   {
/*  54:111 */     if (this.hiddenStyle == null) {
/*  55:113 */       initHiddenStyle();
/*  56:    */     }
/*  57:116 */     return this.hiddenStyle;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private synchronized void initHyperlinkStyle()
/*  61:    */   {
/*  62:121 */     this.hyperlinkStyle = new WritableCellFormat(getHyperlinkFont(), 
/*  63:122 */       NumberFormats.DEFAULT);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public WritableCellFormat getHyperlinkStyle()
/*  67:    */   {
/*  68:127 */     if (this.hyperlinkStyle == null) {
/*  69:129 */       initHyperlinkStyle();
/*  70:    */     }
/*  71:132 */     return this.hyperlinkStyle;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private synchronized void initArial10Pt()
/*  75:    */   {
/*  76:137 */     this.arial10pt = new WritableFont(WritableWorkbook.ARIAL_10_PT);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public WritableFont getArial10Pt()
/*  80:    */   {
/*  81:142 */     if (this.arial10pt == null) {
/*  82:144 */       initArial10Pt();
/*  83:    */     }
/*  84:147 */     return this.arial10pt;
/*  85:    */   }
/*  86:    */   
/*  87:    */   private synchronized void initHyperlinkFont()
/*  88:    */   {
/*  89:152 */     this.hyperlinkFont = new WritableFont(WritableWorkbook.HYPERLINK_FONT);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public WritableFont getHyperlinkFont()
/*  93:    */   {
/*  94:157 */     if (this.hyperlinkFont == null) {
/*  95:159 */       initHyperlinkFont();
/*  96:    */     }
/*  97:162 */     return this.hyperlinkFont;
/*  98:    */   }
/*  99:    */   
/* 100:    */   private synchronized void initDefaultDateFormat()
/* 101:    */   {
/* 102:167 */     this.defaultDateFormat = new WritableCellFormat(DateFormats.DEFAULT);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public WritableCellFormat getDefaultDateFormat()
/* 106:    */   {
/* 107:172 */     if (this.defaultDateFormat == null) {
/* 108:174 */       initDefaultDateFormat();
/* 109:    */     }
/* 110:177 */     return this.defaultDateFormat;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public XFRecord getFormat(XFRecord wf)
/* 114:    */   {
/* 115:192 */     XFRecord format = wf;
/* 116:196 */     if (format == WritableWorkbook.NORMAL_STYLE) {
/* 117:198 */       format = getNormalStyle();
/* 118:200 */     } else if (format == WritableWorkbook.HYPERLINK_STYLE) {
/* 119:202 */       format = getHyperlinkStyle();
/* 120:204 */     } else if (format == WritableWorkbook.HIDDEN_STYLE) {
/* 121:206 */       format = getHiddenStyle();
/* 122:208 */     } else if (format == DateRecord.defaultDateFormat) {
/* 123:210 */       format = getDefaultDateFormat();
/* 124:    */     }
/* 125:214 */     if (format.getFont() == WritableWorkbook.ARIAL_10_PT) {
/* 126:216 */       format.setFont(getArial10Pt());
/* 127:218 */     } else if (format.getFont() == WritableWorkbook.HYPERLINK_FONT) {
/* 128:220 */       format.setFont(getHyperlinkFont());
/* 129:    */     }
/* 130:223 */     return format;
/* 131:    */   }
/* 132:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Styles
 * JD-Core Version:    0.7.0.1
 */