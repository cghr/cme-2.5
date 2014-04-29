/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.OperatorOverloader;
/*  6:   */ 
/*  7:   */ public class StandardOperatorOverloader
/*  8:   */   implements OperatorOverloader
/*  9:   */ {
/* 10:   */   public boolean overridesOperation(Operation operation, Object leftOperand, Object rightOperand)
/* 11:   */     throws EvaluationException
/* 12:   */   {
/* 13:31 */     return false;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Object operate(Operation operation, Object leftOperand, Object rightOperand)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:35 */     throw new EvaluationException("No operation overloaded by default");
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.StandardOperatorOverloader
 * JD-Core Version:    0.7.0.1
 */