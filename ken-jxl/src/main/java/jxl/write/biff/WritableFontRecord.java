/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.FontRecord;
/*   4:    */ import jxl.format.Font;
/*   5:    */ import jxl.write.WriteException;
/*   6:    */ 
/*   7:    */ public class WritableFontRecord
/*   8:    */   extends FontRecord
/*   9:    */ {
/*  10:    */   protected WritableFontRecord(String fn, int ps, int bold, boolean it, int us, int ci, int ss)
/*  11:    */   {
/*  12: 46 */     super(fn, ps, bold, it, us, ci, ss);
/*  13:    */   }
/*  14:    */   
/*  15:    */   protected WritableFontRecord(Font f)
/*  16:    */   {
/*  17: 56 */     super(f);
/*  18:    */   }
/*  19:    */   
/*  20:    */   protected void setPointSize(int pointSize)
/*  21:    */     throws WriteException
/*  22:    */   {
/*  23: 68 */     if (isInitialized()) {
/*  24: 70 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  25:    */     }
/*  26: 73 */     super.setFontPointSize(pointSize);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected void setBoldStyle(int boldStyle)
/*  30:    */     throws WriteException
/*  31:    */   {
/*  32: 84 */     if (isInitialized()) {
/*  33: 86 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  34:    */     }
/*  35: 89 */     super.setFontBoldStyle(boldStyle);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected void setItalic(boolean italic)
/*  39:    */     throws WriteException
/*  40:    */   {
/*  41:101 */     if (isInitialized()) {
/*  42:103 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  43:    */     }
/*  44:106 */     super.setFontItalic(italic);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void setUnderlineStyle(int us)
/*  48:    */     throws WriteException
/*  49:    */   {
/*  50:118 */     if (isInitialized()) {
/*  51:120 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  52:    */     }
/*  53:123 */     super.setFontUnderlineStyle(us);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void setColour(int colour)
/*  57:    */     throws WriteException
/*  58:    */   {
/*  59:135 */     if (isInitialized()) {
/*  60:137 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  61:    */     }
/*  62:140 */     super.setFontColour(colour);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected void setScriptStyle(int scriptStyle)
/*  66:    */     throws WriteException
/*  67:    */   {
/*  68:152 */     if (isInitialized()) {
/*  69:154 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  70:    */     }
/*  71:157 */     super.setFontScriptStyle(scriptStyle);
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected void setStruckout(boolean os)
/*  75:    */     throws WriteException
/*  76:    */   {
/*  77:168 */     if (isInitialized()) {
/*  78:170 */       throw new JxlWriteException(JxlWriteException.formatInitialized);
/*  79:    */     }
/*  80:172 */     super.setFontStruckout(os);
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WritableFontRecord
 * JD-Core Version:    0.7.0.1
 */