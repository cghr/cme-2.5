/*  1:   */ package org.springframework.context.expression;
/*  2:   */ 
/*  3:   */ import org.springframework.core.env.Environment;
/*  4:   */ import org.springframework.expression.AccessException;
/*  5:   */ import org.springframework.expression.EvaluationContext;
/*  6:   */ import org.springframework.expression.PropertyAccessor;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ 
/*  9:   */ public class EnvironmentAccessor
/* 10:   */   implements PropertyAccessor
/* 11:   */ {
/* 12:   */   public Class<?>[] getSpecificTargetClasses()
/* 13:   */   {
/* 14:35 */     return new Class[] { Environment.class };
/* 15:   */   }
/* 16:   */   
/* 17:   */   public boolean canRead(EvaluationContext context, Object target, String name)
/* 18:   */     throws AccessException
/* 19:   */   {
/* 20:43 */     return true;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public TypedValue read(EvaluationContext context, Object target, String name)
/* 24:   */     throws AccessException
/* 25:   */   {
/* 26:51 */     return new TypedValue(((Environment)target).getProperty(name));
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean canWrite(EvaluationContext context, Object target, String name)
/* 30:   */     throws AccessException
/* 31:   */   {
/* 32:59 */     return false;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 36:   */     throws AccessException
/* 37:   */   {}
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.expression.EnvironmentAccessor
 * JD-Core Version:    0.7.0.1
 */