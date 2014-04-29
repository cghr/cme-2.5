/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ public class EncodedURLHelper
/*   7:    */ {
/*   8: 34 */   private static Logger logger = Logger.getLogger(EncodedURLHelper.class);
/*   9: 37 */   private static byte msDosDriveLetter = 1;
/*  10: 38 */   private static byte sameDrive = 2;
/*  11: 39 */   private static byte endOfSubdirectory = 3;
/*  12: 40 */   private static byte parentDirectory = 4;
/*  13: 41 */   private static byte unencodedUrl = 5;
/*  14:    */   
/*  15:    */   public static byte[] getEncodedURL(String s, WorkbookSettings ws)
/*  16:    */   {
/*  17: 45 */     if (s.startsWith("http:")) {
/*  18: 47 */       return getURL(s, ws);
/*  19:    */     }
/*  20: 51 */     return getFile(s, ws);
/*  21:    */   }
/*  22:    */   
/*  23:    */   private static byte[] getFile(String s, WorkbookSettings ws)
/*  24:    */   {
/*  25: 57 */     ByteArray byteArray = new ByteArray();
/*  26:    */     
/*  27: 59 */     int pos = 0;
/*  28: 60 */     if (s.charAt(1) == ':')
/*  29:    */     {
/*  30: 63 */       byteArray.add(msDosDriveLetter);
/*  31: 64 */       byteArray.add((byte)s.charAt(0));
/*  32: 65 */       pos = 2;
/*  33:    */     }
/*  34: 67 */     else if ((s.charAt(pos) == '\\') || 
/*  35: 68 */       (s.charAt(pos) == '/'))
/*  36:    */     {
/*  37: 70 */       byteArray.add(sameDrive);
/*  38:    */     }
/*  39: 73 */     while ((s.charAt(pos) == '\\') || 
/*  40: 74 */       (s.charAt(pos) == '/')) {
/*  41: 76 */       pos++;
/*  42:    */     }
/*  43: 79 */     while (pos < s.length())
/*  44:    */     {
/*  45: 81 */       int nextSepIndex1 = s.indexOf('/', pos);
/*  46: 82 */       int nextSepIndex2 = s.indexOf('\\', pos);
/*  47: 83 */       int nextSepIndex = 0;
/*  48: 84 */       String nextFileNameComponent = null;
/*  49: 86 */       if ((nextSepIndex1 != -1) && (nextSepIndex2 != -1)) {
/*  50: 89 */         nextSepIndex = Math.min(nextSepIndex1, nextSepIndex2);
/*  51: 91 */       } else if ((nextSepIndex1 == -1) || (nextSepIndex2 == -1)) {
/*  52: 94 */         nextSepIndex = Math.max(nextSepIndex1, nextSepIndex2);
/*  53:    */       }
/*  54: 97 */       if (nextSepIndex == -1)
/*  55:    */       {
/*  56:100 */         nextFileNameComponent = s.substring(pos);
/*  57:101 */         pos = s.length();
/*  58:    */       }
/*  59:    */       else
/*  60:    */       {
/*  61:105 */         nextFileNameComponent = s.substring(pos, nextSepIndex);
/*  62:106 */         pos = nextSepIndex + 1;
/*  63:    */       }
/*  64:109 */       if (!nextFileNameComponent.equals(".")) {
/*  65:113 */         if (nextFileNameComponent.equals("..")) {
/*  66:116 */           byteArray.add(parentDirectory);
/*  67:    */         } else {
/*  68:121 */           byteArray.add(StringHelper.getBytes(nextFileNameComponent, 
/*  69:122 */             ws));
/*  70:    */         }
/*  71:    */       }
/*  72:125 */       if (pos < s.length()) {
/*  73:127 */         byteArray.add(endOfSubdirectory);
/*  74:    */       }
/*  75:    */     }
/*  76:131 */     return byteArray.getBytes();
/*  77:    */   }
/*  78:    */   
/*  79:    */   private static byte[] getURL(String s, WorkbookSettings ws)
/*  80:    */   {
/*  81:136 */     ByteArray byteArray = new ByteArray();
/*  82:137 */     byteArray.add(unencodedUrl);
/*  83:138 */     byteArray.add((byte)s.length());
/*  84:139 */     byteArray.add(StringHelper.getBytes(s, ws));
/*  85:140 */     return byteArray.getBytes();
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.EncodedURLHelper
 * JD-Core Version:    0.7.0.1
 */