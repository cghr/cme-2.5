/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.security.MessageDigest;
/*   4:    */ import java.security.NoSuchAlgorithmException;
/*   5:    */ 
/*   6:    */ public abstract class DigestUtils
/*   7:    */ {
/*   8:    */   private static final String MD5_ALGORITHM_NAME = "MD5";
/*   9: 37 */   private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*  10:    */   
/*  11:    */   public static byte[] md5Digest(byte[] bytes)
/*  12:    */   {
/*  13: 45 */     return digest("MD5", bytes);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public static String md5DigestAsHex(byte[] bytes)
/*  17:    */   {
/*  18: 55 */     return digestAsHexString("MD5", bytes);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static StringBuilder appendMd5DigestAsHex(byte[] bytes, StringBuilder builder)
/*  22:    */   {
/*  23: 66 */     return appendDigestAsHex("MD5", bytes, builder);
/*  24:    */   }
/*  25:    */   
/*  26:    */   private static MessageDigest getDigest(String algorithm)
/*  27:    */   {
/*  28:    */     try
/*  29:    */     {
/*  30: 75 */       return MessageDigest.getInstance(algorithm);
/*  31:    */     }
/*  32:    */     catch (NoSuchAlgorithmException ex)
/*  33:    */     {
/*  34: 78 */       throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static byte[] digest(String algorithm, byte[] bytes)
/*  39:    */   {
/*  40: 83 */     return getDigest(algorithm).digest(bytes);
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static String digestAsHexString(String algorithm, byte[] bytes)
/*  44:    */   {
/*  45: 87 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/*  46: 88 */     return new String(hexDigest);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder)
/*  50:    */   {
/*  51: 92 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/*  52: 93 */     return builder.append(hexDigest);
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static char[] digestAsHexChars(String algorithm, byte[] bytes)
/*  56:    */   {
/*  57: 97 */     byte[] digest = digest(algorithm, bytes);
/*  58: 98 */     return encodeHex(digest);
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static char[] encodeHex(byte[] bytes)
/*  62:    */   {
/*  63:102 */     char[] chars = new char[32];
/*  64:103 */     for (int i = 0; i < chars.length; i += 2)
/*  65:    */     {
/*  66:104 */       byte b = bytes[(i / 2)];
/*  67:105 */       chars[i] = HEX_CHARS[(b >>> 4 & 0xF)];
/*  68:106 */       chars[(i + 1)] = HEX_CHARS[(b & 0xF)];
/*  69:    */     }
/*  70:108 */     return chars;
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.DigestUtils
 * JD-Core Version:    0.7.0.1
 */