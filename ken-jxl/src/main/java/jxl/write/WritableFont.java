/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.format.Colour;
/*   4:    */ import jxl.format.Font;
/*   5:    */ import jxl.format.ScriptStyle;
/*   6:    */ import jxl.format.UnderlineStyle;
/*   7:    */ import jxl.write.biff.WritableFontRecord;
/*   8:    */ 
/*   9:    */ public class WritableFont
/*  10:    */   extends WritableFontRecord
/*  11:    */ {
/*  12:    */   public static class FontName
/*  13:    */   {
/*  14:    */     String name;
/*  15:    */     
/*  16:    */     FontName(String s)
/*  17:    */     {
/*  18: 51 */       this.name = s;
/*  19:    */     }
/*  20:    */   }
/*  21:    */   
/*  22:    */   static class BoldStyle
/*  23:    */   {
/*  24:    */     public int value;
/*  25:    */     
/*  26:    */     BoldStyle(int val)
/*  27:    */     {
/*  28: 72 */       this.value = val;
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32: 80 */   public static final FontName ARIAL = new FontName("Arial");
/*  33: 85 */   public static final FontName TIMES = new FontName("Times New Roman");
/*  34: 90 */   public static final FontName COURIER = new FontName("Courier New");
/*  35: 95 */   public static final FontName TAHOMA = new FontName("Tahoma");
/*  36:102 */   public static final BoldStyle NO_BOLD = new BoldStyle(400);
/*  37:106 */   public static final BoldStyle BOLD = new BoldStyle(700);
/*  38:    */   public static final int DEFAULT_POINT_SIZE = 10;
/*  39:    */   
/*  40:    */   public WritableFont(FontName fn)
/*  41:    */   {
/*  42:127 */     this(fn, 10, NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public WritableFont(Font f)
/*  46:    */   {
/*  47:137 */     super(f);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public WritableFont(FontName fn, int ps)
/*  51:    */   {
/*  52:152 */     this(fn, ps, NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public WritableFont(FontName fn, int ps, BoldStyle bs)
/*  56:    */   {
/*  57:167 */     this(fn, ps, bs, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public WritableFont(FontName fn, int ps, BoldStyle bs, boolean italic)
/*  61:    */   {
/*  62:184 */     this(fn, ps, bs, italic, UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public WritableFont(FontName fn, int ps, BoldStyle bs, boolean it, UnderlineStyle us)
/*  66:    */   {
/*  67:203 */     this(fn, ps, bs, it, us, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public WritableFont(FontName fn, int ps, BoldStyle bs, boolean it, UnderlineStyle us, Colour c)
/*  71:    */   {
/*  72:225 */     this(fn, ps, bs, it, us, c, ScriptStyle.NORMAL_SCRIPT);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public WritableFont(FontName fn, int ps, BoldStyle bs, boolean it, UnderlineStyle us, Colour c, ScriptStyle ss)
/*  76:    */   {
/*  77:252 */     super(fn.name, ps, bs.value, it, us.getValue(), c.getValue(), ss.getValue());
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setPointSize(int pointSize)
/*  81:    */     throws WriteException
/*  82:    */   {
/*  83:263 */     super.setPointSize(pointSize);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setBoldStyle(BoldStyle boldStyle)
/*  87:    */     throws WriteException
/*  88:    */   {
/*  89:274 */     super.setBoldStyle(boldStyle.value);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setItalic(boolean italic)
/*  93:    */     throws WriteException
/*  94:    */   {
/*  95:286 */     super.setItalic(italic);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setUnderlineStyle(UnderlineStyle us)
/*  99:    */     throws WriteException
/* 100:    */   {
/* 101:298 */     super.setUnderlineStyle(us.getValue());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setColour(Colour colour)
/* 105:    */     throws WriteException
/* 106:    */   {
/* 107:310 */     super.setColour(colour.getValue());
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setScriptStyle(ScriptStyle scriptStyle)
/* 111:    */     throws WriteException
/* 112:    */   {
/* 113:322 */     super.setScriptStyle(scriptStyle.getValue());
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean isStruckout()
/* 117:    */   {
/* 118:332 */     return super.isStruckout();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setStruckout(boolean struckout)
/* 122:    */     throws WriteException
/* 123:    */   {
/* 124:344 */     super.setStruckout(struckout);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static FontName createFont(String fontName)
/* 128:    */   {
/* 129:357 */     return new FontName(fontName);
/* 130:    */   }
/* 131:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableFont
 * JD-Core Version:    0.7.0.1
 */