/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public abstract class EnumerablePropertySource<T>
/*  6:   */   extends PropertySource<T>
/*  7:   */ {
/*  8:45 */   protected static final String[] EMPTY_NAMES_ARRAY = new String[0];
/*  9:   */   
/* 10:   */   public EnumerablePropertySource(String name, T source)
/* 11:   */   {
/* 12:49 */     super(name, source);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public abstract String[] getPropertyNames();
/* 16:   */   
/* 17:   */   public boolean containsProperty(String name)
/* 18:   */   {
/* 19:65 */     Assert.notNull(name, "property name must not be null");
/* 20:66 */     for (String candidate : getPropertyNames()) {
/* 21:67 */       if (candidate.equals(name)) {
/* 22:68 */         return true;
/* 23:   */       }
/* 24:   */     }
/* 25:71 */     return false;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.EnumerablePropertySource
 * JD-Core Version:    0.7.0.1
 */