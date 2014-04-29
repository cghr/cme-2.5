/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ public class NoRollbackRuleAttribute
/*  4:   */   extends RollbackRuleAttribute
/*  5:   */ {
/*  6:   */   public NoRollbackRuleAttribute(Class clazz)
/*  7:   */   {
/*  8:35 */     super(clazz);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public NoRollbackRuleAttribute(String exceptionName)
/* 12:   */   {
/* 13:45 */     super(exceptionName);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String toString()
/* 17:   */   {
/* 18:50 */     return "No" + super.toString();
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.NoRollbackRuleAttribute
 * JD-Core Version:    0.7.0.1
 */