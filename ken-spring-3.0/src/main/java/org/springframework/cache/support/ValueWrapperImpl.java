/*  1:   */ package org.springframework.cache.support;
/*  2:   */ 
/*  3:   */ import org.springframework.cache.Cache.ValueWrapper;
/*  4:   */ 
/*  5:   */ public class ValueWrapperImpl
/*  6:   */   implements Cache.ValueWrapper
/*  7:   */ {
/*  8:   */   private final Object value;
/*  9:   */   
/* 10:   */   public ValueWrapperImpl(Object value)
/* 11:   */   {
/* 12:32 */     this.value = value;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Object get()
/* 16:   */   {
/* 17:36 */     return this.value;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.support.ValueWrapperImpl
 * JD-Core Version:    0.7.0.1
 */