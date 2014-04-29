/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ public abstract class JdkVersion
/*   4:    */ {
/*   5:    */   public static final int JAVA_13 = 0;
/*   6:    */   public static final int JAVA_14 = 1;
/*   7:    */   public static final int JAVA_15 = 2;
/*   8:    */   public static final int JAVA_16 = 3;
/*   9:    */   public static final int JAVA_17 = 4;
/*  10: 63 */   private static final String javaVersion = System.getProperty("java.version");
/*  11:    */   private static final int majorJavaVersion;
/*  12:    */   
/*  13:    */   static
/*  14:    */   {
/*  15: 65 */     if (javaVersion.contains("1.7.")) {
/*  16: 66 */       majorJavaVersion = 4;
/*  17: 68 */     } else if (javaVersion.contains("1.6.")) {
/*  18: 69 */       majorJavaVersion = 3;
/*  19:    */     } else {
/*  20: 73 */       majorJavaVersion = 2;
/*  21:    */     }
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static String getJavaVersion()
/*  25:    */   {
/*  26: 85 */     return javaVersion;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static int getMajorJavaVersion()
/*  30:    */   {
/*  31: 99 */     return majorJavaVersion;
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Deprecated
/*  35:    */   public static boolean isAtLeastJava14()
/*  36:    */   {
/*  37:115 */     return true;
/*  38:    */   }
/*  39:    */   
/*  40:    */   @Deprecated
/*  41:    */   public static boolean isAtLeastJava15()
/*  42:    */   {
/*  43:130 */     return true;
/*  44:    */   }
/*  45:    */   
/*  46:    */   @Deprecated
/*  47:    */   public static boolean isAtLeastJava16()
/*  48:    */   {
/*  49:145 */     return majorJavaVersion >= 3;
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.JdkVersion
 * JD-Core Version:    0.7.0.1
 */