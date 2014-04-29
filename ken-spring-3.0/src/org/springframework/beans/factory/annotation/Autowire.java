/*  1:   */ package org.springframework.beans.factory.annotation;
/*  2:   */ 
/*  3:   */ public enum Autowire
/*  4:   */ {
/*  5:40 */   NO(0),  BY_NAME(1),  BY_TYPE(2);
/*  6:   */   
/*  7:   */   private final int value;
/*  8:   */   
/*  9:   */   private Autowire(int value)
/* 10:   */   {
/* 11:57 */     this.value = value;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int value()
/* 15:   */   {
/* 16:61 */     return this.value;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isAutowire()
/* 20:   */   {
/* 21:70 */     return (this == BY_NAME) || (this == BY_TYPE);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.Autowire
 * JD-Core Version:    0.7.0.1
 */