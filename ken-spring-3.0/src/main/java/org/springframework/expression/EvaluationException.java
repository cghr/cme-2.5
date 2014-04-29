/*  1:   */ package org.springframework.expression;
/*  2:   */ 
/*  3:   */ public class EvaluationException
/*  4:   */   extends ExpressionException
/*  5:   */ {
/*  6:   */   public EvaluationException(int position, String message)
/*  7:   */   {
/*  8:33 */     super(position, message);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public EvaluationException(String expressionString, String message)
/* 12:   */   {
/* 13:42 */     super(expressionString, message);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public EvaluationException(int position, String message, Throwable cause)
/* 17:   */   {
/* 18:52 */     super(position, message, cause);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public EvaluationException(String message)
/* 22:   */   {
/* 23:60 */     super(message);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public EvaluationException(String message, Throwable cause)
/* 27:   */   {
/* 28:64 */     super(message, cause);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.EvaluationException
 * JD-Core Version:    0.7.0.1
 */