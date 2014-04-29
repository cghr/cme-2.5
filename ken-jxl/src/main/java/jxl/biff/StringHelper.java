/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ public final class StringHelper
/*   8:    */ {
/*   9: 37 */   private static Logger logger = Logger.getLogger(StringHelper.class);
/*  10: 44 */   public static String UNICODE_ENCODING = "UnicodeLittle";
/*  11:    */   
/*  12:    */   /**
/*  13:    */    * @deprecated
/*  14:    */    */
/*  15:    */   public static byte[] getBytes(String s)
/*  16:    */   {
/*  17: 63 */     return s.getBytes();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static byte[] getBytes(String s, WorkbookSettings ws)
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 77 */       return s.getBytes(ws.getEncoding());
/*  25:    */     }
/*  26:    */     catch (UnsupportedEncodingException e) {}
/*  27: 82 */     return null;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static byte[] getUnicodeBytes(String s)
/*  31:    */   {
/*  32:    */     try
/*  33:    */     {
/*  34: 96 */       byte[] b = s.getBytes(UNICODE_ENCODING);
/*  35:    */       byte[] b2;
/*  36:100 */       if (b.length == s.length() * 2 + 2)
/*  37:    */       {
/*  38:102 */         b2 = new byte[b.length - 2];
/*  39:103 */         System.arraycopy(b, 2, b2, 0, b2.length);
/*  40:    */       }
/*  41:104 */       return b2;
/*  42:    */     }
/*  43:    */     catch (UnsupportedEncodingException e) {}
/*  44:111 */     return null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void getBytes(String s, byte[] d, int pos)
/*  48:    */   {
/*  49:126 */     byte[] b = getBytes(s);
/*  50:127 */     System.arraycopy(b, 0, d, pos, b.length);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static void getUnicodeBytes(String s, byte[] d, int pos)
/*  54:    */   {
/*  55:140 */     byte[] b = getUnicodeBytes(s);
/*  56:141 */     System.arraycopy(b, 0, d, pos, b.length);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static String getString(byte[] d, int length, int pos, WorkbookSettings ws)
/*  60:    */   {
/*  61:157 */     if (length == 0) {
/*  62:159 */       return "";
/*  63:    */     }
/*  64:    */     try
/*  65:    */     {
/*  66:164 */       return new String(d, pos, length, ws.getEncoding());
/*  67:    */     }
/*  68:    */     catch (UnsupportedEncodingException e)
/*  69:    */     {
/*  70:171 */       logger.warn(e.toString());
/*  71:    */     }
/*  72:172 */     return "";
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static String getUnicodeString(byte[] d, int length, int pos)
/*  76:    */   {
/*  77:    */     try
/*  78:    */     {
/*  79:188 */       byte[] b = new byte[length * 2];
/*  80:189 */       System.arraycopy(d, pos, b, 0, length * 2);
/*  81:190 */       return new String(b, UNICODE_ENCODING);
/*  82:    */     }
/*  83:    */     catch (UnsupportedEncodingException e) {}
/*  84:195 */     return "";
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static final String replace(String input, String search, String replace)
/*  88:    */   {
/*  89:213 */     String fmtstr = input;
/*  90:214 */     int pos = fmtstr.indexOf(search);
/*  91:215 */     while (pos != -1)
/*  92:    */     {
/*  93:217 */       StringBuffer tmp = new StringBuffer(fmtstr.substring(0, pos));
/*  94:218 */       tmp.append(replace);
/*  95:219 */       tmp.append(fmtstr.substring(pos + search.length()));
/*  96:220 */       fmtstr = tmp.toString();
/*  97:221 */       pos = fmtstr.indexOf(search, pos + replace.length());
/*  98:    */     }
/*  99:223 */     return fmtstr;
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.StringHelper
 * JD-Core Version:    0.7.0.1
 */