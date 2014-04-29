/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.biff.DisplayFormat;
/*   4:    */ import jxl.format.Alignment;
/*   5:    */ import jxl.format.Border;
/*   6:    */ import jxl.format.BorderLineStyle;
/*   7:    */ import jxl.format.CellFormat;
/*   8:    */ import jxl.format.Colour;
/*   9:    */ import jxl.format.Orientation;
/*  10:    */ import jxl.format.Pattern;
/*  11:    */ import jxl.format.VerticalAlignment;
/*  12:    */ import jxl.write.biff.CellXFRecord;
/*  13:    */ 
/*  14:    */ public class WritableCellFormat
/*  15:    */   extends CellXFRecord
/*  16:    */ {
/*  17:    */   public WritableCellFormat()
/*  18:    */   {
/*  19: 53 */     this(WritableWorkbook.ARIAL_10_PT, NumberFormats.DEFAULT);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public WritableCellFormat(WritableFont font)
/*  23:    */   {
/*  24: 63 */     this(font, NumberFormats.DEFAULT);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public WritableCellFormat(DisplayFormat format)
/*  28:    */   {
/*  29: 74 */     this(WritableWorkbook.ARIAL_10_PT, format);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public WritableCellFormat(WritableFont font, DisplayFormat format)
/*  33:    */   {
/*  34: 86 */     super(font, format);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public WritableCellFormat(CellFormat format)
/*  38:    */   {
/*  39: 96 */     super(format);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setAlignment(Alignment a)
/*  43:    */     throws WriteException
/*  44:    */   {
/*  45:107 */     super.setAlignment(a);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setVerticalAlignment(VerticalAlignment va)
/*  49:    */     throws WriteException
/*  50:    */   {
/*  51:118 */     super.setVerticalAlignment(va);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setOrientation(Orientation o)
/*  55:    */     throws WriteException
/*  56:    */   {
/*  57:129 */     super.setOrientation(o);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setWrap(boolean w)
/*  61:    */     throws WriteException
/*  62:    */   {
/*  63:142 */     super.setWrap(w);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setBorder(Border b, BorderLineStyle ls)
/*  67:    */     throws WriteException
/*  68:    */   {
/*  69:154 */     super.setBorder(b, ls, Colour.BLACK);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setBorder(Border b, BorderLineStyle ls, Colour c)
/*  73:    */     throws WriteException
/*  74:    */   {
/*  75:168 */     super.setBorder(b, ls, c);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setBackground(Colour c)
/*  79:    */     throws WriteException
/*  80:    */   {
/*  81:179 */     setBackground(c, Pattern.SOLID);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setBackground(Colour c, Pattern p)
/*  85:    */     throws WriteException
/*  86:    */   {
/*  87:191 */     super.setBackground(c, p);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setShrinkToFit(boolean s)
/*  91:    */     throws WriteException
/*  92:    */   {
/*  93:202 */     super.setShrinkToFit(s);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setIndentation(int i)
/*  97:    */     throws WriteException
/*  98:    */   {
/*  99:212 */     super.setIndentation(i);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setLocked(boolean l)
/* 103:    */     throws WriteException
/* 104:    */   {
/* 105:226 */     super.setLocked(l);
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableCellFormat
 * JD-Core Version:    0.7.0.1
 */