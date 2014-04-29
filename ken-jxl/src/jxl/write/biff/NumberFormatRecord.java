/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.FormatRecord;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ public class NumberFormatRecord
/*   7:    */   extends FormatRecord
/*   8:    */ {
/*   9: 34 */   private static Logger logger = Logger.getLogger(NumberFormatRecord.class);
/*  10:    */   
/*  11:    */   protected NumberFormatRecord(String fmt)
/*  12:    */   {
/*  13: 51 */     String fs = fmt;
/*  14:    */     
/*  15: 53 */     fs = replace(fs, "E0", "E+0");
/*  16:    */     
/*  17: 55 */     fs = trimInvalidChars(fs);
/*  18:    */     
/*  19: 57 */     setFormatString(fs);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected NumberFormatRecord(String fmt, NonValidatingFormat dummy)
/*  23:    */   {
/*  24: 71 */     String fs = fmt;
/*  25:    */     
/*  26: 73 */     fs = replace(fs, "E0", "E+0");
/*  27:    */     
/*  28: 75 */     setFormatString(fs);
/*  29:    */   }
/*  30:    */   
/*  31:    */   private String trimInvalidChars(String fs)
/*  32:    */   {
/*  33: 87 */     int firstHash = fs.indexOf('#');
/*  34: 88 */     int firstZero = fs.indexOf('0');
/*  35: 89 */     int firstValidChar = 0;
/*  36: 91 */     if ((firstHash == -1) && (firstZero == -1)) {
/*  37: 94 */       return "#.###";
/*  38:    */     }
/*  39: 97 */     if ((firstHash != 0) && (firstZero != 0) && 
/*  40: 98 */       (firstHash != 1) && (firstZero != 1))
/*  41:    */     {
/*  42:101 */       firstHash = firstHash == -1 ? (firstHash = 2147483647) : firstHash;
/*  43:102 */       firstZero = firstZero == -1 ? (firstZero = 2147483647) : firstZero;
/*  44:103 */       firstValidChar = Math.min(firstHash, firstZero);
/*  45:    */       
/*  46:105 */       StringBuffer tmp = new StringBuffer();
/*  47:106 */       tmp.append(fs.charAt(0));
/*  48:107 */       tmp.append(fs.substring(firstValidChar));
/*  49:108 */       fs = tmp.toString();
/*  50:    */     }
/*  51:112 */     int lastHash = fs.lastIndexOf('#');
/*  52:113 */     int lastZero = fs.lastIndexOf('0');
/*  53:115 */     if ((lastHash == fs.length()) || 
/*  54:116 */       (lastZero == fs.length())) {
/*  55:118 */       return fs;
/*  56:    */     }
/*  57:122 */     int lastValidChar = Math.max(lastHash, lastZero);
/*  58:125 */     while ((fs.length() > lastValidChar + 1) && (
/*  59:126 */       (fs.charAt(lastValidChar + 1) == ')') || 
/*  60:127 */       (fs.charAt(lastValidChar + 1) == '%'))) {
/*  61:129 */       lastValidChar++;
/*  62:    */     }
/*  63:132 */     return fs.substring(0, lastValidChar + 1);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected static class NonValidatingFormat {}
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.NumberFormatRecord
 * JD-Core Version:    0.7.0.1
 */