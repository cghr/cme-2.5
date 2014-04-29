/*  1:   */ package org.springframework.expression;
/*  2:   */ 
/*  3:   */ public class ExpressionInvocationTargetException
/*  4:   */   extends EvaluationException
/*  5:   */ {
/*  6:   */   public ExpressionInvocationTargetException(int position, String message, Throwable cause)
/*  7:   */   {
/*  8:30 */     super(position, message, cause);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public ExpressionInvocationTargetException(int position, String message)
/* 12:   */   {
/* 13:34 */     super(position, message);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ExpressionInvocationTargetException(String expressionString, String message)
/* 17:   */   {
/* 18:38 */     super(expressionString, message);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ExpressionInvocationTargetException(String message, Throwable cause)
/* 22:   */   {
/* 23:42 */     super(message, cause);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ExpressionInvocationTargetException(String message)
/* 27:   */   {
/* 28:46 */     super(message);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ExpressionInvocationTargetException
 * JD-Core Version:    0.7.0.1
 */