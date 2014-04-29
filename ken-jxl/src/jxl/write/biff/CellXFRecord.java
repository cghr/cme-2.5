/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.DisplayFormat;
/*   4:    */ import jxl.biff.FontRecord;
/*   5:    */ import jxl.biff.XFRecord;
/*   6:    */ import jxl.format.Alignment;
/*   7:    */ import jxl.format.Border;
/*   8:    */ import jxl.format.BorderLineStyle;
/*   9:    */ import jxl.format.CellFormat;
/*  10:    */ import jxl.format.Colour;
/*  11:    */ import jxl.format.Orientation;
/*  12:    */ import jxl.format.Pattern;
/*  13:    */ import jxl.format.VerticalAlignment;
/*  14:    */ import jxl.write.WriteException;
/*  15:    */ 
/*  16:    */ public class CellXFRecord
/*  17:    */   extends XFRecord
/*  18:    */ {
/*  19:    */   protected CellXFRecord(FontRecord fnt, DisplayFormat form)
/*  20:    */   {
/*  21: 48 */     super(fnt, form);
/*  22: 49 */     setXFDetails(XFRecord.cell, 0);
/*  23:    */   }
/*  24:    */   
/*  25:    */   CellXFRecord(XFRecord fmt)
/*  26:    */   {
/*  27: 59 */     super(fmt);
/*  28: 60 */     setXFDetails(XFRecord.cell, 0);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected CellXFRecord(CellFormat format)
/*  32:    */   {
/*  33: 69 */     super(format);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setAlignment(Alignment a)
/*  37:    */     throws WriteException
/*  38:    */   {
/*  39: 80 */     if (isInitialized()) {
/*  40: 82 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  41:    */     }
/*  42: 84 */     super.setXFAlignment(a);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setBackground(Colour c, Pattern p)
/*  46:    */     throws WriteException
/*  47:    */   {
/*  48: 96 */     if (isInitialized()) {
/*  49: 98 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  50:    */     }
/*  51:100 */     super.setXFBackground(c, p);
/*  52:101 */     super.setXFCellOptions(16384);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setLocked(boolean l)
/*  56:    */     throws WriteException
/*  57:    */   {
/*  58:112 */     if (isInitialized()) {
/*  59:114 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  60:    */     }
/*  61:116 */     super.setXFLocked(l);
/*  62:117 */     super.setXFCellOptions(32768);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setIndentation(int i)
/*  66:    */     throws WriteException
/*  67:    */   {
/*  68:127 */     if (isInitialized()) {
/*  69:129 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  70:    */     }
/*  71:131 */     super.setXFIndentation(i);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setShrinkToFit(boolean s)
/*  75:    */     throws WriteException
/*  76:    */   {
/*  77:141 */     if (isInitialized()) {
/*  78:143 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  79:    */     }
/*  80:145 */     super.setXFShrinkToFit(s);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setVerticalAlignment(VerticalAlignment va)
/*  84:    */     throws WriteException
/*  85:    */   {
/*  86:157 */     if (isInitialized()) {
/*  87:159 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  88:    */     }
/*  89:162 */     super.setXFVerticalAlignment(va);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setOrientation(Orientation o)
/*  93:    */     throws WriteException
/*  94:    */   {
/*  95:174 */     if (isInitialized()) {
/*  96:176 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  97:    */     }
/*  98:179 */     super.setXFOrientation(o);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setWrap(boolean w)
/* 102:    */     throws WriteException
/* 103:    */   {
/* 104:192 */     if (isInitialized()) {
/* 105:194 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/* 106:    */     }
/* 107:197 */     super.setXFWrap(w);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setBorder(Border b, BorderLineStyle ls, Colour c)
/* 111:    */     throws WriteException
/* 112:    */   {
/* 113:210 */     if (isInitialized()) {
/* 114:212 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/* 115:    */     }
/* 116:215 */     if (b == Border.ALL)
/* 117:    */     {
/* 118:218 */       super.setXFBorder(Border.LEFT, ls, c);
/* 119:219 */       super.setXFBorder(Border.RIGHT, ls, c);
/* 120:220 */       super.setXFBorder(Border.TOP, ls, c);
/* 121:221 */       super.setXFBorder(Border.BOTTOM, ls, c);
/* 122:222 */       return;
/* 123:    */     }
/* 124:225 */     if (b == Border.NONE)
/* 125:    */     {
/* 126:228 */       super.setXFBorder(Border.LEFT, BorderLineStyle.NONE, Colour.BLACK);
/* 127:229 */       super.setXFBorder(Border.RIGHT, BorderLineStyle.NONE, Colour.BLACK);
/* 128:230 */       super.setXFBorder(Border.TOP, BorderLineStyle.NONE, Colour.BLACK);
/* 129:231 */       super.setXFBorder(Border.BOTTOM, BorderLineStyle.NONE, Colour.BLACK);
/* 130:232 */       return;
/* 131:    */     }
/* 132:235 */     super.setXFBorder(b, ls, c);
/* 133:    */   }
/* 134:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.CellXFRecord
 * JD-Core Version:    0.7.0.1
 */