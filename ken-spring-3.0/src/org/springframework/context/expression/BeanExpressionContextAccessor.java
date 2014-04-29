/*  1:   */ package org.springframework.context.expression;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*  4:   */ import org.springframework.expression.AccessException;
/*  5:   */ import org.springframework.expression.EvaluationContext;
/*  6:   */ import org.springframework.expression.PropertyAccessor;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ 
/*  9:   */ public class BeanExpressionContextAccessor
/* 10:   */   implements PropertyAccessor
/* 11:   */ {
/* 12:   */   public boolean canRead(EvaluationContext context, Object target, String name)
/* 13:   */     throws AccessException
/* 14:   */   {
/* 15:36 */     return ((BeanExpressionContext)target).containsObject(name);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypedValue read(EvaluationContext context, Object target, String name)
/* 19:   */     throws AccessException
/* 20:   */   {
/* 21:40 */     return new TypedValue(((BeanExpressionContext)target).getObject(name));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean canWrite(EvaluationContext context, Object target, String name)
/* 25:   */     throws AccessException
/* 26:   */   {
/* 27:44 */     return false;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 31:   */     throws AccessException
/* 32:   */   {
/* 33:48 */     throw new AccessException("Beans in a BeanFactory are read-only");
/* 34:   */   }
/* 35:   */   
/* 36:   */   public Class[] getSpecificTargetClasses()
/* 37:   */   {
/* 38:52 */     return new Class[] { BeanExpressionContext.class };
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.expression.BeanExpressionContextAccessor
 * JD-Core Version:    0.7.0.1
 */