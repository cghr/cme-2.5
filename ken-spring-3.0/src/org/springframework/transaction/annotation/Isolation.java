/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ public enum Isolation
/*  4:   */ {
/*  5:37 */   DEFAULT(-1),  READ_UNCOMMITTED(1),  READ_COMMITTED(2),  REPEATABLE_READ(4),  SERIALIZABLE(8);
/*  6:   */   
/*  7:   */   private final int value;
/*  8:   */   
/*  9:   */   private Isolation(int value)
/* 10:   */   {
/* 11:84 */     this.value = value;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int value()
/* 15:   */   {
/* 16:86 */     return this.value;
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.Isolation
 * JD-Core Version:    0.7.0.1
 */