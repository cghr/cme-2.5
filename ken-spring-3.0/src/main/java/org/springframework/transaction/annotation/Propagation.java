/*   1:    */ package org.springframework.transaction.annotation;
/*   2:    */ 
/*   3:    */ public enum Propagation
/*   4:    */ {
/*   5: 37 */   REQUIRED(0),  SUPPORTS(1),  MANDATORY(2),  REQUIRES_NEW(3),  NOT_SUPPORTED(4),  NEVER(5),  NESTED(6);
/*   6:    */   
/*   7:    */   private final int value;
/*   8:    */   
/*   9:    */   private Propagation(int value)
/*  10:    */   {
/*  11:101 */     this.value = value;
/*  12:    */   }
/*  13:    */   
/*  14:    */   public int value()
/*  15:    */   {
/*  16:103 */     return this.value;
/*  17:    */   }
/*  18:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.Propagation
 * JD-Core Version:    0.7.0.1
 */