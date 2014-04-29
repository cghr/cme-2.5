/*  1:   */ package org.springframework.instrument.classloading;
/*  2:   */ 
/*  3:   */ import org.springframework.core.OverridingClassLoader;
/*  4:   */ 
/*  5:   */ public class SimpleThrowawayClassLoader
/*  6:   */   extends OverridingClassLoader
/*  7:   */ {
/*  8:   */   public SimpleThrowawayClassLoader(ClassLoader parent)
/*  9:   */   {
/* 10:36 */     super(parent);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.SimpleThrowawayClassLoader
 * JD-Core Version:    0.7.0.1
 */