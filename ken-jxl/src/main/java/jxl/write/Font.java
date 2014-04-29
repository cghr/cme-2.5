/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.format.Colour;
/*   4:    */ import jxl.format.ScriptStyle;
/*   5:    */ import jxl.format.UnderlineStyle;
/*   6:    */ 
/*   7:    */ /**
/*   8:    */  * @deprecated
/*   9:    */  */
/*  10:    */ public class Font
/*  11:    */   extends WritableFont
/*  12:    */ {
/*  13:    */   /**
/*  14:    */    * @deprecated
/*  15:    */    */
/*  16: 39 */   public static final WritableFont.FontName ARIAL = WritableFont.ARIAL;
/*  17:    */   /**
/*  18:    */    * @deprecated
/*  19:    */    */
/*  20: 45 */   public static final WritableFont.FontName TIMES = WritableFont.TIMES;
/*  21:    */   /**
/*  22:    */    * @deprecated
/*  23:    */    */
/*  24: 53 */   public static final WritableFont.BoldStyle NO_BOLD = WritableFont.NO_BOLD;
/*  25:    */   /**
/*  26:    */    * @deprecated
/*  27:    */    */
/*  28: 58 */   public static final WritableFont.BoldStyle BOLD = WritableFont.BOLD;
/*  29:    */   /**
/*  30:    */    * @deprecated
/*  31:    */    */
/*  32: 65 */   public static final UnderlineStyle NO_UNDERLINE = UnderlineStyle.NO_UNDERLINE;
/*  33:    */   /**
/*  34:    */    * @deprecated
/*  35:    */    */
/*  36: 70 */   public static final UnderlineStyle SINGLE = UnderlineStyle.SINGLE;
/*  37:    */   /**
/*  38:    */    * @deprecated
/*  39:    */    */
/*  40: 75 */   public static final UnderlineStyle DOUBLE = UnderlineStyle.DOUBLE;
/*  41:    */   /**
/*  42:    */    * @deprecated
/*  43:    */    */
/*  44: 81 */   public static final UnderlineStyle SINGLE_ACCOUNTING = UnderlineStyle.SINGLE_ACCOUNTING;
/*  45:    */   /**
/*  46:    */    * @deprecated
/*  47:    */    */
/*  48: 87 */   public static final UnderlineStyle DOUBLE_ACCOUNTING = UnderlineStyle.DOUBLE_ACCOUNTING;
/*  49: 90 */   public static final ScriptStyle NORMAL_SCRIPT = ScriptStyle.NORMAL_SCRIPT;
/*  50: 91 */   public static final ScriptStyle SUPERSCRIPT = ScriptStyle.SUPERSCRIPT;
/*  51: 92 */   public static final ScriptStyle SUBSCRIPT = ScriptStyle.SUBSCRIPT;
/*  52:    */   
/*  53:    */   /**
/*  54:    */    * @deprecated
/*  55:    */    */
/*  56:    */   public Font(WritableFont.FontName fn)
/*  57:    */   {
/*  58:103 */     super(fn);
/*  59:    */   }
/*  60:    */   
/*  61:    */   /**
/*  62:    */    * @deprecated
/*  63:    */    */
/*  64:    */   public Font(WritableFont.FontName fn, int ps)
/*  65:    */   {
/*  66:116 */     super(fn, ps);
/*  67:    */   }
/*  68:    */   
/*  69:    */   /**
/*  70:    */    * @deprecated
/*  71:    */    */
/*  72:    */   public Font(WritableFont.FontName fn, int ps, WritableFont.BoldStyle bs)
/*  73:    */   {
/*  74:129 */     super(fn, ps, bs);
/*  75:    */   }
/*  76:    */   
/*  77:    */   /**
/*  78:    */    * @deprecated
/*  79:    */    */
/*  80:    */   public Font(WritableFont.FontName fn, int ps, WritableFont.BoldStyle bs, boolean italic)
/*  81:    */   {
/*  82:144 */     super(fn, ps, bs, italic);
/*  83:    */   }
/*  84:    */   
/*  85:    */   /**
/*  86:    */    * @deprecated
/*  87:    */    */
/*  88:    */   public Font(WritableFont.FontName fn, int ps, WritableFont.BoldStyle bs, boolean it, UnderlineStyle us)
/*  89:    */   {
/*  90:164 */     super(fn, ps, bs, it, us);
/*  91:    */   }
/*  92:    */   
/*  93:    */   /**
/*  94:    */    * @deprecated
/*  95:    */    */
/*  96:    */   public Font(WritableFont.FontName fn, int ps, WritableFont.BoldStyle bs, boolean it, UnderlineStyle us, Colour c)
/*  97:    */   {
/*  98:187 */     super(fn, ps, bs, it, us, c);
/*  99:    */   }
/* 100:    */   
/* 101:    */   /**
/* 102:    */    * @deprecated
/* 103:    */    */
/* 104:    */   public Font(WritableFont.FontName fn, int ps, WritableFont.BoldStyle bs, boolean it, UnderlineStyle us, Colour c, ScriptStyle ss)
/* 105:    */   {
/* 106:213 */     super(fn, ps, bs, it, us, c, ss);
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Font
 * JD-Core Version:    0.7.0.1
 */