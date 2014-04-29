/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ public class SpringVersion
/*  4:   */ {
/*  5:   */   public static String getVersion()
/*  6:   */   {
/*  7:40 */     Package pkg = SpringVersion.class.getPackage();
/*  8:41 */     return pkg != null ? pkg.getImplementationVersion() : null;
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.SpringVersion
 * JD-Core Version:    0.7.0.1
 */