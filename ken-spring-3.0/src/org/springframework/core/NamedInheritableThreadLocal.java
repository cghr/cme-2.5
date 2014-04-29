/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class NamedInheritableThreadLocal<T>
/*  6:   */   extends InheritableThreadLocal<T>
/*  7:   */ {
/*  8:   */   private final String name;
/*  9:   */   
/* 10:   */   public NamedInheritableThreadLocal(String name)
/* 11:   */   {
/* 12:39 */     Assert.hasText(name, "Name must not be empty");
/* 13:40 */     this.name = name;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String toString()
/* 17:   */   {
/* 18:45 */     return this.name;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.NamedInheritableThreadLocal
 * JD-Core Version:    0.7.0.1
 */